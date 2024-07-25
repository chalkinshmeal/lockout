package chalkinshmeal.labyrinth.artifacts.items.wolf;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.STRENGTH;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class WolfUltimate extends LabyrinthItem {
    private final int wolfCount = 5;

    public WolfUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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
        broadcastGameMsg(ChatColor.GOLD + "The Wolf has summoned its pack...");

        // Spawn spiders
        Location spawnLoc = getNearestAirLocation(player.getLocation(), true);
        List<Entity> wolves = new ArrayList<>();
        for (int i = 0; i < wolfCount; i++) {
            Wolf w = (Wolf) player.getWorld().spawnEntity(spawnLoc, EntityType.WOLF);
            w.setOwner(player);
            w.setAware(true);
            giveEffect(w, SPEED, 100000, 2);
            giveEffect(w, STRENGTH, 100000, 1);
            wolves.add(w);
        }
        this.game.addPlayerSummons(player, wolves);
    }
}
