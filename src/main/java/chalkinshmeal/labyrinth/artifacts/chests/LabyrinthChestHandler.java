package chalkinshmeal.labyrinth.artifacts.chests;

import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.getRandNum;

public class LabyrinthChestHandler {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final List<LabyrinthChest> chests;

    public LabyrinthChestHandler(JavaPlugin plugin, ConfigHandler config, String labyrinthName) {
        this.plugin = plugin;
        this.config = config;
        this.chests = getChestsFromConfig(this.plugin, this.config, "labyrinths." + labyrinthName + ".chests");
    }

    public void addChest(Location loc) { this.chests.add(new LabyrinthChest(plugin, config, loc)); }
    public void clearChest() { this.chests.clear(); }
    public LabyrinthChest getRandChest() { return this.chests.get(getRandNum(0, this.getChestCount()-1)); }
    public List<LabyrinthChest> getChests() { return this.chests; }
    public int getChestCount() { return this.chests.size(); }

    // Config
    public void setChestsToConfig(String pathToChests) {
        for (LabyrinthChest chest : this.getChests()) {
            int index = this.getChests().indexOf(chest);
            chest.setChestToConfig(pathToChests + "." + index);
        }
    }
    public static List<LabyrinthChest> getChestsFromConfig(JavaPlugin plugin, ConfigHandler config, String pathToChests) {
        if (!config.doesKeyExist(pathToChests)) return new ArrayList<>();

        List<LabyrinthChest> chests = new ArrayList<>();
        for (String labyrinthName : config.getKeyListFromKey(pathToChests)) {
            int index = config.getKeyListFromKey(pathToChests).indexOf(labyrinthName);
            chests.add(LabyrinthChest.getChestFromConfig(plugin, config,pathToChests + "." + index));
        }
        return chests;
    }
}
