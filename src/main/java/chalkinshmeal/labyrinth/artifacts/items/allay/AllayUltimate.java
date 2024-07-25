package chalkinshmeal.labyrinth.artifacts.items.allay;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class AllayUltimate extends LabyrinthItem {
    public AllayUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!doesPlayerHaveUltimate(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);
        setUsedUltimate(player);

        // Cosmetics
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.AQUA + "Collection Complete.");

        // Give allays random items
        List<Entity> summons = game.getSummonedEntities().get(player.getUniqueId());
        for (Entity summon : summons) {
            if (summon instanceof Allay) {
                Allay allay = (Allay) summon;
                ItemStack item = this.game.getLabyrinthItemHandler().getRandItem(false, true);
                giveItem(allay, item);
            }
        }
    }
}
