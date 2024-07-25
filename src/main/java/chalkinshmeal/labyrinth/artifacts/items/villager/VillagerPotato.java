package chalkinshmeal.labyrinth.artifacts.items.villager;

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
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.BAKED_POTATO;
import static org.bukkit.potion.PotionEffectType.SLOWNESS;

public class VillagerPotato extends LabyrinthItem {
    public VillagerPotato(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);

        // Launch snowball projectile that looks like cobweb
        Snowball projectile = player.launchProjectile(Snowball.class);
        projectile.setItem(new ItemStack(BAKED_POTATO));
        projectile.setVelocity(player.getEyeLocation().getDirection().multiply(2));
    }

    public void onProjectileHitEvent(ProjectileHitEvent event) {
        ProjectileSource shooter = event.getEntity().getShooter();
        Projectile projectile = event.getEntity();
        Block hitBlock = event.getHitBlock();
        Entity hitEntity = event.getHitEntity();
        if (!isShooterAPlayer(shooter)) return;
        if (!isProjectileAnItem(projectile, BAKED_POTATO)) return;

        // Reset/Record state
        event.setCancelled(true);

        // Calculate location
        Location hitLocation = null;
        if (hitBlock != null) hitLocation = hitBlock.getLocation();
        if (hitEntity != null) hitLocation = hitEntity.getLocation();

        // Create splash potion at that location
        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(SLOWNESS, 80, 1), false);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 1), false);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE, 1, 1), false);
        potion.setItemMeta(potionMeta);

        Location spawnPotionLoc = getNearestAirLocation(hitLocation, true);
        ThrownPotion thrownPotion = (ThrownPotion) hitLocation.getWorld().spawnEntity(spawnPotionLoc, EntityType.POTION);
        thrownPotion.setItem(potion);
    }
}
