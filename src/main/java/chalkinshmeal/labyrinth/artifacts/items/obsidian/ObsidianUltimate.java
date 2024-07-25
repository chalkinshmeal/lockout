package chalkinshmeal.labyrinth.artifacts.items.obsidian;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.*;
import static org.bukkit.potion.PotionEffectType.STRENGTH;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class ObsidianUltimate extends LabyrinthItem {
    public ObsidianUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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
        playSound(player, Sound.BLOCK_DEEPSLATE_PLACE);
        broadcastGameMsg(ChatColor.DARK_PURPLE + "Obsidian has broken itself...");
        experienceCountdown(plugin, game, player, getDurationInSeconds());

        // Start delayed die task
        obsidianDelayedDie(game, player, getDurationInSeconds());

        // Buff Player
        addHealth(player, 10000);
        setChestplate(player, NETHERITE_CHESTPLATE);
        setLeggings(player, NETHERITE_LEGGINGS);
        setBoots(player, NETHERITE_BOOTS);
        giveEffect(player, STRENGTH, getDurationInSeconds(), 1);
        giveEffect(player, SPEED, getDurationInSeconds(), 1);
    }

    public void obsidianDelayedDie(Game game, Player player, float duration) {
        new BukkitRunnable() {
            int timeLeft = (int) duration;
            // Save previous armor state
            @Override
            public void run() {
                // End condition
                subtractHealth(player, 2, false);
                timeLeft -= 1;
                if (timeLeft == 2) {
                    playSound(player, Sound.BLOCK_BEACON_DEACTIVATE);
                }
                if (timeLeft < 0) {
                    // Kill
                    player.setHealth(0);
                    this.cancel();
                }
                if (!game.isPlayerInPlay(player)) {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, (long) (20L*0L), 20L*1L);
    }

}