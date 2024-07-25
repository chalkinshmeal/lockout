package chalkinshmeal.labyrinth.artifacts.items.rabbit;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class RabbitUltimate extends LabyrinthItem {
    public RabbitUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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

        // Give Speed
        giveEffect(player, SPEED, getDurationInSeconds(), 5);

        // Cosmetics
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.GOLD + "It's kicking time!! :)");
    }
}
