package chalkinshmeal.labyrinth.artifacts.items.blaze;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.Particles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.LIGHT_BLUE_DYE;

public class BlazeCampfire extends LabyrinthItem {
    public final float damage = 1F; // Amount of damage to do every interval tick
    public BlazeCampfire(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    // Activate Life Sapper
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;

        // Reset/Record state
        event.setCancelled(true);
        setItemType(player, LIGHT_BLUE_DYE);
        startLifeSapperTask(game, player, getCooldownInSeconds(), damage);
    }

    // Life Sapper Damage
    public void startLifeSapperTask(Game game, Player player, float interval, float damage) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // End condition
                if (game.isGameDone()) {
                    this.cancel();
                    return;
                }

                // Get visible player
                Entity entity = getEntityPlayerIsLookingAt(player, 10, false);
                if (entity == null) return;
                if (!isLiving(entity)) return; LivingEntity victim = (LivingEntity) entity;
                if (game.getSummonedEntities().get(player.getUniqueId()).contains(entity)) return;

                // Damage visible player
                victim.damage(damage);
                victim.setNoDamageTicks((int) Math.min(interval * 20, victim.getNoDamageTicks()));

                // Cosmetics
                Location midLoc = victim.getLocation();
                Particles.spawnParticleCircle(midLoc, Particle.SOUL_FIRE_FLAME, 0.75F);

            }
        }.runTaskTimer(this.plugin, 20L * 0L, (long) (20L * interval));
    }
}
