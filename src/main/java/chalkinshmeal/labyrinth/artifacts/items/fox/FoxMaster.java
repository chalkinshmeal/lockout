package chalkinshmeal.labyrinth.artifacts.items.fox;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FoxMaster extends LabyrinthItem {
    public FoxMaster(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Teleport all fox summons to you
        for (Entity summon : this.game.getSummonedEntities().get(player.getUniqueId())) {
            if (summon instanceof Fox) {
                summon.teleport(player);
            }
        }
    }
}
