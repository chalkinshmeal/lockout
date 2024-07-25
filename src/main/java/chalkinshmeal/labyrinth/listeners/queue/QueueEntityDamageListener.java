package chalkinshmeal.labyrinth.listeners.queue;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * When a block is clicked, perform any wand operations
 */
public class QueueEntityDamageListener implements Listener {
    private final Game game;

    /** Constructor */
    public QueueEntityDamageListener(Game game) {
        this.game = game;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        game.preventDamageInQueue(event);
    }
}
