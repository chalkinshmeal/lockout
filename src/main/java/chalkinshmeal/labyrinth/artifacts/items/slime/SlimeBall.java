package chalkinshmeal.labyrinth.artifacts.items.slime;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.SLIME_BALL;
import static org.bukkit.potion.PotionEffectType.JUMP_BOOST;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class SlimeBall extends LabyrinthItem {
    public SlimeBall(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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
        projectile.setItem(new ItemStack(SLIME_BALL));
        projectile.setVelocity(player.getEyeLocation().getDirection().multiply(2));
    }

    public void onProjectileHitEvent(ProjectileHitEvent event) {
        ProjectileSource shooter = event.getEntity().getShooter();
        Projectile projectile = event.getEntity();
        Block hitBlock = event.getHitBlock();
        Entity hitEntity = event.getHitEntity();
        if (!isShooterAPlayer(shooter)) return;
        if (!isProjectileAnItem(projectile, SLIME_BALL)) return;

        // Reset/Record state
        event.setCancelled(true);

        // Calculate location
        Location hitLocation = null;
        if (hitBlock != null) hitLocation = hitBlock.getLocation();
        if (hitEntity != null) hitLocation = hitEntity.getLocation();

        // Do explosion
        Location tntSpawnLoc = getNearestAirLocation(hitLocation, false);
        ((Player) shooter).getWorld().createExplosion(tntSpawnLoc, 0.5F);
        playSound(tntSpawnLoc, Sound.ENTITY_SLIME_SQUISH, 0.3F);
    }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        this.increaseSlimeSize(event);
        this.decreaseSlimeSize(event);
    }

    public void increaseSlimeSize(EntityDamageByEntityEvent event) {
        Entity attackerEntity = event.getDamager(); Entity victimEntity = event.getEntity();
        if (!isAPlayer(attackerEntity)) return; Player attacker = (Player) attackerEntity;
        if (!isPlayerHoldingItemInInventory(attacker)) return;
        if (getRandNum(0, 1) != 0) return;

        // Immunity to slime attacks
        if (!(victimEntity instanceof Slime)) return; Slime slime = (Slime) victimEntity;

        // Spawn slime
        playSound(slime.getLocation(), Sound.ENTITY_SLIME_ATTACK, 0.5F);
        slime.setSize(slime.getSize()+1);

        // Critical mass
        if (slime.getSize() == 12)
            broadcastGameMsg(ChatColor.GREEN + "Slime reaching critical mass...");

        // Explode at critical mass
        if (slime.getSize() == 13) {
            // Cosmetics
            playSound(slime.getLocation(), Sound.ENTITY_GENERIC_EXPLODE);
            attacker.spawnParticle(Particle.EXPLOSION_EMITTER, slime.getLocation(), 10);

            for (int i = 0; i < 30; i++) {
                Slime s = (Slime) attacker.getWorld().spawnEntity(slime.getLocation(), EntityType.SLIME);
                giveEffect(s, SPEED, 100000, 3);
                giveEffect(s, JUMP_BOOST, 100000, 3);
                int size = getRandNum(0, 2);
                s.setSize(size);
                this.game.addPlayerSummons(attacker, List.of(s));
            }
            slime.remove();
        }
    }

    // Reduce slime size, if damaged
    public void decreaseSlimeSize(EntityDamageByEntityEvent event) {
        Entity attackerEntity = event.getDamager(); Entity victimEntity = event.getEntity();
        if (isPlayerHoldingItemInInventory(attackerEntity)) return;
        if (!(victimEntity instanceof Slime)) return; Slime slime = (Slime) victimEntity;

        slime.setSize(slime.getSize() - 1);
    }
}
