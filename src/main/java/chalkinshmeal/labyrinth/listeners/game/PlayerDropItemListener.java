package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.game.LabyrinthEventHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * When a block is clicked, perform any wand operations
 */
public class PlayerDropItemListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final LabyrinthEventHandler labyrinthEventHandler;

    /** Constructor */
    public PlayerDropItemListener(JavaPlugin plugin, LabyrinthEventHandler labyrinthEventHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.labyrinthEventHandler = labyrinthEventHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
    }
}
