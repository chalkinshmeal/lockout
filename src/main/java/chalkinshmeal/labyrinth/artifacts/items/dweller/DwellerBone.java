package chalkinshmeal.labyrinth.artifacts.items.dweller;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.BONE;

public class DwellerBone extends LabyrinthItem {
    public DwellerBone(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);

        // Launch snowball projectile that looks like cobweb
        Snowball projectile = player.launchProjectile(Snowball.class);
        projectile.setItem(new ItemStack(BONE));
        projectile.setVelocity(player.getEyeLocation().getDirection().multiply(2));
    }

    public void onProjectileHitEvent(ProjectileHitEvent event) {
        ProjectileSource shooter = event.getEntity().getShooter();
        Projectile projectile = event.getEntity();
        Block hitBlock = event.getHitBlock();
        Entity hitEntity = event.getHitEntity();
        if (!isShooterAPlayer(shooter)) return;
        if (!isProjectileAnItem(projectile, BONE)) return;

        // Reset/Record state
        event.setCancelled(true);

        // Calculate location
        Location hitLocation = null;
        if (hitBlock != null) hitLocation = hitBlock.getLocation();
        if (hitEntity != null) hitLocation = hitEntity.getLocation();

        // Do explosion
        Location tntSpawnLoc = getNearestAirLocation(hitLocation, false);
        ((Player) shooter).getWorld().createExplosion(tntSpawnLoc, 2);
        playSound(tntSpawnLoc, Sound.ENTITY_GENERIC_EXPLODE);
        ((Player) shooter).spawnParticle(Particle.EXPLOSION_EMITTER, tntSpawnLoc, 10);
    }
}
