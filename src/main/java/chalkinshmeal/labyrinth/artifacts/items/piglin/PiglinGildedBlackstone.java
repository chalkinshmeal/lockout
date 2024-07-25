package chalkinshmeal.labyrinth.artifacts.items.piglin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.Particles;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.AIR;
import static org.bukkit.Material.GILDED_BLACKSTONE;
import static org.bukkit.potion.PotionEffectType.SLOWNESS;

public class PiglinGildedBlackstone extends LabyrinthItem {
    private final float range = 4;

    Multimap<UUID, Block> stones = HashMultimap.create();
    public PiglinGildedBlackstone(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
    }

    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer(); Block placedBlock = event.getBlockPlaced();
        if (!isPlayerHoldingItemInMainHand(player)) return;

        // Reset/Record state
        useHeldItem(player);
        placeBlock(event, GILDED_BLACKSTONE);
        this.game.addPlayerSummons(player, placedBlock, AIR);
        this.stones.put(player.getUniqueId(), placedBlock);

        // Slow players
        slowPlayers(this, player, placedBlock);

        // Cosmetics
        playSound(placedBlock.getLocation(), Sound.BLOCK_DEEPSLATE_PLACE);
    }

    public void slowPlayers(PiglinGildedBlackstone item, Player player, Block block) {
        new BukkitRunnable() {
            final int livesLeft = game.getScoreboard().getLives(player);
            @Override
            public void run() {
                // End conditions
                if (game == null) { this.cancel(); return; }
                if (game.isGameDone()) { this.cancel(); return; }
                if (!game.doesPlayerHaveLives(player, livesLeft)) { this.cancel(); return; }

                // Slow players
                Location loc = new Location(block.getWorld(), block.getX()+0.5, block.getY()+0.5, block.getZ()+0.5);
                for (LivingEntity entity : loc.getNearbyLivingEntities(item.range)) {
                    if (!entity.equals(player))
                        giveEffect(entity, SLOWNESS, 1F, 4);
                }

                // Cosmetics
                Particles.spawnParticleSphere(loc, Color.BLACK, item.range+1);
            }
        }.runTaskTimer(this.plugin, 20L * 0L, (long) (20L * 0.25F));
    }
}