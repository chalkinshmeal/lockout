package chalkinshmeal.lockout.artifacts.tasks.types;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.rewards.LockoutRewardHandler;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTask;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTaskHandler;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.utils.Utils;

public class RideAnEntityTask extends LockoutTask {
    private final EntityType mountType;

    //---------------------------------------------------------------------------------------------
    // Constructor, which takes lockouttaskhandler
    //---------------------------------------------------------------------------------------------
    public RideAnEntityTask(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler, LockoutRewardHandler lockoutRewardHandler, EntityType mountType) {
        super(plugin, configHandler, lockoutTaskHandler, lockoutRewardHandler);
        this.mountType = mountType;
        this.name = "Ride a " + Utils.getReadableEntityTypeName(this.mountType);
        this.item = new ItemStack(Material.SADDLE);
        this.value = 1;
    }

    //---------------------------------------------------------------------------------------------
    // Abstract methods
    //---------------------------------------------------------------------------------------------
    public void validateConfig() {}

    public void addListeners() {
		this.listeners.add(new RideAnEntityTaskListener(this));
    }

    //---------------------------------------------------------------------------------------------
    // Any listeners. Upon completion, LockoutTaskHandler.CompleteTask(player);
    //---------------------------------------------------------------------------------------------
    public void onEntityMountEvent(EntityMountEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getMount().getType() != this.mountType) return;
        this.complete((Player) event.getEntity());
    }
}

//---------------------------------------------------------------------------------------------
// Private classes - any listeners that this task requires
//---------------------------------------------------------------------------------------------
class RideAnEntityTaskListener implements Listener {
    private final RideAnEntityTask task;

    public RideAnEntityTaskListener(RideAnEntityTask task) {
        this.task = task;
    }

    /** Event Handler */
    @EventHandler
    public void onEntityMountEvent(EntityMountEvent event) {
        if (this.task.isComplete()) return;
        this.task.onEntityMountEvent(event);
    }
}

