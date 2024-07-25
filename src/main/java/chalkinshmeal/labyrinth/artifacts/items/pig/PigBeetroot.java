package chalkinshmeal.labyrinth.artifacts.items.pig;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.useHeldItem;

public class PigBeetroot extends LabyrinthItem {
    public PigBeetroot(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;

        // Reset state
        event.setCancelled(true);
        useHeldItem(player);

        PlayerInventory inv = player.getInventory();
        inv.addItem(this.game.getLabyrinthItemHandler().getRandItem(false));
    }
}
