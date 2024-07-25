package chalkinshmeal.labyrinth.artifacts.items.wither;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import static chalkinshmeal.labyrinth.utils.Utils.isAPlayer;

public class WitherDeflect extends LabyrinthItem {
    public WitherDeflect(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onProjectileHitEvent(ProjectileHitEvent event) {
        Entity hitEntity = event.getHitEntity(); ProjectileSource src = event.getEntity().getShooter();
        if (!isAPlayer(hitEntity)) return; Player hitPlayer = (Player) hitEntity;
        if (!isPlayerHoldingItemInMainHand(hitPlayer)) return;

        // Evade
        event.setCancelled(true);
    }
}
