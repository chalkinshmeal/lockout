package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;

public class EntityInteractListener implements Listener {
    private final Game game;

    public EntityInteractListener(Game game) {
        this.game = game;
    }

    @EventHandler
    public void onEntityInteractEvent(EntityInteractEvent event) {
        this.game.getLabyrinthItemHandler().onEntityInteractEvent(event);
    }
}
