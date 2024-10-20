package chalkinshmeal.lockout.listeners.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import chalkinshmeal.lockout.artifacts.compass.LockoutCompass;
import chalkinshmeal.lockout.artifacts.scoreboard.LockoutScoreboard;

public class PlayerJoinListener implements Listener {
    private final LockoutCompass lockoutCompass;
    private final LockoutScoreboard lockoutScoreboard;

    public PlayerJoinListener(LockoutCompass lockoutCompass, LockoutScoreboard lockoutScoreboard) {
        this.lockoutCompass = lockoutCompass;
        this.lockoutScoreboard = lockoutScoreboard;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.lockoutCompass.giveCompass(event.getPlayer());
        this.lockoutScoreboard.showToPlayer(event.getPlayer());
    }
}