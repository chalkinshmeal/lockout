package chalkinshmeal.labyrinth.artifacts.items.enderman;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.Particles;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.ENDER_EYE;

public class EndermanEye extends LabyrinthItem {
    public EndermanEye(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Cosmetics
        playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_LAUNCH);

        // Send endersignal
        EnderSignal eyeOfEnder = (EnderSignal) player.getWorld().spawnEntity(player.getLocation(), EntityType.EYE_OF_ENDER);
        eyeOfEnder.setTargetLocation(player.getEyeLocation().add(0, 2, 0));
        eyeOfEnder.setDropItem(true);
    }
    public void onItemSpawnEvent(ItemSpawnEvent event) throws ReflectiveOperationException {
        Item eyeOfEnder = event.getEntity();
        if (!eyeOfEnder.getItemStack().getType().equals(ENDER_EYE)) return;
        event.setCancelled(true);

        int range = 15;
        for (Entity _e : eyeOfEnder.getNearbyEntities(range, range, range)) {
            if (!(_e instanceof LivingEntity)) continue;
            LivingEntity e = (LivingEntity) _e;
            if (!isAirBetweenPoints(e.getEyeLocation(), eyeOfEnder.getLocation())) continue;

            Particles.spawnParticleLine(e.getEyeLocation(), eyeOfEnder.getLocation(), Particle.END_ROD, 0.2);
            //try {
            //    Laser.GuardianLaser laser = new Laser.GuardianLaser(e.getEyeLocation(), eyeOfEnder.getLocation(), 3, 50);
            //    laser.start(plugin);
            //}
            //catch (Exception exp) {
            //    plugin.getServer().getLogger().warning("EndermanEye: Laser failed to initialize");
            //}
            subtractHealth(e, 5);
            playSound(e.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT);
        }
    }
}
