package chalkinshmeal.lockout.artifacts.tasks;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class LockoutTask {
    private final ConfigHandler configHandler;
    private final LockoutTaskHandler lockoutTaskHandler;
    private boolean completed;
    protected String name;
    protected ItemStack item;
    protected TextComponent itemDisplayName;

    //---------------------------------------------------------------------------------------------
    // Constructor
    //---------------------------------------------------------------------------------------------
    public LockoutTask(ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler) {
        this.configHandler = configHandler;
        this.lockoutTaskHandler = lockoutTaskHandler;
        this.completed = false;
        this.name = "NotImplemented";
        this.item = new ItemStack(Material.DIRT);
        Utils.setDisplayName(item, this.itemDisplayName);
    }

    //---------------------------------------------------------------------------------------------
    // Accessor/Mutator methods
    //---------------------------------------------------------------------------------------------
    public ItemStack getItem() { return this.item; }
    public void setItemDisplayName(TextComponent displayName) { this.item = Utils.setDisplayName(this.item, displayName); }
    public boolean isComplete() { return this.completed; }

    //---------------------------------------------------------------------------------------------
    // Task methods
    //---------------------------------------------------------------------------------------------
    public void complete(UUID uuid) {
        this.completed = true;
        this.item = new ItemStack(Material.BARRIER);
        this.lockoutTaskHandler.complete(this, uuid);
    }

    //---------------------------------------------------------------------------------------------
    // Listeners that can be overridden by individual tasks 
    //---------------------------------------------------------------------------------------------
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {}
}
