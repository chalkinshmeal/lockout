package chalkinshmeal.lockout.listeners.game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import chalkinshmeal.lockout.artifacts.game.LabyrinthEventHandler;

public class BlockBreakListener implements Listener {
    private final LabyrinthEventHandler labyrinthEventHandler;

    /** Constructor */
    public BlockBreakListener(LabyrinthEventHandler labyrinthEventHandler) {
        this.labyrinthEventHandler = labyrinthEventHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onBlockDestroy(BlockBreakEvent event) {
//        this.labyrinthEventHandler.onBlockBreakEvent(event);
    }
}