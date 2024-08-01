package chalkinshmeal.lockout.artifacts.tasks.types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.rewards.LockoutRewardHandler;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTask;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTaskHandler;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.utils.Utils;

public class ObtainItemsTask extends LockoutTask {
    private final Material material;
    private final int amount;
    private final static String configKey = "obtainAnItemItemTypes";

    //---------------------------------------------------------------------------------------------
    // Constructor, which takes lockouttaskhandler
    //---------------------------------------------------------------------------------------------
    public ObtainItemsTask(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler, LockoutRewardHandler lockoutRewardHandler, Material material, int amount) {
        super(plugin, configHandler, lockoutTaskHandler, lockoutRewardHandler);
        this.material = material;
        this.amount = amount;
        this.name = "Obtain " + ((this.amount == 1 ? "a" : this.amount)) + " " + Utils.getReadableMaterialName(material);
        this.item = new ItemStack(this.material);
    }

    //---------------------------------------------------------------------------------------------
    // Abstract methods
    //---------------------------------------------------------------------------------------------
    public void validateConfig() {
        for (String materialStr : this.configHandler.getKeyListFromKey(configKey)) {
            Material.valueOf(materialStr);
        }
    }

    public void addListeners() {
		this.listeners.add(new ObtainItemsTaskEntityPickupItemEventListener(this));
		this.listeners.add(new ObtainItemsTaskInventoryClickEventListener(this));
    }

    //---------------------------------------------------------------------------------------------
    // Task getter
    //---------------------------------------------------------------------------------------------
    public static List<ObtainItemsTask> getObtainItemsTasks(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler,
                                                          LockoutRewardHandler lockoutRewardHandler) {
        List<ObtainItemsTask> tasks = new ArrayList<>();
        int taskCount = 3;
        List<String> materialStrs = Utils.getRandomItems(configHandler.getKeyListFromKey(ObtainItemsTask.configKey), taskCount);

        // Check that materials are good
        for (String materialStr : configHandler.getKeyListFromKey(ObtainItemsTask.configKey)) { Material.valueOf(materialStr); }

        for (int i = 0; i < taskCount; i++) {
            String materialStr = materialStrs.get(i);
            Material material = Material.valueOf(materialStrs.get(i));
            int amount = configHandler.getInt(ObtainItemsTask.configKey + "." + materialStr, 1);
            tasks.add(new ObtainItemsTask(plugin, configHandler, lockoutTaskHandler, lockoutRewardHandler, material, amount));
        }
        return tasks;
    }

    //---------------------------------------------------------------------------------------------
    // Any listeners. Upon completion, LockoutTaskHandler.CompleteTask(player);
    //---------------------------------------------------------------------------------------------
    public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Material itemType = event.getItem().getItemStack().getType();
        if (itemType != this.material) return;

        int amountNeededBeforePickup = this.amount - event.getItem().getItemStack().getAmount();
        if (!Utils.hasMaterial(player, this.material, amountNeededBeforePickup)) return;

        this.complete(player);
    }
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;

        Material itemType = event.getCurrentItem().getType();
        if (itemType != this.material) return;
        if (!Utils.hasMaterial(player, this.material, this.amount)) return;

        this.complete(player);
    }
}

//---------------------------------------------------------------------------------------------
// Private classes - any listeners that this task requires
//---------------------------------------------------------------------------------------------
class ObtainItemsTaskEntityPickupItemEventListener implements Listener {
    private final ObtainItemsTask task;

    public ObtainItemsTaskEntityPickupItemEventListener(ObtainItemsTask task) {
        this.task = task;
    }

    /** Event Handler */
    @EventHandler
    public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
        if (this.task.isComplete()) return;
        this.task.onEntityPickupItemEvent(event);
    }
}

class ObtainItemsTaskInventoryClickEventListener implements Listener {
    private final ObtainItemsTask task;

    public ObtainItemsTaskInventoryClickEventListener(ObtainItemsTask task) {
        this.task = task;
    }

    /** Event Handler */
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (this.task.isComplete()) return;
        this.task.onInventoryClickEvent(event);
    }
}