package chalkinshmeal.labyrinth.artifacts.items.slime;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.strikeFakeLightning;
import static chalkinshmeal.labyrinth.utils.Utils.useHeldItem;

public class SlimeUltimate extends LabyrinthItem {
    private final int spiderCount = 10;

    public SlimeUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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
        broadcastGameMsg(ChatColor.GREEN + "Big boi...?");

        // Spawn slime
        Slime s = (Slime) player.getWorld().spawnEntity(player.getLocation().add(0, 25, 0), EntityType.SLIME);
        s.setSize(8);
        this.game.addPlayerSummons(player, List.of(s));
    }
}
