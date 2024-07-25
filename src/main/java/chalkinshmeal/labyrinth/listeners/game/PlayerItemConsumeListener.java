package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.game.LabyrinthEventHandler;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItemHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * When a block is clicked, perform any wand operations
 */
public class PlayerItemConsumeListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final LabyrinthEventHandler labyrinthEventHandler;
    private final LabyrinthItemHandler labyrinthItemHandler;

    /** Constructor */
    public PlayerItemConsumeListener(JavaPlugin plugin, LabyrinthEventHandler labyrinthEventHandler, LabyrinthItemHandler labyrinthItemHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.labyrinthEventHandler = labyrinthEventHandler;
        this.labyrinthItemHandler = labyrinthItemHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        this.labyrinthItemHandler.onPlayerItemConsumeEvent(event);
    }
}
