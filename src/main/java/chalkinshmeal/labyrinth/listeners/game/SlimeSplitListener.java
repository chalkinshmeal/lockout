package chalkinshmeal.labyrinth.listeners.game;

import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItemHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;

public class SlimeSplitListener implements Listener {
    private final LabyrinthItemHandler labyrinthItemHandler;

    /** Constructor */
    public SlimeSplitListener(LabyrinthItemHandler labyrinthItemHandler) {
        this.labyrinthItemHandler = labyrinthItemHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent event) {
        this.labyrinthItemHandler.onSlimeSplitEvent(event);
    }
}
