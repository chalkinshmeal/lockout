package chalkinshmeal.labyrinth.artifacts.items.piglin;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.SpawnUtils;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.AIR;

public class PiglinTrap extends LabyrinthItem {
    List<Block> traps = new ArrayList<>();
    public PiglinTrap(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
    }

    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer(); Block placedBlock = event.getBlockPlaced();
        if (!isPlayerHoldingItemInMainHand(player)) return;

        // Reset/Record state
        useHeldItem(player);
        placeBlock(event, this.material);
        this.game.addPlayerSummons(player, placedBlock, AIR);
        this.traps.add(placedBlock);

        // Cosmetics
        playSound(placedBlock.getLocation(), Sound.BLOCK_TRIPWIRE_ATTACH);
    }

    public void onBlockRedstoneEvent(BlockRedstoneEvent event) {
        Block block = event.getBlock();
        if (!isMaterial(block, this.material)) return;
        if (!traps.contains(block)) return;

        // Reset state
        traps.remove(block);
        this.game.removePlayerSummon(block);

        // Create explosion
        SpawnUtils.spawnExplosion(block.getLocation(), 1.5F);
    }

}