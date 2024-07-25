package chalkinshmeal.labyrinth.artifacts.items.allay;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.Particles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static chalkinshmeal.labyrinth.utils.Utils.getEntityPlayerIsLookingAt;
import static chalkinshmeal.labyrinth.utils.Utils.isLiving;

public class AllayLifeSapper extends LabyrinthItem {
    public final float damage = 1F; // Amount of damage to do every interval tick
    public AllayLifeSapper(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    // On spawn, activate life sapper
    public void onSpawn(Player player) {
        if (!isPlayerHoldingItemInInventory(player)) return;
        startLifeSapperTask(game, player, getCooldownInSeconds(), damage, this);
    }

    // Life Sapper Damage
    public void startLifeSapperTask(Game game, Player player, float interval, float damage, AllayLifeSapper item) {
        new BukkitRunnable() {
            final int livesLeft = game.getScoreboard().getLives(player);
            @Override
            public void run() {
                // End condition
                if (game.isGameDone() || game.getScoreboard().getLives(player) != livesLeft) {
                    this.cancel();
                    return;
                }

                if (!item.isPlayerHoldingItemInMainHand(player)) return;

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