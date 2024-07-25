package chalkinshmeal.labyrinth.artifacts.classes;

import chalkinshmeal.labyrinth.artifacts.cooldown.CooldownHandler;
import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItemHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static chalkinshmeal.labyrinth.utils.Utils.getRandNum;
import static chalkinshmeal.labyrinth.utils.Utils.isAPlayer;

/**
 * LabyrinthClassHandler
 * Handles all Labyrinth related events
 * General events related to Labyrinth players (double jump, death handling) should be handled in this parent class
 * More specific class events should be handled with specific instantiations of children of this class
 */
public class LabyrinthClassHandler {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final Game game;
    private final CooldownHandler cooldownHandler;
    private LabyrinthItemHandler labyrinthItemHandler;
    private Map<String, LabyrinthClass> classes;
    private boolean isItemModeEnabled;

    public LabyrinthClassHandler(JavaPlugin plugin, Game game, CooldownHandler cooldownHandler, LabyrinthItemHandler labyrinthItemHandler) {
        this.plugin = plugin;
        this.config = new ConfigHandler(plugin, "plugins/labyrinth/classes.yml");
        this.game = game;
        this.cooldownHandler = cooldownHandler;
        this.labyrinthItemHandler = labyrinthItemHandler;
        if (this.labyrinthItemHandler == null) this.labyrinthItemHandler = new LabyrinthItemHandler(plugin, game);
        this.classes = new HashMap<>();
        this.isItemModeEnabled = false;

        getClassesFromConfig("classes");
    }
    // -------------------------------------------------------------------------------------------
    // Commands
    // -------------------------------------------------------------------------------------------
    public void giveKit(Player player) { this.classes.get(this.getClassNameByPlayer(player)).giveKit(player, this.isItemModeEnabled); }
    public void giveClassDebug(Player player, String className) {
        className = ChatColor.stripColor(className.strip().toLowerCase().replace(' ', '_'));
        getClassesFromConfig("classes");
        LabyrinthClass labyrinthClass = this.classes.get(className);
        if (labyrinthClass == null) { player.sendMessage(ChatColor.RED + "Could not find class '" + ChatColor.GOLD + className + ChatColor.RED + "'"); return; }
        this.classes.get(className).giveKit(player, this.isItemModeEnabled);
    }

    // -------------------------------------------------------------------------------------------
    // Manipulators
    // -------------------------------------------------------------------------------------------
    public LabyrinthItemHandler getLabyrinthItemHandler() { return this.labyrinthItemHandler; }
    public String getClassNameByPlayer(Player player) { return this.game.getClassSelections().getOrDefault(player.getUniqueId(), "general"); }
    public LabyrinthClass getClassByName(String className) {
        if (className == null) return null;
        className = ChatColor.stripColor(className.strip().toLowerCase().replace(' ', '_'));
        return this.classes.get(className);
    }
    public String getRandClassName() {
        int randNum = getRandNum(0, this.classes.size() - 1);
        return (String) this.classes.keySet().toArray()[randNum];
    }
    public LabyrinthClass getClassByPlayer(Player player) { return this.getClassByName(this.getClassNameByPlayer(player)); }
    public List<String> getClassNames() {
        if (this.classes.isEmpty()) getClassesFromConfig("classes");
        return new ArrayList<>(this.classes.keySet());
    }
    public boolean isItemModeEnabled() { return this.isItemModeEnabled; }
    public void toggleIsItemModeEnabled() { this.isItemModeEnabled = !this.isItemModeEnabled; }

    // -------------------------------------------------------------------------------------------
    // Iterators for all classes' event listeners
    // -------------------------------------------------------------------------------------------
    //public void onBlockPlaceEvent(BlockPlaceEvent event) { for (LabyrinthClass c : this.classes) c.onBlockPlaceEvent(event); }
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (!isAPlayer(event.getEntity())) return;
        Player player = (Player) event.getEntity();
        this.getClassByPlayer(player).onEntityDamageEvent(event);
    }
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!isAPlayer(event.getEntity())) return;
        Player player = (Player) event.getEntity();
        this.getClassByPlayer(player).onEntityDamageByEntityEvent(event);
    }
    public void onEntityRegainHealthEvent(EntityRegainHealthEvent event) {
        if (!isAPlayer(event.getEntity())) return;
        Player player = (Player) event.getEntity();
        LabyrinthClass labyrinthClass = this.getClassByPlayer(player);
        if (labyrinthClass == null) return;
        labyrinthClass.onEntityRegainHealthEvent(event);
    }
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        this.getClassByPlayer(player).onPlayerDeathEvent(event);
    }
    public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        this.getClassByPlayer(player).onPlayerToggleFlightEvent(event);
    }
    //public void onPlayerInteractEvent(PlayerInteractEvent event) { for (LabyrinthClass c : this.classes) c.onPlayerInteractEvent(event); }
    //public void onPlayerMoveEvent(PlayerMoveEvent event) { for (LabyrinthClass c : this.classes) c.onPlayerMoveEvent(event); }

    // -------------------------------------------------------------------------------------------
    // Config
    // -------------------------------------------------------------------------------------------
    public void getClassesFromConfig(String pathToClasses) {
        if (!config.doesKeyExist(pathToClasses)) return;

        Map<String, LabyrinthClass> classes = new HashMap<>();
        for (String className : config.getKeyListFromKey(pathToClasses)) {
            switch (className) {
                case "wither": classes.put(className, new Wither(plugin, className, this.game, this.labyrinthItemHandler)); break;
                default: classes.put(className, new LabyrinthClass(plugin, className, this.game, this.labyrinthItemHandler));
            }
        }
        this.classes = classes;
    }
}