package chalkinshmeal.lockout.artifacts.tasks.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.rewards.LockoutRewardHandler;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTask;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTaskHandler;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.utils.Utils;



public class PlaceItemsTask extends LockoutTask {
    private final Material material;
    private final int amount;
    private final Map<Player, Integer> placedItems;
    private final static String configKey = "placeAnItemItemTypes";

    //---------------------------------------------------------------------------------------------
    // Constructor, which takes lockouttaskhandler
    //---------------------------------------------------------------------------------------------
    public PlaceItemsTask(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler,
                          LockoutRewardHandler lockoutRewardHandler, Material material, int amount) {
        super(plugin, configHandler, lockoutTaskHandler, lockoutRewardHandler);
        this.material = material;
        this.amount = amount;
        this.placedItems = new HashMap<>();
        this.name = "Place " + this.amount + " " + Utils.getReadableMaterialName(material);
        this.item = new ItemStack(this.material);
    }

    //---------------------------------------------------------------------------------------------
    // Abstract methods
    //---------------------------------------------------------------------------------------------
    public void validateConfig() {
        for (String materialStr : this.configHandler.getKeyListFromKey(PlaceItemsTask.configKey)) {
            Material.valueOf(materialStr);
        }
    }

    public void addListeners() {
		this.listeners.add(new PlaceItemsTaskBlockPlaceEventListener(this));
    }

    //---------------------------------------------------------------------------------------------
    // Task getter
    //---------------------------------------------------------------------------------------------
    public static List<PlaceItemsTask> getPlaceItemsTasks(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler,
                                                          LockoutRewardHandler lockoutRewardHandler) {
        List<PlaceItemsTask> tasks = new ArrayList<>();
        int taskCount = 3;
        List<String> materialStrs = Utils.getRandomItems(configHandler.getKeyListFromKey(PlaceItemsTask.configKey), taskCount);

        // Check that materials are good
        for (String materialStr : configHandler.getKeyListFromKey(PlaceItemsTask.configKey)) { Material.valueOf(materialStr); }

        for (int i = 0; i < taskCount; i++) {
            String materialStr = materialStrs.get(i);
            Material _material = Material.valueOf(materialStrs.get(i));
            int _amount = configHandler.getInt(PlaceItemsTask.configKey + "." + materialStr, 1);
            tasks.add(new PlaceItemsTask(plugin, configHandler, lockoutTaskHandler, lockoutRewardHandler, _material, _amount));
        }
        return tasks;
    }

    //---------------------------------------------------------------------------------------------
    // Any listeners. Upon completion, LockoutTaskHandler.CompleteTask(player);
    //---------------------------------------------------------------------------------------------
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        // Return if material does not match
        Material itemType = event.getBlock().getType();
        if (itemType != this.material) return;

        // Return if 
        Player player = event.getPlayer();
        this.placedItems.put(player, this.placedItems.getOrDefault(player, 0) + 1);
        if (this.placedItems.get(player) < this.amount) return;
        this.complete(player);
    }
}

//---------------------------------------------------------------------------------------------
// Private classes - any listeners that this task requires
//---------------------------------------------------------------------------------------------
class PlaceItemsTaskBlockPlaceEventListener implements Listener {
    private final PlaceItemsTask task;

    public PlaceItemsTaskBlockPlaceEventListener(PlaceItemsTask task) {
        this.task = task;
    }

    /** Event Handler */
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        if (this.task.isComplete()) return;
        this.task.onBlockPlaceEvent(event);
    }
}
