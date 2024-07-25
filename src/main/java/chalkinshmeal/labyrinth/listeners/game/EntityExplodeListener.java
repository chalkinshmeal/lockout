package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.game.LabyrinthEventHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityExplodeListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final LabyrinthEventHandler labyrinthEventHandler;

    /** Constructor */
    public EntityExplodeListener(JavaPlugin plugin, LabyrinthEventHandler labyrinthEventHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.labyrinthEventHandler = labyrinthEventHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        this.labyrinthEventHandler.onEntityExplodeEvent(event);
    }
}
