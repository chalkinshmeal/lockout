package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItemHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * When a block is clicked, perform any wand operations
 */
public class PlayerItemHeldListener implements Listener {
    private final JavaPlugin plugin;
    private final LabyrinthItemHandler labyrinthItemHandler;

    /** Constructor */
    public PlayerItemHeldListener(JavaPlugin plugin, LabyrinthItemHandler labyrinthItemHandler) {
        this.plugin = plugin;
        this.labyrinthItemHandler = labyrinthItemHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        this.labyrinthItemHandler.onPlayerItemHeldEvent(event);
    }
}
