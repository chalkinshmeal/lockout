package chalkinshmeal.labyrinth.artifacts.classes;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItemHandler;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Wither extends LabyrinthClass {
    Wither(JavaPlugin plugin, String className, Game game, LabyrinthItemHandler labyrinthItemHandler) { super(plugin, className, game, labyrinthItemHandler); }

    // -------------------------------------------------------------------------------------------
    // LabyrinthClass overrides
    // -------------------------------------------------------------------------------------------
    public void doubleJump(PlayerToggleFlightEvent event) {
        if (game.isUltimateActive(event.getPlayer())) return;
        super.doubleJump(event);
    }
}
