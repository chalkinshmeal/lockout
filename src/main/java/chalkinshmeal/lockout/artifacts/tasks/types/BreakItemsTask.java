package chalkinshmeal.lockout.artifacts.tasks.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.rewards.LockoutRewardHandler;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTask;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTaskHandler;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.utils.Utils;



public class BreakItemsTask extends LockoutTask {
    private final Material material;
    private final int amount;
    private final Map<Player, Integer> brokenItems;
    private static final String configKey = "breakItemsTask";

    //---------------------------------------------------------------------------------------------
    // Constructor, which takes lockouttaskhandler
    //---------------------------------------------------------------------------------------------
    public BreakItemsTask(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler,
                          LockoutRewardHandler lockoutRewardHandler, Material material, int amount) {
        super(plugin, configHandler, lockoutTaskHandler, lockoutRewardHandler);
        this.material = material;
        this.amount = amount;
        this.brokenItems = new HashMap<>();
        this.name = "Break " + this.amount + " " + Utils.getReadableMaterialName(material);
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
		this.listeners.add(new BreakItemsTaskBlockBreakEventListener(this));
    }

    //---------------------------------------------------------------------------------------------
    // Task getter
    //---------------------------------------------------------------------------------------------
    public static List<BreakItemsTask> getBreakItemsTasks(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler,
                                                          LockoutRewardHandler lockoutRewardHandler) {
        List<BreakItemsTask> tasks = new ArrayList<>();
        int taskCount = 3;
        String configKey = "breakItemsTask";
        List<String> materialStrs = Utils.getRandomItems(configHandler.getKeyListFromKey(configKey), taskCount);

        for (int i = 0; i < taskCount; i++) {
            String materialStr = materialStrs.get(i);
            Material _material = Material.valueOf(materialStrs.get(i));
            int _amount = configHandler.getInt(configKey + "." + materialStr, 1);
            tasks.add(new BreakItemsTask(plugin, configHandler, lockoutTaskHandler, lockoutRewardHandler, _material, _amount));
        }
        return tasks;
    }

    //---------------------------------------------------------------------------------------------
    // Any listeners. Upon completion, LockoutTaskHandler.CompleteTask(player);
    //---------------------------------------------------------------------------------------------
    public void onBlockBreakEvent(BlockBreakEvent event) {
        // Return if material does not match
        Material itemType = event.getBlock().getType();
        if (itemType != this.material) return;

        // Return if 
        Player player = event.getPlayer();
        this.brokenItems.put(player, this.brokenItems.getOrDefault(player, 0) + 1);
        if (this.brokenItems.get(player) < this.amount) return;
        this.complete(player);
    }
}

//---------------------------------------------------------------------------------------------
// Private classes - any listeners that this task requires
//---------------------------------------------------------------------------------------------
class BreakItemsTaskBlockBreakEventListener implements Listener {
    private final BreakItemsTask task;

    public BreakItemsTaskBlockBreakEventListener(BreakItemsTask task) {
        this.task = task;
    }

    /** Event Handler */
    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (this.task.isComplete()) return;
        this.task.onBlockBreakEvent(event);
    }
}
