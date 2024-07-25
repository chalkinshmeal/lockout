package chalkinshmeal.labyrinth.artifacts.items.cactus;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.enchantments.Enchantment.THORNS;
import static org.bukkit.potion.PotionEffectType.RESISTANCE;

public class CactusUltimate extends LabyrinthItem {
    public CactusUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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
        broadcastGameMsg(ChatColor.DARK_GREEN + "One has been crowned with thorns...");

        // Give cactus ultimate items
        giveEffect(player, RESISTANCE, 45, 1);
        addEnchantToHelmet(player, THORNS, 4);
        addEnchantToChestplate(player, THORNS, 4);
        addEnchantToLeggings(player, THORNS, 4);
        addEnchantToBoots(player, THORNS, 4);
    }
}
