package chalkinshmeal.labyrinth.artifacts.items.axolotl;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class AxolotlUltimate extends LabyrinthItem {
    public AxolotlUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.LIGHT_PURPLE + "Someone is feeling a little hungry...");
        experienceCountdown(plugin, game, player, getDurationInSeconds());

        // Start scheduled task to steal items
        axolotlUltimateTask(this.game, player, (int) getCooldownInSeconds(), (int) getDurationInSeconds());
    }

    public void axolotlUltimateTask(Game game, Player player, int cooldown, int duration) {
        new BukkitRunnable() {
            float secsLeft = duration;
            @Override
            public void run() {
                // Ultimate properties
                playSound(player, Sound.ENTITY_ELDER_GUARDIAN_HURT);
                player.getWorld().spawnParticle(Particle.EXPLOSION, player.getLocation(), 30);
                for (Entity e : player.getNearbyEntities(3, 3, 3)) {
                    if (!(e instanceof Player)) continue;
                    if (e.equals(player)) continue;

                    ItemStack item = removeAndGetRandItem((Player) e);
                    if (item == null) return;

                    player.getInventory().addItem(item);

                    playSound(player, Sound.ENTITY_ELDER_GUARDIAN_HURT);
                    ((Player) e).sendMessage(ChatColor.LIGHT_PURPLE + player.getName() + " has stolen from you!");
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "You have stolen an item!");
                }

                // End condition
                secsLeft -= 1;
                if (secsLeft < 0) { this.cancel(); }
            }
        }.runTaskTimer(this.plugin, 20L*0L, 20L*cooldown);
    }
}
