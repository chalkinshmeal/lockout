package chalkinshmeal.labyrinth.artifacts.items.drowned;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.isAPlayer;
import static chalkinshmeal.labyrinth.utils.Utils.isEntityATrident;

public class DrownedTrident extends LabyrinthItem {
    public DrownedTrident(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victim = event.getEntity();

        // If attacker is the player, prevent the attack
        if (isAPlayer(attacker)) {
            if (!isPlayerHoldingItemInMainHand(attacker)) return;
            event.setCancelled(true);
        }
        // If attacker is a trident, remove it after it does its damage
        else if (isEntityATrident(attacker)) {
            attacker.remove();
        }
    }

    public void onEntitySpawnEvent(EntitySpawnEvent event) {
        // Prevent tridents from spawning?
        if (isEntityATrident(event.getEntity())) event.setCancelled(true);
    }
}
