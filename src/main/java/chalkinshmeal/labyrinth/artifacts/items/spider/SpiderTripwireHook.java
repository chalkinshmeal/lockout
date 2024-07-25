package chalkinshmeal.labyrinth.artifacts.items.spider;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.giveEffect;
import static chalkinshmeal.labyrinth.utils.Utils.placeBlock;
import static org.bukkit.Material.*;
import static org.bukkit.event.block.Action.PHYSICAL;
import static org.bukkit.potion.PotionEffectType.POISON;
import static org.bukkit.potion.PotionEffectType.SLOWNESS;

public class SpiderTripwireHook extends LabyrinthItem {
    public final int range = 40;
    List<List<Block>> tripwires = new ArrayList<>();
    public SpiderTripwireHook(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
    }

    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer(); Block placedBlock = event.getBlockPlaced();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset/Record state
        resetItemCooldown(player);
        placeBlock(event, TRIPWIRE_HOOK);
        this.game.addPlayerSummons(player, placedBlock, AIR);

        // Check if there is a tripwire opposite, facing this one
        Block baseBlock = event.getBlockAgainst();
        Block opposingHook = getOpposingHook(baseBlock, placedBlock);
        if (opposingHook == null) return;

        // Fill with string
        setBlockBetweenPoints(game, player, placedBlock, opposingHook, TRIPWIRE);
    }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer(); Block clickedBlock = event.getClickedBlock();
        if (!event.getAction().equals(PHYSICAL)) return;
        if (isPlayerHoldingItemInInventory(player)) return;

        // Check if a tripwire was set off. If so, perform action
        for (List<Block> tripwireBlocks : this.tripwires) {
            for (Block block : tripwireBlocks) {
                if (clickedBlock.equals(block)) {
                    // Remove tripwires
                    this.tripwires.remove(tripwireBlocks);
                    for (Block removedBlock : tripwireBlocks) this.game.removePlayerSummon(removedBlock);

                    // Give player effect
                    giveEffect(player, SLOWNESS, 2, 5);
                    giveEffect(player, POISON, 2, 5);
                    this.game.getCooldownHandler().resetCooldown("double-jump", player);

                    // Cosmetics
                    player.sendMessage(ChatColor.DARK_PURPLE + "You set off a poison trap!");

                    return;
                }
            }
        }
    }

    public Block getOpposingHook(Block baseBlock, Block placedBlock) {
        BlockFace face = baseBlock.getFace(placedBlock);
        BlockFace oppositeFace = face.getOppositeFace();
        Vector v = baseBlock.getFace(placedBlock).getDirection();
        Location loc = placedBlock.getLocation();
        for (int i = 0; i < range; i++) {
            loc.add(v);
            Block currentBlock = loc.getWorld().getBlockAt(loc);
            if (currentBlock.getType().equals(TRIPWIRE_HOOK)) {
                Directional blockData = (Directional) currentBlock.getBlockData();
                if (blockData.getFacing().equals(oppositeFace)) {
                    return currentBlock;
                }
            }
            if (!currentBlock.getType().equals(AIR)) break;
        }
        return null;
    }

    public void setBlockBetweenPoints(Game game, Player player, Block b1, Block b2, Material m) {
        // Note: Only works for 2D
        Location loc1 = b1.getLocation();
        Location loc2 = b2.getLocation();
        int x1 = (int) loc1.getX();
        int x2 = (int) loc2.getX();
        int y1 = (int) loc1.getY();
        int y2 = (int) loc2.getY();
        int z1 = (int) loc1.getZ();
        int z2 = (int) loc2.getZ();
        if (y1 != y2) return;

        Vector step;
        int numSteps;

        if (x1 == x2) {
            step = new Vector(0, 0, Math.signum(z2-z1));
            numSteps = Math.abs(z2 - z1) - 1;
        }
        else if (z1 == z2) {
            step = new Vector(Math.signum(x2-x1), 0, 0);
            numSteps = Math.abs(x2 - x1) - 1;
        }
        else return;

        List<Block> tripwireBlocks = new ArrayList<>();
        tripwireBlocks.add(b1);
        tripwireBlocks.add(b2);
        for (int i = 0; i < numSteps; i++) {
            Location setLoc = loc1.add(step);
            Block setBlock = setLoc.getWorld().getBlockAt(setLoc);
            Material prevMaterial = setBlock.getType();
            setBlock.setType(m);
            game.addPlayerSummons(player, setBlock, prevMaterial);
            tripwireBlocks.add(setBlock);
        }
        this.tripwires.add(tripwireBlocks);
    }
}