package chalkinshmeal.labyrinth.artifacts.items.obsidian;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static chalkinshmeal.labyrinth.utils.Utils.playSound;
import static chalkinshmeal.labyrinth.utils.Utils.useHeldItem;
import static org.bukkit.Material.AIR;
import static org.bukkit.Material.BEACON;

public class ObsidianBeacon extends LabyrinthItem {
    Map<UUID, Block> beacons;
    public ObsidianBeacon(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
        this.beacons = new HashMap<>();
    }

    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer(); Block placedBlock = event.getBlockPlaced();
        if (!isPlayerHoldingItemInMainHand(player)) return;

        // Reset/Record state
        useHeldItem(player);

        // Cosmetics
        player.sendMessage(ChatColor.DARK_PURPLE + "Waypoint initialized");
        playSound(player, Sound.BLOCK_BEACON_ACTIVATE);

        // Add block to summons
        this.game.addPlayerSummons(player, placedBlock, AIR);
        this.beacons.put(player.getUniqueId(), placedBlock);

        // Actually place beacon. Need to use getBlockReplacedState, as that is the block AFTER
        // event.setCancelled(true); happens in the preventBlockPlace.
        useHeldItem(player);
        event.getBlockReplacedState().setType(BEACON);
    }

    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getY() >= 0) return;
        if (this.beacons.get(player.getUniqueId()) == null) return;

        // Cosmetics
        player.sendMessage(ChatColor.DARK_PURPLE + "Waypoint activated");
        playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT);

        // Respawn player
        player.spigot().respawn();

        // Teleport player to beacon
        player.teleport(this.beacons.get(player.getUniqueId()).getLocation());

        // Break and reset beacon
        this.beacons.get(player.getUniqueId()).setType(AIR);
        this.beacons.put(player.getUniqueId(), null);
    }

}
