package chalkinshmeal.labyrinth.artifacts.items.chorus_flower;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.playSound;

public class ChorusFlowerPersonalTeleporter extends LabyrinthItem {
    public ChorusFlowerPersonalTeleporter(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Cosmetics
        player.sendMessage(ChatColor.LIGHT_PURPLE + "Woosh!");
        playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT);

        // Teleport player
        player.teleport(this.game.getLabyrinth().getRandSurfaceLocation());
    }
}
