package chalkinshmeal.lockout.listeners.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import chalkinshmeal.lockout.artifacts.compass.LockoutCompass;

public class PlayerJoinListener implements Listener {
    private final LockoutCompass lockoutCompass;

    public PlayerJoinListener(LockoutCompass lockoutCompass) {
        this.lockoutCompass = lockoutCompass;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.lockoutCompass.giveCompass(event.getPlayer());
    }
}