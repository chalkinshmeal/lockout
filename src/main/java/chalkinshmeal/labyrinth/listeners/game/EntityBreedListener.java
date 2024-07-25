package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItemHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityBreedListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final LabyrinthItemHandler labyrinthItemHandler;

    /** Constructor */
    public EntityBreedListener(JavaPlugin plugin, LabyrinthItemHandler labyrinthItemHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.labyrinthItemHandler = labyrinthItemHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onEntityBreedListener(EntityBreedEvent event) {
        this.labyrinthItemHandler.onEntityBreedEvent(event);
    }
}
