package chalkinshmeal.labyrinth.artifacts.labyrinth;

import chalkinshmeal.labyrinth.artifacts.chests.LabyrinthChestHandler;
import chalkinshmeal.labyrinth.artifacts.queue.QueueHandler;
import chalkinshmeal.labyrinth.artifacts.spawn.Spawn;
import chalkinshmeal.labyrinth.artifacts.spawn.SpawnHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static chalkinshmeal.labyrinth.utils.Utils.getRandNum;

public class Labyrinth {
    private final ConfigHandler config;
    private final String name;
    private final World world;
    private final Location pos1;
    private final Location pos2;
    private final BoundingBox boundingBox;
    private final SpawnHandler spawnHandler;
    private final QueueHandler queueHandler;
    private final LabyrinthChestHandler chestHandler;

    public Labyrinth(JavaPlugin plugin, ConfigHandler config, String name, Location pos1, Location pos2) {
        this.config = config;
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.world = pos1.getWorld();
        this.boundingBox = this.calculateBoundingBox(pos1, pos2);
        this.spawnHandler = new SpawnHandler(plugin, config, name);
        this.queueHandler = new QueueHandler(plugin, config, name);
        this.chestHandler = new LabyrinthChestHandler(plugin, config, name);
    }

    // Getters
    public String getName() { return this.name; }
    public Integer getQueueCount() { return this.queueHandler.getQueueCount(); }
    public Integer getChestCount() { return this.chestHandler.getChestCount(); }
    public Integer getSpawnCount() { return this.spawnHandler.getSpawnCount(); }
    public Location getSpawn(int index) {
        return this.spawnHandler.getSpawn(index).getLocation();
    }
    public Location getRandQueue() { return this.queueHandler.getRandQueue().getLocation(); }
    public Location getRandSpawn() { return this.spawnHandler.getRandSpawn().getLocation(); }
    public BoundingBox getBoundingBox() { return this.boundingBox; }
    public Location getRandSurfaceLocation() {
        Block b = null;
        while (b == null || b.getType().isAir()) {
            int x = getRandNum((int) pos1.getX(), (int) pos2.getX());
            int z = getRandNum((int) pos1.getZ(), (int) pos2.getZ());
            int y = this.world.getHighestBlockYAt(x, z);
            b = this.world.getBlockAt(x, y, z);
        }
        return b.getLocation().add(0, 1, 0);
    }
    public BoundingBox calculateBoundingBox(Location pos1, Location pos2) {
        int x_l = (int) (min(pos1.getX(), pos2.getX()) - 1); int x_h = (int) (max(pos1.getX(), pos2.getX()) + 1);
        int y_l = (int) (min(pos1.getY(), pos2.getY()) - 1); int y_h = (int) (max(pos1.getY(), pos2.getY()) + 1);
        int z_l = (int) (min(pos1.getZ(), pos2.getZ()) - 1); int z_h = (int) (max(pos1.getZ(), pos2.getZ()) + 1);
        return new BoundingBox(x_l, y_l, z_l, x_h, y_h, z_h);
    }

    public boolean isLocationInLabyrinth(Location location) { return this.boundingBox.contains(location.toVector()); }
    public boolean isBlockASpawn(Location loc) {
        for (Spawn spawn : this.spawnHandler.getSpawns()) {
            int spawnXLoc = (int) spawn.getLocation().getX();
            int spawnZLoc = (int) spawn.getLocation().getZ();
            if (spawnXLoc == loc.getX() && spawnZLoc == loc.getZ()) return true;
        }
        return false;
    }

    public void addSpawn(Player player) { this.spawnHandler.addSpawn(player.getLocation()); }
    public void addQueue(Player player) { this.queueHandler.addQueue(player.getLocation()); }
    public void addChest(Location loc) { this.chestHandler.addChest(loc); }
    public void clearSpawns() { this.spawnHandler.clearSpawn(); }
    public void clearQueues() { this.queueHandler.clearQueue(); }
    public void clearChests() { this.chestHandler.clearChest(); }
    public void removeEntities() {
        for (Entity entity : this.world.getEntities()) {
            if (entity instanceof Player) continue;
            if (this.boundingBox.contains(entity.getLocation().toVector())) {
                entity.remove();
            }
        }
    }

    // Config
    public static Labyrinth getLabyrinthFromConfig(JavaPlugin plugin, ConfigHandler config, String labyrinthName) {
        String pathToLabyrinth = "labyrinths." + labyrinthName;
        Location pos1 = config.getLocation(pathToLabyrinth + ".bounds.0");
        Location pos2 = config.getLocation(pathToLabyrinth + ".bounds.1");

        return new Labyrinth(plugin, config, labyrinthName, pos1, pos2);
    }

    public void setLabyrinthToConfig(String pathToLabyrinths) {
        // IF labyrinths: does not exist, return
        if (!this.config.doesKeyExist(pathToLabyrinths)) return;

        // Clear existing labyrinth, if exists
        String pathToLabyrinth = pathToLabyrinths + "." + this.getName();
        this.config.setValue(pathToLabyrinth, null);

        // Save labyrinth to file
        this.config.setValue(pathToLabyrinth + ".world", this.world.getName());
        this.config.setLocation(pathToLabyrinth + ".bounds.0", this.pos1);
        this.config.setLocation(pathToLabyrinth + ".bounds.1", this.pos2);

        // Save spawns to file
        this.spawnHandler.setSpawnsToConfig(pathToLabyrinth + ".spawns");
        this.queueHandler.setQueuesToConfig(pathToLabyrinth + ".queues");
        this.chestHandler.setChestsToConfig(pathToLabyrinth + ".chests");
    }
}
