package chalkinshmeal.lockout.artifacts.tasks.types;

import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.rewards.LockoutRewardHandler;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTask;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTaskHandler;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.utils.Utils;

public class PlaceItemInItemFrameTask extends LockoutTask {
    private final Material material;

    //---------------------------------------------------------------------------------------------
    // Constructor, which takes lockouttaskhandler
    //---------------------------------------------------------------------------------------------
    public PlaceItemInItemFrameTask(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler, LockoutRewardHandler lockoutRewardHandler, Material material) {
        super(plugin, configHandler, lockoutTaskHandler, lockoutRewardHandler);
        this.material = material;
        this.name = "Place a " + Utils.getReadableMaterialName(material) + " in an item frame";
        this.item = new ItemStack(Material.ITEM_FRAME);
    }

    //---------------------------------------------------------------------------------------------
    // Abstract methods
    //---------------------------------------------------------------------------------------------
    public void addListeners() {
		this.listeners.add(new PlaceItemInItemFrameTaskPlayerItemConsumeListener(this));
    }

    //---------------------------------------------------------------------------------------------
    // Any listeners. Upon completion, LockoutTaskHandler.CompleteTask(player);
    //---------------------------------------------------------------------------------------------
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame)) return;
        Player player = event.getPlayer();
        Material itemInHand = player.getInventory().getItemInMainHand().getType();
        if (itemInHand != this.material) return;
        ItemFrame itemFrame = (ItemFrame) event.getRightClicked();
        if (itemFrame.getItem().getType() != Material.AIR) return;

        this.complete(event.getPlayer());
    }
}

//---------------------------------------------------------------------------------------------
// Private classes - any listeners that this task requires
//---------------------------------------------------------------------------------------------
class PlaceItemInItemFrameTaskPlayerItemConsumeListener implements Listener {
    private final PlaceItemInItemFrameTask task;

    public PlaceItemInItemFrameTaskPlayerItemConsumeListener(PlaceItemInItemFrameTask task) {
        this.task = task;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        if (this.task.isComplete()) return;
        this.task.onPlayerInteractEntityEvent(event);
    }
}

