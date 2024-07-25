package chalkinshmeal.labyrinth.artifacts.items.wither;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class WitherUltimate extends LabyrinthItem {
    public WitherUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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
        broadcastGameMsg(ChatColor.RED + "The Wither has been spawned...");
        player.sendMessage(ChatColor.DARK_GRAY + "(You can fly now)");

        // Disable double jump
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setFlySpeed((float) 0.03);
        startWitherUltimateTask(game, player, (int) getDurationInSeconds());
    }

    public void startWitherUltimateTask(Game game, Player player, int duration) {
        new BukkitRunnable() {
            float secsLeft = duration;
            @Override
            public void run() {
                // End condition
                secsLeft -= 1;
                if (game.isGameDone() || secsLeft <= 0) {
                    player.setFlySpeed(1);
                    player.setAllowFlight(false);
                    player.setFlying(false);
                    game.setIsUltimateActive(player.getUniqueId(), false);
                    this.cancel();
                }
            }
        }.runTaskTimer(this.plugin, 20L*0L, 20L*1L);
    }
}
