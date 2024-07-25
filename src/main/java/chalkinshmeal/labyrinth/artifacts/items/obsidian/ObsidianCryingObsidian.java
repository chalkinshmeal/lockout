package chalkinshmeal.labyrinth.artifacts.items.obsidian;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.Particles;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.AIR;
import static org.bukkit.Material.CRYING_OBSIDIAN;
import static org.bukkit.Particle.DRAGON_BREATH;

public class ObsidianCryingObsidian extends LabyrinthItem {
    Multimap<UUID, Block> obsidians = HashMultimap.create();
    public ObsidianCryingObsidian(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
    }

    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer(); Block placedBlock = event.getBlockPlaced();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset/Record state
        useHeldItem(player);
        resetItemCooldown(player);
        placeBlock(event, CRYING_OBSIDIAN);
        this.game.addPlayerSummons(player, placedBlock, AIR);
        this.obsidians.put(player.getUniqueId(), placedBlock);

        // Create damage pillars
        for (Block b : this.obsidians.get(player.getUniqueId())) {
            this.spawnDamagePillar(b.getLocation());
        }

        // Cosmetics
        playSound(placedBlock.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 2F);
        playSound(placedBlock.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 10000F);
    }

    // Spawns a damage pillar at a location
    private void spawnDamagePillar(Location loc) {
        int maxRange = 5;
        int maxDamage = 10;
        float x = (float) loc.getX() + 0.5F;
        float z = (float) loc.getZ() + 0.5F;
        for (Entity entity : loc.getNearbyEntities(maxRange, maxRange, maxRange)) {
            if (!isLiving(entity)) continue; LivingEntity victim = (LivingEntity) entity;

            // Get distance from damage pillar
            float distance = getDistance(x, z, (float) victim.getX(), (float) victim.getZ());

            // Get damage, inversely proportional to how far away you are
            float proportion = (maxRange - distance) / maxRange;
            float damage = maxDamage * proportion;

            // Damage entity
            victim.damage(damage);
            victim.setNoDamageTicks(0);
        }

        // Cosmetics
        int maxHeight = loc.getWorld().getMaxHeight();
        for (int y = (int) loc.getY(); y < maxHeight; y++) {
            Location destLoc = new Location(loc.getWorld(), loc.getX() + 0.5, y, loc.getZ() + 0.5);
            Particles.spawnParticleCircle(destLoc, DRAGON_BREATH, 1);
        }
    }
}
