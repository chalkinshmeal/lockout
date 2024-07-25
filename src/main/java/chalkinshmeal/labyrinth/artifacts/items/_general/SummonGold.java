package chalkinshmeal.labyrinth.artifacts.items._general;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.GOLDEN_SWORD;
import static org.bukkit.Material.STONE_BUTTON;

public class SummonGold extends LabyrinthItem {
    public SummonGold(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Launch golden weapon
        ArmorStand weapon = getNewArmorStand(player.getLocation(), false, false);
        this.game.addPlayerSummons(player, List.of(weapon));
        weapon.setCanMove(true);
        weapon.setGravity(true);
        weapon.setItem(EquipmentSlot.HAND, new ItemStack(GOLDEN_SWORD));

        Snowball projectile = player.launchProjectile(Snowball.class);
        projectile.setGravity(false);
        projectile.setItem(new ItemStack(STONE_BUTTON));
        projectile.setVelocity(player.getEyeLocation().getDirection().multiply(2));
        projectile.setPassenger(weapon);

        // Track entity
        for (Entity e : projectile.getNearbyEntities(30, 30, 30)) {
            if (e instanceof LivingEntity && !e.equals(player) && !(e instanceof ArmorStand)) {
                trackEntity(projectile, (LivingEntity) e);
                break;
            }
        }
    }

    public void onProjectileHitEvent(ProjectileHitEvent event) {
        ProjectileSource shooter = event.getEntity().getShooter();
        Projectile projectile = event.getEntity();
        Block hitBlock = event.getHitBlock();
        Entity hitEntityDeadOrAlive = event.getHitEntity();
        if (!isShooterAPlayer(shooter)) return;
        if (!isProjectileAnItem(projectile, STONE_BUTTON)) return;
        if (hitBlock != null) {
            for (Entity passenger : projectile.getPassengers()) {
                passenger.remove();
            }
            projectile.remove();
        }
        if (!isLiving(hitEntityDeadOrAlive)) return; LivingEntity hitEntity = (LivingEntity) hitEntityDeadOrAlive;

        // Reset/Record state
        event.setCancelled(true);

        // Damage entity
        hitEntity.damage(6);

        // Kill projectile
        //for (Entity passenger : projectile.getPassengers()) {
        //    passenger.remove();
        //}
        //projectile.remove();
    }

    public void trackEntity(Projectile projectile, LivingEntity target) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // End condition
                Vector projToTarget = getVectorFromProjectileToEntity(projectile, target).multiply(4);
                projectile.setVelocity(projToTarget);
                if (projectile.isOnGround() || projectile.isDead()) this.cancel();
            }
        }.runTaskTimer(this.plugin, (long) (20L*0), (long) (20L*0.1));
    }
}
