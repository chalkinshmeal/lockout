package chalkinshmeal.lockout.artifacts.tasks.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.rewards.LockoutRewardHandler;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTask;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTaskHandler;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.utils.Utils;

public class ReachVelocityTask extends LockoutTask {
    private static final String configKey = "reachVelocityTask";
    private static final String normalKey1 = "minVelocity";
    private static final String normalKey2 = "maxVelocity";
    private final int maxVelocity;
    private final Map<UUID, LocationTimePair> playerLastLocation = new HashMap<>();


    //---------------------------------------------------------------------------------------------
    // Constructor, which takes lockouttaskhandler
    //---------------------------------------------------------------------------------------------
    public ReachVelocityTask(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler,
                           LockoutRewardHandler lockoutRewardHandler, int maxVelocity) {
        super(plugin, configHandler, lockoutTaskHandler, lockoutRewardHandler);
        this.maxVelocity = maxVelocity;
        this.name = "Reach a velocity of " + this.maxVelocity + " blocks/second";
        this.item = new ItemStack(Material.ELYTRA);
    }

    //---------------------------------------------------------------------------------------------
    // Abstract methods
    //---------------------------------------------------------------------------------------------
    public void validateConfig() {
        this.configHandler.getInt(configKey + "." + normalKey1, 1);
        this.configHandler.getInt(configKey + "." + normalKey2, 1);
    }

    public void addListeners() {
		this.listeners.add(new ReachVelocityTaskPlayerMoveEventListener(this));
    }

    //---------------------------------------------------------------------------------------------
    // Task getter
    //---------------------------------------------------------------------------------------------
    public static List<ReachVelocityTask> getTasks(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler,
                                                          LockoutRewardHandler lockoutRewardHandler) {
        List<ReachVelocityTask> tasks = new ArrayList<>();
        int targetVelocity = -1;
        int minVelocity = configHandler.getInt(configKey + "." + normalKey1, 10);
        int maxVelocity = configHandler.getInt(configKey + "." + normalKey2, 10);
        targetVelocity = Utils.getRandNum(minVelocity, maxVelocity);
        tasks.add(new ReachVelocityTask(plugin, configHandler, lockoutTaskHandler, lockoutRewardHandler, targetVelocity));
        return tasks;
    }

    //---------------------------------------------------------------------------------------------
    // Any listeners. Upon completion, LockoutTaskHandler.CompleteTask(player);
    //---------------------------------------------------------------------------------------------
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Get the current location and time
        Location currentLocation = player.getLocation();
        long currentTime = System.currentTimeMillis();

        // Check if we have a previous location for the player
        if (!playerLastLocation.containsKey(playerId)) {
            playerLastLocation.put(playerId, new LocationTimePair(currentLocation, currentTime));
            return;
        }

        // Get the previous location and time
        LocationTimePair lastLocationTime = playerLastLocation.get(playerId);
        Location lastLocation = lastLocationTime.getLocation();
        long lastTime = lastLocationTime.getTime();

        // Calculate the distance traveled and time elapsed
        double distance = currentLocation.distance(lastLocation);
        double timeElapsed = (currentTime - lastTime) / 1000.0;  // Convert milliseconds to seconds

        // Calculate the velocity in blocks per second
        double velocity = distance / timeElapsed;

        // Update the player's last location and time
        playerLastLocation.put(playerId, new LocationTimePair(currentLocation, currentTime));

        // Check if the velocity exceeds the target velocity
        if (velocity > 10000) return;
        if (velocity < this.maxVelocity) return;

        this.complete(player);
    }

    // Utility class to store a location and the time it was recorded
    private static class LocationTimePair {
        private final Location location;
        private final long time;

        public LocationTimePair(Location location, long time) {
            this.location = location;
            this.time = time;
        }

        public Location getLocation() {
            return location;
        }

        public long getTime() {
            return time;
        }
    }
}

//---------------------------------------------------------------------------------------------
// Private classes - any listeners that this task requires
//---------------------------------------------------------------------------------------------
class ReachVelocityTaskPlayerMoveEventListener implements Listener {
    private final ReachVelocityTask task;

    public ReachVelocityTaskPlayerMoveEventListener(ReachVelocityTask task) {
        this.task = task;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (this.task.isComplete()) return;
        this.task.onPlayerMoveEvent(event);
    }
}
