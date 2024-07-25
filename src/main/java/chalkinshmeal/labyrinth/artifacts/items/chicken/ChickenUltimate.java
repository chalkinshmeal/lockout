package chalkinshmeal.labyrinth.artifacts.items.chicken;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class ChickenUltimate extends LabyrinthItem {
    public ChickenUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!doesPlayerHaveUltimate(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);
        setUsedUltimate(player);
        experienceCountdown(plugin, game, player, getDurationInSeconds());

        // Cosmetics
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.RED + "The chicken is not so scared anymore...");

        // Start chicken throwing task
        chickenUltimateTask(this.game, player, (int) getCooldownInSeconds(), (int) getDurationInSeconds());
    }

    public void chickenUltimateTask(Game game, Player player, int cooldown, int duration) {
        new BukkitRunnable() {
            float secsLeft = duration;
            @Override
            public void run() {
                // Ultimate properties
                player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 1);
                player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, player.getLocation(), 30);
                for (Entity e : player.getNearbyEntities(5, 5, 5)) {
                    Vector v = getVectorFromPlayerToEntity(player, e);
                    e.setVelocity(v.multiply(4).setY(1.5));
                    if (e instanceof LivingEntity) {
                        LivingEntity le = (LivingEntity) e;
                        le.damage(3);
                    }
                }

                // End condition
                secsLeft -= 1;
                if (secsLeft < 0 || !game.isUltimateActive(player)) { this.cancel(); }
            }
        }.runTaskTimer(this.plugin, 20L*0L, 20L*cooldown);
    }

}
