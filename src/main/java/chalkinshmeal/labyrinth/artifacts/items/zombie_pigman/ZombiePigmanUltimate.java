package chalkinshmeal.labyrinth.artifacts.items.zombie_pigman;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.AIR;

public class ZombiePigmanUltimate extends LabyrinthItem {
    Map<UUID, Entity> trappedEntities;
    public ZombiePigmanUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
        this.trappedEntities = new HashMap<>();
    }

    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer(); Entity entity = event.getRightClicked();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!doesPlayerHaveUltimate(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);
        setUsedUltimate(player);

        // Cosmetics
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.GOLD + "Ryoiki... Tenkai... ");
        playSound(player, Sound.ENTITY_EVOKER_PREPARE_SUMMON);

        // Choose a point - build limit at 319
        Location p1 = player.getLocation();
        p1.setY(300);
        Location p2 = player.getLocation();
        p2.setY(300);
        p2.add(15, 6, 15);

        // Build the domain
        List<Location> locs = getWallsBetweenPoints(p1, p2);
        List<Block> blocks = new ArrayList<>();
        for (Location loc : locs) {
            blocks.add(loc.getBlock());
            loc.getBlock().setType(Material.GOLD_BLOCK);
            this.game.addPlayerSummons(player, loc.getBlock(), AIR);
        }

        // Teleport player
        player.teleport(p1.add(3, 2, 3));
        entity.teleport(p1.add(8, 2, 8));

        int piglinCount = 5;
        List<Entity> piglins = new ArrayList<>();
        for (int i = 0; i < piglinCount; i++) {
            PigZombie piglin = (PigZombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIFIED_PIGLIN);
            piglin.setAware(true);
            piglins.add(piglin);
        }
        this.game.addPlayerSummons(player, piglins);
        this.trappedEntities.put(player.getUniqueId(), entity);
    }
    public void onEntityDeathEvent(EntityDeathEvent event) {
        for (UUID uuid : this.trappedEntities.keySet()) {
            Entity killedEntity = event.getEntity();
            Player player = this.plugin.getServer().getPlayer(uuid);
            Entity trappedEntity = this.trappedEntities.get(uuid);
            if (killedEntity.equals(player) && isAPlayer(trappedEntity)) {
                this.game.teleportPlayerToRandSpawn((Player) trappedEntity);
                this.trappedEntities.remove(uuid);
            }
            else if (killedEntity.equals(trappedEntity)) {
                this.game.teleportPlayerToRandSpawn(player);
                this.trappedEntities.remove(uuid);
            }
        }
    }
}
