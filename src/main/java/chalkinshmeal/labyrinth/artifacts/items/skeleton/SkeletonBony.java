package chalkinshmeal.labyrinth.artifacts.items.skeleton;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class SkeletonBony extends LabyrinthItem {
    private final int chance = 3; // 1/3 chance to evade
    public SkeletonBony(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onProjectileHitEvent(ProjectileHitEvent event) {
        Entity hitEntity = event.getHitEntity(); ProjectileSource src = event.getEntity().getShooter();
        if (!isAPlayer(hitEntity)) return; Player hitPlayer = (Player) hitEntity;
        if (!isPlayerHoldingItemInInventory(hitPlayer)) return;
        if (getRandNum(0, chance-1) != 0) return;

        // Cosmetics
        hitPlayer.sendMessage(ChatColor.LIGHT_PURPLE + "You evaded an attack!");
        if (isProjectileSourceAPlayer(src))
            ((Player) src).sendMessage(ChatColor.GOLD + hitPlayer.getName() + " " + ChatColor.LIGHT_PURPLE + "evaded your attack!");

        // Evade
        event.setCancelled(true);
    }
}
