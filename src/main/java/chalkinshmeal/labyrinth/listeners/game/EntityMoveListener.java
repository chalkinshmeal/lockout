package chalkinshmeal.labyrinth.listeners.game;

import io.papermc.paper.event.entity.EntityMoveEvent;
import chalkinshmeal.labyrinth.artifacts.cooldown.CooldownHandler;
import chalkinshmeal.labyrinth.artifacts.game.LabyrinthEventHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * When a block is clicked, perform any wand operations
 */
public class EntityMoveListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final LabyrinthEventHandler labyrinthEventHandler;
    private final CooldownHandler cooldownHandler;

    /** Constructor */
    public EntityMoveListener(JavaPlugin plugin, LabyrinthEventHandler labyrinthEventHandler, CooldownHandler cooldownHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.labyrinthEventHandler = labyrinthEventHandler;
        this.cooldownHandler = cooldownHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
    }
}
