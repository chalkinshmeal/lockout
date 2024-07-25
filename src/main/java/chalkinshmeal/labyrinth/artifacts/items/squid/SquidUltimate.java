package chalkinshmeal.labyrinth.artifacts.items.squid;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.*;
import static org.bukkit.potion.PotionEffectType.*;

public class SquidUltimate extends LabyrinthItem {
    public SquidUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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
        broadcastGameMsg(ChatColor.GOLD + "They have metamorphosized into a beautiful... glow squid!");

        // Give player knight items
        LabyrinthItem glowInkSac = new LabyrinthItem(Map.of(
                "material", GLOW_INK_SAC,
                "enchants", Map.of("SHARPNESS", 4, "KNOCKBACK", 3),
                "displayName", "§bGlow Inker"));
        player.getInventory().setItem(0, glowInkSac.getItemStack());
        player.getInventory().setHelmet(new LabyrinthItem(Map.of("plugin", plugin, "material", LEATHER_HELMET, "colorStr", "#b5f8e1")).getItemStack());
        player.getInventory().setChestplate(new LabyrinthItem(Map.of("plugin", plugin, "material", LEATHER_CHESTPLATE, "colorStr", "#b5f8e1")).getItemStack());
        player.getInventory().setLeggings(new LabyrinthItem(Map.of("plugin", plugin, "material", LEATHER_LEGGINGS, "colorStr", "#b5f8e1")).getItemStack());
        player.getInventory().setBoots(new LabyrinthItem(Map.of("plugin", plugin, "material", LEATHER_BOOTS, "colorStr", "#b5f8e1")).getItemStack());

        giveEffect(player, SPEED, 45, 3);
        giveEffect(player, STRENGTH, 45, 1);
        giveEffect(player, RESISTANCE, 45, 1);
        giveEffect(player, FIRE_RESISTANCE, 45, 1);
        giveEffect(player, ABSORPTION, 45, 4);
    }
}
