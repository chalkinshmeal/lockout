package chalkinshmeal.labyrinth.artifacts.items.enderman;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class EndermanUltimate extends LabyrinthItem {
    Map<UUID, EnderCrystal> crystals;
    public EndermanUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
        this.crystals = new HashMap<>();
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
        experienceCountdown(plugin, game, player, getDurationInSeconds());

        // Cosmetics
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.GOLD + player.getName() + ChatColor.DARK_PURPLE + " has been chosen");

        // Create crystal
        EnderCrystal crystal = (EnderCrystal) player.getWorld().spawnEntity(player.getLocation(), EntityType.END_CRYSTAL);
        this.crystals.put(player.getUniqueId(), crystal);
        this.game.addPlayerSummons(player, List.of(crystal));
        crystal.setBeamTarget(player.getLocation());

        // Start scheduled task
        this.endermanUltimateTask(plugin, player, crystal, (int) getDurationInSeconds(), (int) getCooldownInSeconds());
    }

    public static void endermanUltimateTask(JavaPlugin plugin, Player player, EnderCrystal crystal, int duration, int cooldown) {
        new BukkitRunnable() {
            float secsLeft = duration;
            float secsMax = duration;
            @Override
            public void run() {
                crystal.setBeamTarget(player.getLocation().subtract(0, 2, 0));
                DragonFireball fireball = (DragonFireball) player.getWorld().spawnEntity(player.getLocation(), EntityType.DRAGON_FIREBALL);
                fireball.setVelocity(new Vector(0, -1, 0));

                secsLeft -= 1;
                if (secsLeft < 0 || crystal.isDead()) { this.cancel(); }
            }
        }.runTaskTimer(plugin, 20L*0L, 20L*cooldown);
    }
}
