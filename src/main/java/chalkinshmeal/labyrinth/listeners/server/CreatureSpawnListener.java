package chalkinshmeal.labyrinth.listeners.server;

import chalkinshmeal.labyrinth.artifacts.server.ServerEventHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CreatureSpawnListener implements Listener {
    private final ServerEventHandler serverEventHandler;

    /** Constructor */
    public CreatureSpawnListener(ServerEventHandler serverEventHandler) {
        this.serverEventHandler = serverEventHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        this.serverEventHandler.preventNaturalMobSpawn(event);
    }
}
