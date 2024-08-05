package chalkinshmeal.lockout.artifacts.tasks.specific;

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

public class ObtainMusicDiskTask extends LockoutTask {
    //---------------------------------------------------------------------------------------------
    // Constructor, which takes lockouttaskhandler
    //---------------------------------------------------------------------------------------------
    public ObtainMusicDiskTask(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler,
                           LockoutRewardHandler lockoutRewardHandler) {
        super(plugin, configHandler, lockoutTaskHandler, lockoutRewardHandler);
        this.name = "Obtain a music disc";
        this.item = new ItemStack(Material.MUSIC_DISC_CAT);
    }

    //---------------------------------------------------------------------------------------------
    // Abstract methods
    //---------------------------------------------------------------------------------------------
    public void validateConfig() {}

    public void addListeners() {
		this.listeners.add(new ObtainMusicDiskTaskEntityPickupItemEventListener(this));
		this.listeners.add(new ObtainMusicDiskTaskInventoryClickEventListener(this));
    }

    //---------------------------------------------------------------------------------------------
    // Task getter
    //---------------------------------------------------------------------------------------------
    public static List<ObtainMusicDiskTask> getTasks(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler,
                                                          LockoutRewardHandler lockoutRewardHandler) {
        List<ObtainMusicDiskTask> tasks = new ArrayList<>();
        tasks.add(new ObtainMusicDiskTask(plugin, configHandler, lockoutTaskHandler, lockoutRewardHandler));
        return tasks;
    }

    //---------------------------------------------------------------------------------------------
    // Any listeners. Upon completion, LockoutTaskHandler.CompleteTask(player);
    //---------------------------------------------------------------------------------------------
    public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Material itemType = event.getItem().getItemStack().getType();
        if (!itemType.name().startsWith("MUSIC_DISC_")) return;

        this.complete(player);
    }

    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;

        Material itemType = event.getCurrentItem().getType();
        if (!itemType.name().startsWith("MUSIC_DISC_")) return;

        this.complete(player);
    }
}

//---------------------------------------------------------------------------------------------
// Private classes - any listeners that this task requires
//---------------------------------------------------------------------------------------------
class ObtainMusicDiskTaskEntityPickupItemEventListener implements Listener {
    private final ObtainMusicDiskTask task;

    public ObtainMusicDiskTaskEntityPickupItemEventListener(ObtainMusicDiskTask task) {
        this.task = task;
    }

    /** Event Handler */
    @EventHandler
    public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
        if (this.task.isComplete()) return;
        this.task.onEntityPickupItemEvent(event);
    }
}

class ObtainMusicDiskTaskInventoryClickEventListener implements Listener {
    private final ObtainMusicDiskTask task;

    public ObtainMusicDiskTaskInventoryClickEventListener(ObtainMusicDiskTask task) {
        this.task = task;
    }

    /** Event Handler */
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (this.task.isComplete()) return;
        this.task.onInventoryClickEvent(event);
    }
}