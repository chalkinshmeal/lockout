package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.classes.LabyrinthClassHandler;
import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.game.LabyrinthEventHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * When a block is clicked, perform any wand operations
 */
public class EntityDamageListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final LabyrinthEventHandler labyrinthEventHandler;
    private final LabyrinthClassHandler labyrinthClassHandler;
    private final Game game;

    /** Constructor */
    public EntityDamageListener(JavaPlugin plugin, Game game, LabyrinthEventHandler labyrinthEventHandler, LabyrinthClassHandler labyrinthClassHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.labyrinthEventHandler = labyrinthEventHandler;
        this.labyrinthClassHandler = labyrinthClassHandler;
        this.game = game;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        this.labyrinthClassHandler.onEntityDamageEvent(event);
        this.labyrinthEventHandler.onEntityDamageEvent(event);
    }
}
