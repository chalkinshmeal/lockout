package chalkinshmeal.labyrinth.artifacts.server;

import org.bukkit.event.entity.CreatureSpawnEvent;

public class ServerEventHandler {
    public void preventNaturalMobSpawn(CreatureSpawnEvent event) {
        if (!(event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL))) return;
        event.setCancelled(true);
    }
}
