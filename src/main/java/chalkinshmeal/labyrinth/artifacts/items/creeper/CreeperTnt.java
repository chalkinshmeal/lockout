package chalkinshmeal.labyrinth.artifacts.items.creeper;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.useHeldItem;

public class CreeperTnt extends LabyrinthItem {
    public CreeperTnt(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer(); Block placedBlock = event.getBlockPlaced();
        if (!isPlayerHoldingItemInMainHand(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);

        // Ignite the tnt
        TNTPrimed tnt = (TNTPrimed) placedBlock.getWorld().spawnEntity(placedBlock.getLocation(), EntityType.TNT);
        tnt.setFuseTicks(30);
    }
}
