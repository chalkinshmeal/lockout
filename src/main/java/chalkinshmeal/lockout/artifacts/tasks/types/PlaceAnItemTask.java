package chalkinshmeal.lockout.artifacts.tasks.types;

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

public class PlaceAnItemTask extends LockoutTask {
    private final Material material;

    //---------------------------------------------------------------------------------------------
    // Constructor, which takes lockouttaskhandler
    //---------------------------------------------------------------------------------------------
    public PlaceAnItemTask(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler, LockoutRewardHandler lockoutRewardHandler, Material material) {
        super(plugin, configHandler, lockoutTaskHandler, lockoutRewardHandler);
        this.material = material;
        this.name = "Place a " + Utils.getReadableMaterialName(material);
        this.item = new ItemStack(this.material);
    }

    //---------------------------------------------------------------------------------------------
    // Abstract methods
    //---------------------------------------------------------------------------------------------
    public void addListeners() {
		this.listeners.add(new PlaceAnItemTaskBlockPlaceEventListener(this));
    }

    //---------------------------------------------------------------------------------------------
    // Any listeners. Upon completion, LockoutTaskHandler.CompleteTask(player);
    //---------------------------------------------------------------------------------------------
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Material itemType = event.getBlock().getType();
        if (itemType != this.material) return;

        this.complete(player);
    }
}

//---------------------------------------------------------------------------------------------
// Private classes - any listeners that this task requires
//---------------------------------------------------------------------------------------------
class PlaceAnItemTaskBlockPlaceEventListener implements Listener {
    private final PlaceAnItemTask task;

    public PlaceAnItemTaskBlockPlaceEventListener(PlaceAnItemTask task) {
        this.task = task;
    }

    /** Event Handler */
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        if (this.task.isComplete()) return;
        this.task.onBlockPlaceEvent(event);
    }
}