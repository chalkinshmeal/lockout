package chalkinshmeal.labyrinth.artifacts.items.snow_golem;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.SNOWBALL;

public class SnowGolemSnowballs extends LabyrinthItem {
    public SnowGolemSnowballs(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onProjectileHitEvent(ProjectileHitEvent event) {
        ProjectileSource shooter = event.getEntity().getShooter();
        Projectile projectile = event.getEntity();
        Block hitBlock = event.getHitBlock(); Entity hitEntityDeadOrAlive = event.getHitEntity();
        if (!isShooterAPlayer(shooter)) return; Player player = (Player) shooter;
        if (!isProjectileAnItem(projectile, SNOWBALL)) return;
        if (!isLiving(hitEntityDeadOrAlive)) return; LivingEntity hitEntity = (LivingEntity) hitEntityDeadOrAlive;
        if (hitEntity == null) return;

        // Reset/Record state
        event.setCancelled(true);

        // Freeze
        subtractHealth(hitEntity, 1);
        addFreeze(hitEntity, 3);
        if (hitEntity.getFreezeTicks() == hitEntity.getMaxFreezeTicks() && !hitEntity.isFreezeTickingLocked()) {
            startGolemHitFreezeTask(this.game, hitEntity, false);
            if (hitEntity instanceof Player)
                hitEntity.sendMessage(ChatColor.AQUA + "You feel... cold...");
            player.sendMessage(ChatColor.AQUA + "You opponent is freezing!");
        }
    }

    public void startGolemHitFreezeTask(Game game, LivingEntity ent, boolean isUltimateActive) {
        new BukkitRunnable() {
            float freezeHitsLeft = 4;

            @Override
            public void run() {
                // Ultimate properties
                ent.lockFreezeTicks(true);
                if (isUltimateActive) ent.damage(3);
                else ent.damage(1);

                // End condition
                freezeHitsLeft -= 1;
                if (freezeHitsLeft < 0) {
                    ent.lockFreezeTicks(false);
                    this.cancel();
                }
            }
        }.runTaskTimer(this.plugin, 20L * 0L, 20L * 2L);
    }
}
