package chalkinshmeal.labyrinth.artifacts.items._general;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Particles.spawnParticleCircle;

public class LightningCircle extends LabyrinthItem {
    public LightningCircle(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;
        Block block = player.getTargetBlock(50);
        if (block == null) return;

        // Reset/Record state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Cosmetics - create ring of particles above hit block
        Location center = block.getLocation().add(0, 7, 0);
        spawnParticleCircle(center, Particle.SOUL_FIRE_FLAME, 10);
    }
}
