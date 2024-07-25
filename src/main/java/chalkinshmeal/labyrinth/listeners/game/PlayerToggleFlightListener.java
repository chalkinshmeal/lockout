package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.classes.LabyrinthClassHandler;
import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerToggleFlightListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final Game game;
    private final LabyrinthClassHandler labyrinthClassHandler;

    /** Constructor */
    public PlayerToggleFlightListener(JavaPlugin plugin, Game game, LabyrinthClassHandler labyrinthClassHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.game = game;
        this.labyrinthClassHandler = labyrinthClassHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        this.labyrinthClassHandler.onPlayerToggleFlightEvent(event);
    }
}
