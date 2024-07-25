package chalkinshmeal.labyrinth.artifacts.items.wither;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.WITHER;

public class WitherBow extends LabyrinthItem {
    public WitherBow(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        if (this.game.isUltimateActive(player)) {
            setItemCooldown(player, (int) (this.getCooldownInTicks() / 2));
        }
        else {
            resetItemCooldown(player);
        }

        // Cosmetics
        playSound(player, Sound.ENTITY_WITHER_SHOOT, 0.5F);

        player.launchProjectile(WitherSkull.class);
    }

    public void onProjectileHitEvent(ProjectileHitEvent event) {
        ProjectileSource shooter = event.getEntity().getShooter();
        Projectile projectile = event.getEntity();
        Block hitBlock = event.getHitBlock();
        Entity hitEntity = event.getHitEntity();
        if (!isShooterAPlayer(shooter)) return;
        if (!isProjectileAWitherSkull(projectile)) return;

        // Calculate location
        Location hitLocation = null;
        if (hitBlock != null) hitLocation = hitBlock.getLocation();
        if (hitEntity != null) hitLocation = hitEntity.getLocation();

        // Create splash potion at that location
        ItemStack potion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(WITHER, 80, 1), false);
        potionMeta.setColor(Color.BLACK);
        potion.setItemMeta(potionMeta);

        Location spawnPotionLoc = getNearestAirLocation(hitLocation, true);
        ThrownPotion thrownPotion = (ThrownPotion) hitLocation.getWorld().spawnEntity(spawnPotionLoc, EntityType.POTION);
        thrownPotion.setItem(potion);
    }
}
