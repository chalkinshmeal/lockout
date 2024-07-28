package chalkinshmeal.lockout.listeners.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import chalkinshmeal.lockout.artifacts.game.GameHandler;

public class InventoryClickListener implements Listener {
    private final GameHandler gameHandler;

    public InventoryClickListener(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        this.gameHandler.onInventoryClickEvent(event);
    }
}