package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.classes.LabyrinthClassHandler;
import chalkinshmeal.labyrinth.artifacts.game.LabyrinthEventHandler;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItemHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * When a block is clicked, perform any wand operations
 */
public class EntityDamageByEntityListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final LabyrinthEventHandler labyrinthEventHandler;
    private final LabyrinthClassHandler labyrinthClassHandler;
    private final LabyrinthItemHandler labyrinthItemHandler;

    /** Constructor */
    public EntityDamageByEntityListener(JavaPlugin plugin, LabyrinthEventHandler labyrinthEventHandler, LabyrinthClassHandler labyrinthClassHandler, LabyrinthItemHandler labyrinthItemHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.labyrinthEventHandler = labyrinthEventHandler;
        this.labyrinthClassHandler = labyrinthClassHandler;
        this.labyrinthItemHandler = labyrinthItemHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        this.labyrinthClassHandler.onEntityDamageByEntityEvent(event);
        this.labyrinthItemHandler.onEntityDamageByEntityEvent(event);
    }
}
