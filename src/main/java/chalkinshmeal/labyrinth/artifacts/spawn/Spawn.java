package chalkinshmeal.labyrinth.artifacts.spawn;

import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class Spawn {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final Location spawn;

    public Spawn(JavaPlugin plugin, ConfigHandler config, Location spawn) {
        this.plugin = plugin;
        this.config = config;
        this.spawn = spawn;
    }

    public Location getLocation() { return this.spawn; }

    // Config
    public void setSpawnToConfig(String pathToSpawn) {
        this.config.setLocation(pathToSpawn, this.spawn);
    }
    public static Spawn getSpawnFromConfig(JavaPlugin plugin, ConfigHandler config, String pathToSpawn) {
        return new Spawn(plugin, config, config.getLocation(pathToSpawn));
    }
}
