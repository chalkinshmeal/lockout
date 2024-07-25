package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.classes.LabyrinthClassHandler;
import chalkinshmeal.labyrinth.artifacts.game.LabyrinthEventHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityRegainHealthListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final LabyrinthEventHandler labyrinthEventHandler;
    private final LabyrinthClassHandler labyrinthClassHandler;

    /** Constructor */
    public EntityRegainHealthListener(JavaPlugin plugin, LabyrinthEventHandler labyrinthEventHandler, LabyrinthClassHandler labyrinthClassHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.labyrinthEventHandler = labyrinthEventHandler;
        this.labyrinthClassHandler = labyrinthClassHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        this.labyrinthClassHandler.onEntityRegainHealthEvent(event);
        //this.labyrinthEventHandler.onEntityRegainHealthEvent(event);
    }
}
