package chalkinshmeal.labyrinth.artifacts.labyrinth;

import chalkinshmeal.labyrinth.cmdframework.handler.CommandHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.getRandNum;

public class LabyrinthHandler {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final CommandHandler cmdHandler;
    public List<Labyrinth> labyrinths;

    public LabyrinthHandler(JavaPlugin plugin, ConfigHandler config, CommandHandler cmdHandler) {
        this.plugin = plugin;
        this.config = config;
        this.cmdHandler = cmdHandler;
        this.labyrinths = null;
    }

    // Getters
    public List<Labyrinth> getLabyrinthsSortedByName() {
        List<Labyrinth> labyrinths = new ArrayList<>();
        List<String> labyrinthNames = this.getLabyrinthNames();
        Collections.sort(labyrinthNames);
        for (String labyrinthName : labyrinthNames) {
            labyrinths.add(this.getLabyrinthByName(labyrinthName));
        }
        return labyrinths;
    }
    public List<String> getLabyrinthNames() {
        List<String> labyrinthNames = new ArrayList<>();
        for (Labyrinth a : this.labyrinths) { labyrinthNames.add(a.getName()); }
        return labyrinthNames;
    }
    public int getNumLabyrinths() { return this.labyrinths.size(); }
    public Labyrinth getLabyrinthByName(String labyrinthName) {
        if (labyrinthName.equalsIgnoreCase("random")) {
            int randNum = getRandNum(0, this.getNumLabyrinths() - 1);
            return this.labyrinths.get(randNum);
        }
        for (Labyrinth a : this.labyrinths) { if (a.getName().equalsIgnoreCase(labyrinthName)) return a; }
        return null;
    }

    public void addLabyrinth(Player player, String labyrinthName, Location pos1, Location pos2) {
        if (this.getLabyrinthByName(labyrinthName) != null) {
            player.sendMessage(ChatColor.RED + "Labyrinth '" + labyrinthName + "' already exists!");
            return;
        }
        this.labyrinths.add(new Labyrinth(this.plugin, this.config, labyrinthName, pos1, pos2));
        player.sendMessage(ChatColor.GREEN + "Labyrinth '" + labyrinthName + "' has been added!");
        this.setLabyrinthsToConfig();
        this.cmdHandler.setTabList("remove-labyrinth", "name", this.getLabyrinthNames());
    }
    public void addSpawn(Player player, String labyrinthName) {
        Labyrinth labyrinth = this.getLabyrinthByName(labyrinthName);
        if (labyrinth == null) return;

        labyrinth.addSpawn(player);
        player.sendMessage(ChatColor.GREEN + "Spawnpoint #" + labyrinth.getSpawnCount() + " for labyrinth '" + labyrinthName + "' has been added!");
        this.setLabyrinthsToConfig();
    }
    public void addQueue(Player player, String labyrinthName) {
        Labyrinth labyrinth = this.getLabyrinthByName(labyrinthName);
        if (labyrinth == null) return;

        labyrinth.addQueue(player);
        player.sendMessage(ChatColor.GREEN + "Queue #" + labyrinth.getQueueCount() + " for labyrinth '" + labyrinthName + "' has been added!");
        this.setLabyrinthsToConfig();
    }
    public void addChest(Player player, Location loc, String labyrinthName) {
        Labyrinth labyrinth = this.getLabyrinthByName(labyrinthName);
        if (labyrinth == null) return;

        labyrinth.addChest(loc);
        player.sendMessage(ChatColor.GREEN + "Chest #" + labyrinth.getChestCount() + " for labyrinth '" + labyrinthName + "' has been added!");
        this.setLabyrinthsToConfig();
    }

    public void clearSpawns(Player player, String labyrinthName) {
        Labyrinth labyrinth = this.getLabyrinthByName(labyrinthName);
        if (labyrinth == null) return;
        labyrinth.clearSpawns();

        player.sendMessage(ChatColor.GREEN + "Spawnpoints for labyrinth' " + labyrinthName + "' have been cleared!");
        this.setLabyrinthsToConfig();
    }
    public void clearQueues(Player player, String labyrinthName) {
        Labyrinth labyrinth = this.getLabyrinthByName(labyrinthName);
        if (labyrinth == null) return;
        labyrinth.clearQueues();

        player.sendMessage(ChatColor.GREEN + "Queues for labyrinth' " + labyrinthName + "' have been cleared!");
        this.setLabyrinthsToConfig();
    }
    public void clearChests(Player player, String labyrinthName) {
        Labyrinth labyrinth = this.getLabyrinthByName(labyrinthName);
        if (labyrinth == null) return;
        labyrinth.clearChests();

        player.sendMessage(ChatColor.GREEN + "Chests for labyrinth' " + labyrinthName + "' have been cleared!");
        this.setLabyrinthsToConfig();
    }
    public void removeLabyrinth(Player player, String labyrinthName) {
        Labyrinth labyrinth = this.getLabyrinthByName(labyrinthName);
        if (labyrinth == null) {
            player.sendMessage(ChatColor.RED + "Labyrinth '" + labyrinthName + "' is not a valid labyrinth");
            return;
        }

        this.labyrinths.remove(labyrinth);

        player.sendMessage(ChatColor.GREEN + "Labyrinth '" + labyrinthName + "' has been removed!");
        this.setLabyrinthsToConfig();
        this.cmdHandler.setTabList("add-labyrinth", "name", this.getLabyrinthNames());
    }

    // Config
    public static List<Labyrinth> getLabyrinthsFromConfig(JavaPlugin plugin, ConfigHandler config, String pathToLabyrinths) {
        if (!config.doesKeyExist(pathToLabyrinths)) return new ArrayList<>();

        List<Labyrinth> labyrinths = new ArrayList<>();
        for (String labyrinthName : config.getKeyListFromKey(pathToLabyrinths)) {
            labyrinths.add(Labyrinth.getLabyrinthFromConfig(plugin, config, labyrinthName));
        }
        return labyrinths;
    }

    public void setLabyrinthsToConfig() {
        this.config.setValue("labyrinths", "");
        for (Labyrinth a : this.labyrinths) { a.setLabyrinthToConfig("labyrinths"); }
    }
}
