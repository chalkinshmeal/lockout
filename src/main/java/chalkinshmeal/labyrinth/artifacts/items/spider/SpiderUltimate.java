package chalkinshmeal.labyrinth.artifacts.items.spider;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.STRENGTH;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class SpiderUltimate extends LabyrinthItem {
    private final int spiderCount = 10;

    public SpiderUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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
        broadcastGameMsg(ChatColor.RED + "The spiders have answered their queen's call...");

        // Spawn spiders
        Location spawnLoc = getNearestAirLocation(player.getLocation(), true);
        List<Entity> spiders = new ArrayList<>();
        for (int i = 0; i < spiderCount; i++) {
            Spider s = (Spider) player.getWorld().spawnEntity(spawnLoc, EntityType.CAVE_SPIDER);
            s.setAware(true);
            giveEffect(s, SPEED, 100000, 2);
            giveEffect(s, STRENGTH, 100000, 1);
            spiders.add(s);
        }
        this.game.addPlayerSummons(player, spiders);
    }
}
