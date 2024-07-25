package chalkinshmeal.labyrinth.artifacts.items.enderdragon;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.AIR;

public class EnderdragonFlight extends LabyrinthItem {
    public EnderdragonFlight(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        resetItemCooldown(player);
        experienceCountdown(plugin, game, player, getDurationInSeconds());

        roarTask(this, player, 5, getDurationInSeconds());
    }

    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isItemCooldownBetween(player, getCooldownInSeconds()-getDurationInSeconds(), getCooldownInSeconds())) return;

        // Cosmetics
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 100);
        player.getWorld().spawnParticle(Particle.ENCHANT, player.getLocation(), 30);
        player.getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation(), 30);

        // Glide player
        player.setGliding(true);
        Vector playerLookDir = player.getEyeLocation().getDirection().normalize();
        player.setVelocity(playerLookDir.multiply(0.8));

        // Set nearby blocks to air, and add them to summons
        List<Block> blocks = getNonAirBlocksWithinRadius(player.getLocation(), 2);
        for (Block b : blocks) {
            if (this.game.isBlockASpawn(b.getLocation())) continue;
            this.game.addPlayerSummons(player, b, b.getType());
            b.setType(AIR);
        }

        // Fling away enemies nearby
        for (Entity e : player.getNearbyEntities(2, 2, 2)) {
            Vector v = getVectorFromPlayerToEntity(player, e);
            e.setVelocity(v.multiply(4).setY(1.5));
            if (e instanceof LivingEntity) {
                LivingEntity le = (LivingEntity) e;
                le.damage(3);
            }
        }
    }

    public void roarTask(EnderdragonFlight item, Player player, float cooldown, float duration) {
        new BukkitRunnable() {
            float secsLeft = duration;
            int livesLeft = game.getScoreboard().getLives(player);
            @Override
            public void run() {
                // End condition
                secsLeft -= cooldown;
                if (secsLeft <= 0 || !isPlayerHoldingItemInMainHand(player)) { this.cancel(); }
                if (livesLeft != game.getScoreboard().getLives(player)) { this.cancel(); }
                if (!item.isPlayerHoldingItemInMainHand(player)) { this.cancel(); }

                // Cackle
                playSound(player, Sound.ENTITY_ENDER_DRAGON_FLAP);
                playSound(player, Sound.ENTITY_ENDER_DRAGON_AMBIENT);
            }
        }.runTaskTimer(this.plugin, (long) (20L*0), (long) (20L*cooldown));
    }
}
