package chalkinshmeal.labyrinth.artifacts.items.bat;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.Particles;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class BatShriek extends LabyrinthItem {
    int range = 5;
    float power = 2.5F;
    public BatShriek(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Knockback entities from player
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (!isAirBetweenPoints(entity.getLocation(), player.getEyeLocation())) continue;
            Vector playerEntityVec = getVectorFromPlayerToEntity(player, entity);
            entity.setVelocity(entity.getVelocity().add(playerEntityVec.normalize().multiply(power).setY(0.8)));
        }

        // Cosmetics
        playSound(player, Sound.ENTITY_WARDEN_SONIC_BOOM, 0.3F);
        Particles.spawnParticle(player.getEyeLocation(), Particle.SONIC_BOOM, 1);
        Particles.spawnParticle(player.getEyeLocation(), Particle.EXPLOSION, 30);
    }
}
