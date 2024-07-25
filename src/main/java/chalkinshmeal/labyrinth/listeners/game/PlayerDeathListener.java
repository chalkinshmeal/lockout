package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.classes.LabyrinthClassHandler;
import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.game.LabyrinthEventHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * When a block is clicked, perform any wand operations
 */
public class PlayerDeathListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final Game game;
    private final LabyrinthEventHandler labyrinthEventHandler;
    private final LabyrinthClassHandler labyrinthClassHandler;

    /** Constructor */
    public PlayerDeathListener(JavaPlugin plugin, Game game, LabyrinthEventHandler labyrinthEventHandler, LabyrinthClassHandler labyrinthClassHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.game = game;
        this.labyrinthEventHandler = labyrinthEventHandler;
        this.labyrinthClassHandler = labyrinthClassHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        this.labyrinthClassHandler.onPlayerDeathEvent(event);
    }
}
