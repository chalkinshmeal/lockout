package chalkinshmeal.labyrinth.artifacts.items.ocelot;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import static chalkinshmeal.labyrinth.utils.Utils.giveEffect;
import static chalkinshmeal.labyrinth.utils.Utils.useHeldItem;
import static org.bukkit.potion.PotionEffectType.SLOWNESS;
import static org.bukkit.potion.PotionEffectType.WEAKNESS;

public class OcelotSlow extends LabyrinthItem {
    public OcelotSlow(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        useHeldItem(player);

        // Cosmetics
        broadcastGameMsg(ChatColor.DARK_AQUA + "You have been purr attacked!");

        // Give slow
        for (UUID uuid : this.game.getInGamePlayerUUIDs()) {
            Player slowedPlayer = this.plugin.getServer().getPlayer(uuid);
            if (slowedPlayer.getName().equals(player.getName())) continue;
            giveEffect(slowedPlayer, SLOWNESS, 10, 2);
            giveEffect(slowedPlayer, WEAKNESS, 10, 2);
        }
    }
}
