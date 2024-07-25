package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.classes.LabyrinthClassHandler;
import chalkinshmeal.labyrinth.artifacts.game.LabyrinthEventHandler;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItemHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * When a block is clicked, perform any wand operations
 */
public class PlayerInteractListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final LabyrinthEventHandler labyrinthEventHandler;
    private final LabyrinthClassHandler labyrinthClassHandler;
    private final LabyrinthItemHandler labyrinthItemHandler;

    /** Constructor */
    public PlayerInteractListener(JavaPlugin plugin, LabyrinthEventHandler labyrinthEventHandler, LabyrinthClassHandler labyrinthClassHandler, LabyrinthItemHandler labyrinthItemHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.labyrinthEventHandler = labyrinthEventHandler;
        this.labyrinthClassHandler = labyrinthClassHandler;
        this.labyrinthItemHandler = labyrinthItemHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        this.labyrinthEventHandler.onPlayerInteractEvent(event);
        this.labyrinthItemHandler.onPlayerInteractEvent(event);
    }
}
