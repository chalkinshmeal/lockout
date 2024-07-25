package chalkinshmeal.labyrinth.artifacts.items.blaze;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.Particles;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class BlazeUltimate extends LabyrinthItem {
    public final float damage = 1F;
    public final float range = 5F;
    public BlazeUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!doesPlayerHaveUltimate(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);
        setUsedUltimate(player);

        // Cosmetics
        experienceCountdown(this.plugin, game, player, getDurationInSeconds());
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.GOLD + "Let them burn...");

        // Ultimate effect - Burn players
        startHellfireTask(game, player, getDurationInSeconds(), damage, range, getCooldownInSeconds());
    }

    // Life Sapper Damage
    public void startHellfireTask(Game game, Player player, float duration, float damage, float range, float interval) {
        new BukkitRunnable() {
            float secsLeft = duration;
            int livesLeft = game.getScoreboard().getLives(player);
            @Override
            public void run() {
                // Game end condition
                if (game.isGameDone() || game.getScoreboard().getLives(player) != livesLeft || secsLeft <= 0) {
                    this.cancel();
                    return;
                }

                secsLeft -= (1F/20F);

                // Get nearby entities
                for (Entity entity : player.getNearbyEntities(range, range, range)) {
                    if (!(entity instanceof LivingEntity)) return; LivingEntity victim = (LivingEntity) entity;
                    if (victim.getNoDamageTicks() != 0) return;

                    victim.damage(damage);
                    victim.setFireTicks((int) (20L * interval));
                    victim.setNoDamageTicks((int) (20L * interval));
                }

                // Cosmetics
                Particles.spawnParticleCircle(player.getEyeLocation(), Particle.FALLING_LAVA, range);
            }
        }.runTaskTimer(this.plugin, 20L * 0L, (long) (1));
    }
}
