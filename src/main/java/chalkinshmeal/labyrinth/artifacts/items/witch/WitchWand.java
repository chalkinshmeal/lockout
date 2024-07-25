package chalkinshmeal.labyrinth.artifacts.items.witch;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.strikeLightning;

public class WitchWand extends LabyrinthItem {
    private final int maxRange = 50;
    public WitchWand(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;
        Block block = player.getTargetBlock(maxRange);
        if (block == null) return;

        // Reset state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Zap lightning at the location
        strikeLightning(block.getLocation());
    }
}
