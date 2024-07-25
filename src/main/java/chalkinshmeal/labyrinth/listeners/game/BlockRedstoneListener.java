package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockRedstoneListener implements Listener {
    private final Game game;

    /** Constructor */
    public BlockRedstoneListener(Game game) {
        this.game = game;
    }

    /** Event Handler */
    @EventHandler
    public void onBlockRedstoneEvent(BlockRedstoneEvent event) {
        this.game.getLabyrinthItemHandler().onBlockRedstoneEvent(event);
    }
}
