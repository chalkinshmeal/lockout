package chalkinshmeal.labyrinth.artifacts.items.warden;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class WardenUltimate extends LabyrinthItem {
    public WardenUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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
        broadcastGameMsg(ChatColor.AQUA + "The Warden can see you...");

        // Buff player, darkness all other players
        giveEffect(player, PotionEffectType.STRENGTH, 20, 5);
        for (Player p : this.game.getPlayers()) {
            if (!p.equals(player))
                giveEffect(p, PotionEffectType.DARKNESS, 20, 2);
        }
        for (Player p : this.game.getPlayers()) {
            if (!p.equals(player))
                giveEffect(p, PotionEffectType.BLINDNESS, 20, 2);
        }
    }
}
