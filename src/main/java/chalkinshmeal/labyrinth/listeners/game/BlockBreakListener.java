package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.game.LabyrinthEventHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    private final LabyrinthEventHandler labyrinthEventHandler;

    /** Constructor */
    public BlockBreakListener(LabyrinthEventHandler labyrinthEventHandler) {
        this.labyrinthEventHandler = labyrinthEventHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onBlockDestroy(BlockBreakEvent event) {
        this.labyrinthEventHandler.onBlockBreakEvent(event);
    }
}