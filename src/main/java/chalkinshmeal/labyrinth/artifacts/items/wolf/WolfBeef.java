package chalkinshmeal.labyrinth.artifacts.items.wolf;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class WolfBeef extends LabyrinthItem {
    public WolfBeef(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        useHeldItem(player);
        addHealth(player, 6);
        giveEffect(player, SPEED, 5, 2);
        playSound(player, Sound.ENTITY_GENERIC_EAT);
    }
}
