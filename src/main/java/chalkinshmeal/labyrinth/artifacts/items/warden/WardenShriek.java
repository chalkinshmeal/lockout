package chalkinshmeal.labyrinth.artifacts.items.warden;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static chalkinshmeal.labyrinth.utils.Utils.playSound;

public class WardenShriek extends LabyrinthItem {
    public WardenShriek(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Cosmetics
        playSound(player, Sound.ENTITY_WARDEN_SONIC_BOOM, 0.5F);

        // Create explosions + particles
        Vector playerLookDir = player.getEyeLocation().getDirection();
        Location base = player.getEyeLocation();
        int STEPS = 10;
        for (int i = 0; i < STEPS; i++) {
            base.add(playerLookDir);
            player.getWorld().spawnParticle(Particle.SONIC_BOOM, base, 1);
            if (i > 4) player.getWorld().createExplosion(base, 2);
        }
    }
}
