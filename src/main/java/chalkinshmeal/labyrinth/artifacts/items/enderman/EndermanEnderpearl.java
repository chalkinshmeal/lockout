package chalkinshmeal.labyrinth.artifacts.items.enderman;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static chalkinshmeal.labyrinth.utils.Utils.playSound;

public class EndermanEnderpearl extends LabyrinthItem {
    public EndermanEnderpearl(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Cosmetics
        playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT);

        // Teleport to top block where player is looking
        Location loc = player.getTargetBlock(100).getLocation();
        player.teleport(loc.getWorld().getHighestBlockAt(loc).getLocation().add(0, 2, 0));

        // Kill velocity
        player.setVelocity(new Vector(0, 0, 0));
    }
}
