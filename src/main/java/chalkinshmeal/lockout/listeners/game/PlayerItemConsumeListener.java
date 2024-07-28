package chalkinshmeal.lockout.listeners.game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import chalkinshmeal.lockout.artifacts.game.GameHandler;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTaskHandler;

public class PlayerItemConsumeListener implements Listener {
    private final LockoutTaskHandler lockoutTaskHandler;

    public PlayerItemConsumeListener(LockoutTaskHandler lockoutTaskHandler) {
        this.lockoutTaskHandler = lockoutTaskHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        this.lockoutTaskHandler.onPlayerItemConsumeEvent(event);
    }
}