package chalkinshmeal.labyrinth.artifacts.items.bat;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.Particles;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class BatLaser extends LabyrinthItem {
    public final float damage = 0.4F; // Amount of damage to do every interval tick
    public final int range = 12;
    public final float interval = 0.1F;
    public BatLaser(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    // Activate Life Sapper
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Entity entity = getEntityPlayerIsLookingAt(player, range, true);
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;
        if (entity == null) return;

        // Reset/Record state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Start task
        startBatLaserTask(game, player, getCooldownInSeconds(), getDurationInSeconds(), interval, damage, this);
    }

    // Life Sapper Damage
    public void startBatLaserTask(Game game, Player player, float cooldown, float duration, float interval, float damage, BatLaser item) {
        new BukkitRunnable() {
            int livesLeft = game.getScoreboard().getLives(player);
            float secsLeft = duration;
            @Override
            public void run() {
                // End condition
                secsLeft -= interval;
                if (secsLeft < 0 || game.isGameDone() || game.getScoreboard().getLives(player) != livesLeft) {
                    this.cancel();
                    return;
                }
                if (!item.isPlayerHoldingItemInMainHand(player)) return;

                // Get visible entity
                Entity entity = getEntityPlayerIsLookingAt(player, 10, true);
                if (entity == null) return;
                if (!isLiving(entity)) return; LivingEntity victim = (LivingEntity) entity;
                if (game.getSummonedEntities().getOrDefault(player.getUniqueId(), new ArrayList<>()).contains(entity)) return;

                // Damage visible player
                victim.damage(damage);
                victim.setNoDamageTicks((int) Math.min(interval * 20, victim.getNoDamageTicks()));

                // Cosmetics
                Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1.0F);
                Particles.spawnParticleLine(player.getEyeLocation(), victim.getEyeLocation(), Color.RED, 0.2);
            }
        }.runTaskTimer(this.plugin, 20L * 0L, (long) (20L * interval));
    }
}
