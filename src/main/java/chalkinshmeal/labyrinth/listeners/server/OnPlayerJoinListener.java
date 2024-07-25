package chalkinshmeal.labyrinth.listeners.server;

import chalkinshmeal.labyrinth.Plugin;
import chalkinshmeal.labyrinth.artifacts.game.GameHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * When a block is clicked, perform any wand operations
 */
public class OnPlayerJoinListener implements Listener {
    private final Plugin plugin;
    private final ConfigHandler configHandler;
    private final GameHandler gameHandler;

    /** Constructor */
    public OnPlayerJoinListener(Plugin plugin, GameHandler gameHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.gameHandler = gameHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.plugin.sendUpdate(event);
    }
}