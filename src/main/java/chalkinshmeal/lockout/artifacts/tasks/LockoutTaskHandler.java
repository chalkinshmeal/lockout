package chalkinshmeal.lockout.artifacts.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.compass.LockoutCompass;
import chalkinshmeal.lockout.artifacts.tasks.types.EatAnItem;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.listeners.game.PlayerItemConsumeListener;

public class LockoutTaskHandler {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final LockoutCompass lockoutCompass;
    private List<LockoutTask> tasks;
    private List<Listener> listeners;

    public LockoutTaskHandler(JavaPlugin plugin, ConfigHandler configHandler, LockoutCompass lockoutCompass) {
        this.plugin = plugin;
        this.configHandler = configHandler;
        this.lockoutCompass = lockoutCompass;
        this.tasks = new ArrayList<>();
        this.listeners = new ArrayList<>();

        this.CreateTaskList();
    }

    public void CreateTaskList() {
        this.tasks = new ArrayList<>();

        // EatAnItem task
        for (String materialStr : this.configHandler.getListFromKey("eatAnItemItems")) {
            Material material = Material.valueOf(materialStr.toUpperCase());
            this.tasks.add(new EatAnItem(this.configHandler, this, material));
            System.out.println("Adding task: " + this.tasks.get(this.tasks.size() - 1).name);
        }
    }

    //---------------------------------------------------------------------------------------------
	// Accessor/Mutator methods
    //---------------------------------------------------------------------------------------------
    public List<LockoutTask> GetTasks() { return this.tasks; }

    //---------------------------------------------------------------------------------------------
	// Task methods
    //---------------------------------------------------------------------------------------------
    public void complete(LockoutTask task, UUID uuid) {
        Player completedPlayer = this.plugin.getServer().getPlayer(uuid);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(completedPlayer.getName() + " completed task " + task.name);
        }

        this.lockoutCompass.updateTasksInventory(this);
    }

    //---------------------------------------------------------------------------------------------
	// Iterators for all tasks' event listeners
    //---------------------------------------------------------------------------------------------
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) { for (LockoutTask task : this.tasks) if (!task.isComplete()) task.onPlayerItemConsumeEvent(event); }

    //---------------------------------------------------------------------------------------------
	// Register all server-wide listeners
    //---------------------------------------------------------------------------------------------
	public void registerListeners() {
		PluginManager manager = this.plugin.getServer().getPluginManager();
		this.listeners.add(new PlayerItemConsumeListener(this));

        for (Listener l : this.listeners) { manager.registerEvents(l, this.plugin); }
	}

    public void unRegisterListeners() {
        for (Listener l : this.listeners) { HandlerList.unregisterAll(l); }
    }
}
