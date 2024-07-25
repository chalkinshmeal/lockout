package chalkinshmeal.labyrinth.artifacts.items.spider;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.AIR;
import static org.bukkit.Material.COBWEB;

public class SpiderWeb extends LabyrinthItem {
    public SpiderWeb(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);

        // Launch snowball projectile that looks like cobweb
        Snowball projectile = player.launchProjectile(Snowball.class);
        projectile.setItem(new ItemStack(COBWEB));
        projectile.setVelocity(player.getEyeLocation().getDirection().multiply(2));
    }

    public void onProjectileHitEvent(ProjectileHitEvent event) {
        ProjectileSource shooter = event.getEntity().getShooter();
        Projectile projectile = event.getEntity();
        Block hitBlock = event.getHitBlock();
        Entity hitEntity = event.getHitEntity();
        if (!isShooterAPlayer(shooter)) return;
        if (!isProjectileAnItem(projectile, COBWEB)) return;

        // Reset/Record state
        event.setCancelled(true);

        // Determine location to spawn web
        Location hitLocation = null;
        if (hitBlock != null) { hitLocation = getNearestAirLocation(hitBlock, false); }
        else if (hitEntity != null && hitEntity instanceof LivingEntity) { hitLocation = ((LivingEntity) hitEntity).getEyeLocation(); }
        else if (hitEntity != null) { hitLocation = hitEntity.getLocation(); }

        // If we get here, something went wrong
        if (!hitLocation.getBlock().getType().equals(AIR)) return;

        // Set block to web
        Block thrownWeb = hitLocation.getWorld().getBlockAt(hitLocation);
        thrownWeb.setType(Material.COBWEB);
        this.game.addPlayerSummons((Player) shooter, thrownWeb, AIR);

        // Remove after a cooldown
        removeCobweb(game, thrownWeb, (int) getDurationInSeconds());
    }

    public void removeCobweb(Game game, Block block, int duration) {
        new BukkitRunnable() {
            @Override
            public void run() {
                game.removePlayerSummon(block);
            }
        }.runTaskLater(this.plugin, 20L*duration);
    }
}
