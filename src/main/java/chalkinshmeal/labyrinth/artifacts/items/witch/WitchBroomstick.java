package chalkinshmeal.labyrinth.artifacts.items.witch;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static chalkinshmeal.labyrinth.utils.Utils.playSound;
import static chalkinshmeal.labyrinth.utils.Utils.spawnPotion;
import static org.bukkit.Material.AIR;
import static org.bukkit.potion.PotionEffectType.INSTANT_DAMAGE;

public class WitchBroomstick extends LabyrinthItem {
    private final float dropPotionCooldown = 0.5F;
    public WitchBroomstick(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Start drop potion task
        dropPotionTask(game, player, dropPotionCooldown, (int) getDurationInSeconds());
        cackleTask(this, player, 1, getDurationInSeconds());
    }

    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isItemCooldownBetween(player, getCooldownInSeconds()-getDurationInSeconds(), getCooldownInSeconds())) return;

        // Cosmetics
        player.getWorld().spawnParticle(Particle.WHITE_ASH, player.getLocation(), 30);

        // Glide player
        player.setGliding(true);
        Vector playerLookDir = player.getEyeLocation().getDirection().normalize();
        player.setVelocity(playerLookDir.multiply(0.5));
    }

    public void dropPotionTask(Game game, Player player, float cooldown, int duration) {
        new BukkitRunnable() {
            float secsLeft = duration - cooldown;
            int livesLeft = game.getScoreboard().getLives(player);
            @Override
            public void run() {
                // End condition
                secsLeft -= cooldown;
                if (secsLeft <= 0 || !isPlayerHoldingItemInMainHand(player)) { this.cancel(); }
                if (livesLeft != game.getScoreboard().getLives(player)) { this.cancel(); }
                if (!isItemCooldownBetween(player, getCooldownInSeconds()-getDurationInSeconds(), getCooldownInSeconds())) { this.cancel(); }

                // Get location to spawn potion
                Location spawnPotionLoc = player.getLocation().subtract(0, 1, 0);
                if (!spawnPotionLoc.getBlock().getType().equals(AIR)) return;

                // Spawn splash potion at that location
                spawnPotion(spawnPotionLoc, INSTANT_DAMAGE, 1, 2, Color.PURPLE);
            }
        }.runTaskTimer(this.plugin, (long) (20L*cooldown), (long) (20L*cooldown));
    }

    public void cackleTask(WitchBroomstick item, Player player, float cooldown, float duration) {
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
                playSound(player, Sound.ENTITY_WITCH_CELEBRATE);
            }
        }.runTaskTimer(this.plugin, (long) (20L*0), (long) (20L*cooldown));
    }
}