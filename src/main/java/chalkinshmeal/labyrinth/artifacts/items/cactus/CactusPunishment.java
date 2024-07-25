package chalkinshmeal.labyrinth.artifacts.items.cactus;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.Particles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.CACTUS;

public class CactusPunishment extends LabyrinthItem {
    public CactusPunishment(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victimEntity = event.getEntity();
        if (!isAPlayer(victimEntity)) return; Player victim = (Player) victimEntity;
        if (!isPlayerHoldingItemInInventory(victim)) return;

        this.changeItemCount(victim, 1);
    }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Launch snowball projectile that looks like cobweb
        Snowball projectile = player.launchProjectile(Snowball.class);
        projectile.setItem(new ItemStack(CACTUS));
        projectile.setVelocity(player.getEyeLocation().getDirection().multiply(2));
    }

    public void onProjectileHitEvent(ProjectileHitEvent event) {
        ProjectileSource shooter = event.getEntity().getShooter();
        Projectile projectile = event.getEntity();
        Entity hitEntity = event.getHitEntity();
        if (!isShooterAPlayer(shooter)) return; Player player = (Player) shooter;
        if (!isProjectileAnItem(projectile, CACTUS)) return;
        if (hitEntity == null) return;
        if (!isLiving(hitEntity)) return; LivingEntity victim = (LivingEntity) hitEntity;

        // Reset/Record state
        event.setCancelled(true);

        // Calculate location
        Location hitLocation = hitEntity.getLocation();

       // Skewer entity that player hit
       double health = victim.getHealth();
       int charges = getItemCount(player);
       float storedDamage = ((float) charges);
       if (storedDamage >= health) {
           player.sendMessage(ChatColor.GREEN + "Skewered for " + ChatColor.RED + (health / 2) + ChatColor.GREEN + " hearts.");
           int chargesUsed = (int) health;
           victim.damage(health);
           this.setItemCount(player, Math.max(charges - chargesUsed, 1));
       }
       else {
           player.sendMessage(ChatColor.GREEN + "Skewered for " + ChatColor.RED + (storedDamage / 2) + ChatColor.GREEN + " hearts.");
           victim.damage(storedDamage);
           this.setItemCount(player, 1);
       }

       // Teleport player right after skewer
       player.teleport(hitLocation);

       // Cosmetics
       Particles.spawnParticleLine(player.getEyeLocation(), victim.getEyeLocation(), Particle.ANGRY_VILLAGER, 0.1);
       playSound(player, Sound.ENTITY_PANDA_BITE);
    }

}
