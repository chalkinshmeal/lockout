package chalkinshmeal.labyrinth.artifacts.items.pig;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class PigUltimate extends LabyrinthItem {
    private List<UUID> ultingPlayers;
    public PigUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
        this.ultingPlayers = new ArrayList<>();
    }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        removePassengers(event);

        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!doesPlayerHaveUltimate(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);
        setUsedUltimate(player);
        experienceCountdown(plugin, game, player, getDurationInSeconds());
        setUltimateDone(plugin, game, player, getDurationInSeconds());
        this.ultingPlayers.add(player.getUniqueId());

        // Cosmetics
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.RED + "It's Pigstep time...");
        player.sendMessage(ChatColor.DARK_GRAY + "(You can pick people up and throw them now)");
    }

    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer(); Entity entity = event.getRightClicked();
        if (!isMainHand(event)) return;
        if (player == null) return;
        if (!isUltimateActive(player)) return;
        if (!(isLiving(entity))) return;
        LivingEntity livingEntity = (LivingEntity) entity;
        if (!(this.ultingPlayers.contains(player.getUniqueId()))) return;

        // Reset/Record state
        event.setCancelled(true);

        // Add entity as passenger
        if (player.getPassengers().isEmpty()) {
            player.addPassenger(livingEntity);
        }
    }

    public void removePassengers(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getPassengers().isEmpty()) return;

        for (Entity entity : player.getPassengers()) {
            if (!isLiving(entity)) continue;
            LivingEntity livingEntity = (LivingEntity) entity;
            player.removePassenger(livingEntity);
            livingEntity.setVelocity(player.getLocation().getDirection().multiply(5));
            subtractHealth(livingEntity, 5);
            player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 1);
            player.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, player.getLocation(), 30);
        }
    }
}
