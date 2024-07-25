package chalkinshmeal.labyrinth.artifacts.queue;

import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class Queue {
    private JavaPlugin plugin;
    private ConfigHandler config;
    private Location queue;

    public Queue(JavaPlugin plugin, ConfigHandler config, Location queue) {
        this.plugin = plugin;
        this.config = config;
        this.queue = queue;
    }

    public Location getLocation() { return this.queue; }

    // Config
    public void setQueueToConfig(String pathToQueue) {
        this.config.setLocation(pathToQueue, this.queue);
    }
    public static Queue getQueueFromConfig(JavaPlugin plugin, ConfigHandler config, String pathToQueue) {
        return new Queue(plugin, config, config.getLocation(pathToQueue));
    }
}
