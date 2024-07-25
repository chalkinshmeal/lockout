package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.classes.LabyrinthClassHandler;
import chalkinshmeal.labyrinth.artifacts.game.LabyrinthEventHandler;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItemHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockPlaceListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final LabyrinthEventHandler labyrinthEventHandler;
    private final LabyrinthClassHandler labyrinthClassHandler;
    private final LabyrinthItemHandler labyrinthItemHandler;

    /** Constructor */
    public BlockPlaceListener(JavaPlugin plugin, LabyrinthEventHandler labyrinthEventHandler, LabyrinthClassHandler labyrinthClassHandler, LabyrinthItemHandler labyrinthItemHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.labyrinthEventHandler = labyrinthEventHandler;
        this.labyrinthClassHandler = labyrinthClassHandler;
        this.labyrinthItemHandler = labyrinthItemHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        this.labyrinthItemHandler.onBlockPlaceEvent(event);
        this.labyrinthEventHandler.onBlockPlaceEvent(event);
    }
}
