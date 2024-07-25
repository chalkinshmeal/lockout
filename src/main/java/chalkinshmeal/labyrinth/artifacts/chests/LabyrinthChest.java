package chalkinshmeal.labyrinth.artifacts.chests;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.labyrinth.data.ConfigHandler;

public class LabyrinthChest {
    private JavaPlugin plugin;
    private ConfigHandler config;
    private Location loc;

    public LabyrinthChest(JavaPlugin plugin, ConfigHandler config, Location loc) {
        this.plugin = plugin;
        this.config = config;
        this.loc = loc;
    }

    public Location getLocation() { return this.loc; }

    // Config
    public void setChestToConfig(String pathToChest) {
        this.config.setLocation(pathToChest, this.loc);
    }
    public static LabyrinthChest getChestFromConfig(JavaPlugin plugin, ConfigHandler config, String pathToChest) {
        return new LabyrinthChest(plugin, config, config.getLocation(pathToChest));
    }
}
