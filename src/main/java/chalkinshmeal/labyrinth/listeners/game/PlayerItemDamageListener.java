package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.game.LabyrinthEventHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerItemDamageListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final Game game;
    private final LabyrinthEventHandler labyrinthEventHandler;

    /** Constructor */
    public PlayerItemDamageListener(JavaPlugin plugin, Game game, LabyrinthEventHandler labyrinthEventHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.game = game;
        this.labyrinthEventHandler = labyrinthEventHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        this.labyrinthEventHandler.onPlayerItemDamageEvent(event);
    }
}
