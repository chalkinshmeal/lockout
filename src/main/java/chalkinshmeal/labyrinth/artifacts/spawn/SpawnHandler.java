package chalkinshmeal.labyrinth.artifacts.spawn;

import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpawnHandler {
    private JavaPlugin plugin;
    private ConfigHandler config;
    private List<Spawn> spawns;

    public SpawnHandler(JavaPlugin plugin, ConfigHandler config, String labyrinthName) {
        this.plugin = plugin;
        this.config = config;
        this.spawns = this.getSpawnsFromConfig(this.plugin, this.config, "labyrinths." + labyrinthName + ".spawns");
    }

    public void addSpawn(Location loc) { this.spawns.add(new Spawn(plugin, config, loc)); }
    public void clearSpawn() { this.spawns.clear(); }

    public Spawn getSpawn(int index) { return this.spawns.get(index); }
    public Spawn getRandSpawn() { return this.spawns.get(this.getRandNum(0, this.getSpawnCount()-1)); }
    public List<Spawn> getSpawns() { return this.spawns; }
    public int getSpawnCount() { return this.spawns.size(); }

    // Utilities
    public int getRandNum(int lo, int hi) {
        Random random = new Random();
        return random.nextInt((hi-lo)+1) + lo;
    }

    // Config
    public void setSpawnsToConfig(String pathToSpawns) {
        for (Spawn spawn : this.getSpawns()) {
            Integer index = this.getSpawns().indexOf(spawn);
            spawn.setSpawnToConfig(pathToSpawns + "." + index);
        }
    }
    public static List<Spawn> getSpawnsFromConfig(JavaPlugin plugin, ConfigHandler config, String pathToSpawns) {
        if (!config.doesKeyExist(pathToSpawns)) return new ArrayList<>();

        List<Spawn> spawns = new ArrayList<>();
        for (String labyrinthName : config.getKeyListFromKey(pathToSpawns)) {
            Integer index = config.getKeyListFromKey(pathToSpawns).indexOf(labyrinthName);
            spawns.add(Spawn.getSpawnFromConfig(plugin, config,pathToSpawns + "." + index));
        }
        return spawns;
    }
}
