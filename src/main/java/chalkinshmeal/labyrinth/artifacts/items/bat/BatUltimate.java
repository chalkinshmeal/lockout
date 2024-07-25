package chalkinshmeal.labyrinth.artifacts.items.bat;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.STRENGTH;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class BatUltimate extends LabyrinthItem {
    private Map<UUID, Float> healthGained;

    public BatUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
        this.healthGained = new HashMap<>();
    }

    public void onSpawn(Player player) {
        // Initialize healthGained Map
        this.healthGained.put(player.getUniqueId(), 0F);
    }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!doesPlayerHaveUltimate(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);
        setUsedUltimate(player);

        // Buff player for a duration
        float prevMaxHealth = (float) player.getMaxHealth();
        player.setMaxHealth(20);
        giveEffect(player, SPEED, getDurationInSeconds(), 7);
        giveEffect(player, STRENGTH, getDurationInSeconds(), 1);
        startEndBatUltimateTask(this, player, getDurationInSeconds(), prevMaxHealth);

        // Cosmetics
        experienceCountdown(this.plugin, game, player, getDurationInSeconds());
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.RED + "FEED FEED FEED FEED");
    }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victim = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isLiving(victim)) return;
        if (!isUltimateActive(player)) return;

        this.healthGained.put(player.getUniqueId(), this.healthGained.get(player.getUniqueId()) + 2);
    }

    public void startEndBatUltimateTask(BatUltimate item, Player player, float duration, float maxHealth) {
        new BukkitRunnable() {
            int prevLivesLeft = item.game.getScoreboard().getLives(player);
            float secsLeft = duration;
            @Override
            public void run() {
                // End condition
                secsLeft -= 1;
                int livesLeft = item.game.getScoreboard().getLives(player);
                if (item.game.isGameDone() || livesLeft != prevLivesLeft) {
                    this.cancel();
                    return;
                }

                // End condition, if player survives
                if (secsLeft < 0) {
                    // Return player to max health, previously, plus the amount of times hit
                    float healthGained = item.healthGained.get(player.getUniqueId());
                    player.setMaxHealth(maxHealth + healthGained);
                    player.setHealth(player.getMaxHealth());

                    // Cosmetics
                    player.sendMessage(ChatColor.RED + "You have gained " + ChatColor.GREEN + healthGained / 2 + ChatColor.RED + " hearts in your feed.");
                    this.cancel();
                    return;
                }
            }
        }.runTaskTimer(this.plugin, 20L*0L, 20L*1L);
    }
}
