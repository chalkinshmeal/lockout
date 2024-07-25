package chalkinshmeal.labyrinth.artifacts.items.snow_golem;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.JACK_O_LANTERN;

public class SnowGolemUltimate extends LabyrinthItem {
    public SnowGolemUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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
        setUltimateDone(plugin, game, player, getDurationInSeconds());

        // Cosmetics
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.GOLD + "The snow golem has learned a new spell...");
        setHelmet(player, JACK_O_LANTERN);
        startSnowGolemReplaceJackOLanternTask(this.game, player, (int) getDurationInSeconds());
    }

    public void startSnowGolemReplaceJackOLanternTask(Game game, Player player, int secs) {
        new BukkitRunnable() {
            @Override
            public void run() {
                setHelmet(player, Material.PUMPKIN);
            }
        }.runTaskLater(this.plugin, 20L*secs);
    }
}
