package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.game.LabyrinthEventHandler;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItemHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityShootBowListener implements Listener {
    private final LabyrinthEventHandler labyrinthEventHandler;
    private final LabyrinthItemHandler labyrinthItemHandler;

    /** Constructor */
    public EntityShootBowListener(LabyrinthEventHandler labyrinthEventHandler, LabyrinthItemHandler labyrinthItemHandler) {
        this.labyrinthEventHandler = labyrinthEventHandler;
        this.labyrinthItemHandler = labyrinthItemHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onEntityShootBowEvent(EntityShootBowEvent event) {
        this.labyrinthItemHandler.onEntityShootBowEvent(event);
    }
}
