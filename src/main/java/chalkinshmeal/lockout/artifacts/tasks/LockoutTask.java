package chalkinshmeal.lockout.artifacts.tasks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.rewards.LockoutReward;
import chalkinshmeal.lockout.artifacts.rewards.LockoutRewardHandler;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public abstract class LockoutTask {
    protected final JavaPlugin plugin;
    private final LockoutTaskHandler lockoutTaskHandler;
    private final LockoutRewardHandler lockoutRewardHandler;
    protected List<Listener> listeners;
    private boolean completed;

    protected String name;
    protected ItemStack item;
    protected TextComponent itemDisplayName;
    protected int value;
    protected LockoutReward reward;

    //---------------------------------------------------------------------------------------------
    // Constructor
    //---------------------------------------------------------------------------------------------
    public LockoutTask(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler, LockoutRewardHandler lockoutRewardHandler) {
        this.plugin = plugin;
        this.lockoutTaskHandler = lockoutTaskHandler;
        this.lockoutRewardHandler = lockoutRewardHandler;
        this.completed = false;
        this.listeners = new ArrayList<>();
        this.name = "NotImplemented";
        this.item = new ItemStack(Material.DIRT);
        this.value = 1;
        this.reward = null;
        Utils.setDisplayName(item, this.itemDisplayName);
    }

    //---------------------------------------------------------------------------------------------
    // Accessor/Mutator methods
    //---------------------------------------------------------------------------------------------
    public void init() {
        this.reward = (this.value > 0) ? this.lockoutRewardHandler.getRandomReward() : this.lockoutRewardHandler.getRandomPunishment();
        this.setItemDisplayName(Component.text(this.name, NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false));
        this.addLore();
        this.addListeners();
    }

    public ItemStack getItem() { return this.item; }
    public void setItemDisplayName(TextComponent displayName) { this.item = Utils.setDisplayName(this.item, displayName); }
    public void addLore() {
        Component valueLore = Component.text("Value: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text(String.valueOf(this.value), NamedTextColor.GOLD));
        Component rewardLore = Component.text("Reward: ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false)
                .append(Component.text((this.reward == null) ? "Nothing" : this.reward.getDescription(), NamedTextColor.LIGHT_PURPLE));

        this.item = Utils.addLore(this.item, valueLore);
        this.item = Utils.addLore(this.item, rewardLore);
    }
    public boolean isComplete() { return this.completed; }

    //---------------------------------------------------------------------------------------------
    // Task methods
    //---------------------------------------------------------------------------------------------
    public void complete(Player player) {
        this.completed = true;
        this.item = Utils.setMaterial(this.item, Material.GRAY_STAINED_GLASS_PANE);
        this.lockoutTaskHandler.complete(this, player);
        this.unRegisterListeners();
        if (this.reward != null) this.reward.giveReward(player);
    }

    //---------------------------------------------------------------------------------------------
    // Listener methods
    //---------------------------------------------------------------------------------------------
    public abstract void addListeners();
    public void registerListeners() {
		PluginManager manager = this.plugin.getServer().getPluginManager();
        for (Listener l : this.listeners) { manager.registerEvents(l, this.plugin); }
    }
    public void unRegisterListeners() {
        for (Listener l : this.listeners) { HandlerList.unregisterAll(l); }
    }
}
