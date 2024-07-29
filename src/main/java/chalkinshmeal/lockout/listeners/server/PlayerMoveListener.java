package chalkinshmeal.lockout.listeners.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import chalkinshmeal.lockout.artifacts.game.GameHandler;

public class PlayerMoveListener implements Listener {
    private final GameHandler gameHandler;

    public PlayerMoveListener(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        this.gameHandler.onPlayerMoveEvent(event);
    }
}