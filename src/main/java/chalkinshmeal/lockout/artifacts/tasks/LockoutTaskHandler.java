package chalkinshmeal.lockout.artifacts.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.compass.LockoutCompass;
import chalkinshmeal.lockout.artifacts.rewards.LockoutRewardHandler;
import chalkinshmeal.lockout.artifacts.scoreboard.LockoutScoreboard;
import chalkinshmeal.lockout.artifacts.tasks.types.BreakItemsTask;
import chalkinshmeal.lockout.artifacts.tasks.types.DrinkMilkToCurePoisonTask;
import chalkinshmeal.lockout.artifacts.tasks.types.EatAnItemTask;
import chalkinshmeal.lockout.artifacts.tasks.types.GrowWheatWithBonemealTask;
import chalkinshmeal.lockout.artifacts.tasks.types.LightTNTTask;
import chalkinshmeal.lockout.artifacts.tasks.types.ObtainItemsTask;
import chalkinshmeal.lockout.artifacts.tasks.types.PlaceItemsTask;
import chalkinshmeal.lockout.artifacts.tasks.types.PlaceItemInItemFrameTask;
import chalkinshmeal.lockout.artifacts.tasks.types.PunchAnEntityWithItemTask;
import chalkinshmeal.lockout.artifacts.tasks.types.RideAnEntityTask;
import chalkinshmeal.lockout.artifacts.tasks.types.TeleportWithAnEnderpearlTask;
import chalkinshmeal.lockout.artifacts.tasks.types.WearFullIronArmorTask;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class LockoutTaskHandler {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final LockoutCompass lockoutCompass;
    private final LockoutScoreboard lockoutScoreboard;
    private LockoutRewardHandler lockoutRewardHandler;
    private final int maxLockoutTasks;
    private List<LockoutTask> tasks;

    public LockoutTaskHandler(JavaPlugin plugin, ConfigHandler configHandler, LockoutCompass lockoutCompass, LockoutScoreboard lockoutScoreboard) {
        this.plugin = plugin;
        this.configHandler = configHandler;
        this.lockoutCompass = lockoutCompass;
        this.lockoutScoreboard = lockoutScoreboard;
        this.lockoutRewardHandler = new LockoutRewardHandler(this.plugin);
        this.tasks = new ArrayList<>();
        this.maxLockoutTasks = this.configHandler.getInt("taskCount", 27);
    }

    // Create the list of tasks for this lockout challenge
    // Return true if successful, false if not
    public boolean CreateTaskList() {
        this.lockoutRewardHandler = new LockoutRewardHandler(this.plugin);
        List<LockoutTask> allTasks = new ArrayList<>();
        try {
            allTasks.addAll(BreakItemsTask.getBreakItemsTasks(plugin, configHandler, this, lockoutRewardHandler));
            allTasks.addAll(PlaceItemsTask.getPlaceItemsTasks(plugin, configHandler, this, lockoutRewardHandler));
            allTasks.addAll(ObtainItemsTask.getObtainItemsTasks(plugin, configHandler, this, lockoutRewardHandler));

            // Add tasks
            List<String> materialStrs = Utils.getRandomItems(this.configHandler.getListFromKey("eatAnItemItems"), 1);
            Material material = Material.valueOf(materialStrs.get(0));
            allTasks.add(new EatAnItemTask(this.plugin, this.configHandler, this, this.lockoutRewardHandler, material));

            List<String> mountStrs = Utils.getRandomItems(this.configHandler.getListFromKey("rideAnEntityMounts"), 1);
            EntityType mountType = EntityType.valueOf(mountStrs.get(0));
            allTasks.add(new RideAnEntityTask(plugin, configHandler, this, lockoutRewardHandler, mountType));

            List<String> entityTypeStrs = Utils.getRandomItems(this.configHandler.getListFromKey("punchAnEntityEntityTypes"), 1);
            List<String> itemStrs = Utils.getRandomItems(this.configHandler.getListFromKey("punchAnEntityItemTypes"), 1);
            EntityType punchEntityType = EntityType.valueOf(entityTypeStrs.get(0));
            Material punchMaterial = Material.valueOf(itemStrs.get(0));
            allTasks.add(new PunchAnEntityWithItemTask(plugin, configHandler, this, lockoutRewardHandler, punchEntityType, punchMaterial));

            materialStrs = Utils.getRandomItems(this.configHandler.getListFromKey("placeItemInItemFrameItemTypes"), 1);
            material = Material.valueOf(materialStrs.get(0));
            allTasks.add(new PlaceItemInItemFrameTask(this.plugin, this.configHandler, this, this.lockoutRewardHandler, material));

            allTasks.add(new WearFullIronArmorTask(plugin, configHandler, this, lockoutRewardHandler));
            allTasks.add(new TeleportWithAnEnderpearlTask(plugin, configHandler, this, lockoutRewardHandler));
            allTasks.add(new GrowWheatWithBonemealTask(plugin, configHandler, this, lockoutRewardHandler));
            allTasks.add(new LightTNTTask(plugin, configHandler, this, lockoutRewardHandler));
            allTasks.add(new DrinkMilkToCurePoisonTask(plugin, configHandler, this, lockoutRewardHandler));
        }
        catch (Exception e) {
            this.plugin.getLogger().warning("Could not create task list: " + e.getMessage());
            return false;
        }
        // Randomly get items
        this.tasks = Utils.getRandomItems(allTasks, Math.min(this.maxLockoutTasks, allTasks.size()));
        Collections.shuffle(this.tasks);

        // Initialize tasks (Generate rewards, set lore, etc.)
        for (LockoutTask task : this.tasks) {
            task.init();
        }

        return true;
    }

    //---------------------------------------------------------------------------------------------
	// Accessor/Mutator methods
    //---------------------------------------------------------------------------------------------
    public List<LockoutTask> GetTasks() { return this.tasks; }

    //---------------------------------------------------------------------------------------------
	// Task methods
    //---------------------------------------------------------------------------------------------
    public void complete(LockoutTask task, Player completedPlayer) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(Component.text()
                .append(Component.text(completedPlayer.getName(), NamedTextColor.GOLD))
                .append(Component.text(" has completed the task ", NamedTextColor.GRAY))
                .append(Component.text(task.name, NamedTextColor.BLUE)));
            if (task.reward != null) {
                player.sendMessage(Component.text()
                    .append(Component.text("  Reward: ", NamedTextColor.GRAY))
                    .append(Component.text(task.reward.getDescription(), NamedTextColor.LIGHT_PURPLE)));
            }
            Utils.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL);
        }

        this.lockoutCompass.updateTasksInventory(this);
        this.lockoutScoreboard.addScore(completedPlayer, task.value);
    }

    //---------------------------------------------------------------------------------------------
	// Register all server-wide listeners
    //---------------------------------------------------------------------------------------------
	public void registerListeners() {
        for (LockoutTask task : this.tasks) {
            task.registerListeners();
        }
	}

    public void unRegisterListeners() {
        for (LockoutTask task : this.tasks) { task.unRegisterListeners(); }
    }
}
