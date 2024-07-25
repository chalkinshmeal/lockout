package chalkinshmeal.labyrinth.artifacts.items.sheep;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

import static chalkinshmeal.labyrinth.utils.Utils.strikeFakeLightning;
import static chalkinshmeal.labyrinth.utils.Utils.useHeldItem;
import static org.bukkit.Material.*;

public class SheepUltimate extends LabyrinthItem {
    public SheepUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);
        setUsedUltimate(player);

        // Cosmetics
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.AQUA + "The sheep has gained some " + ChatColor.RED + "c" + ChatColor.GOLD + "o" + ChatColor.YELLOW + "l" + ChatColor.GREEN + "o" + ChatColor.LIGHT_PURPLE + "r" + ChatColor.AQUA + "...");

        // Give all items
        LabyrinthItem wool = null;
        wool = new LabyrinthItem(Map.of(
                "position", 0,
                "material", BLUE_WOOL,
                "enchants", Map.of("SHARPNESS", 4, "KNOCKBACK", 4),
                "colorStr", "BLUE",
                "displayName", "§1Blue boi"));
        player.getInventory().setItem(0, wool.getItemStack());

        wool = new LabyrinthItem(Map.of(
                "position", 0,
                "material", RED_WOOL,
                "enchants", Map.of("SHARPNESS", 4, "FIRE_ASPECT", 2),
                "colorStr", "RED",
                "displayName", "§4Red boi"));
        player.getInventory().setItem(1, wool.getItemStack());

        wool = this.game.getLabyrinthItemHandler().getItem(56);
        player.getInventory().setItem(2, wool.getItemStack());

        wool = new LabyrinthItem(Map.of(
                "position", 0,
                "material", PURPLE_WOOL,
                "enchants", Map.of("SHARPNESS", 6, "KNOCKBACK", 6),
                "colorStr", "PURPLE",
                "displayName", "§5Purple boi"));
        player.getInventory().setItem(3, wool.getItemStack());

        player.getInventory().setHelmet(new LabyrinthItem(Map.of("material", LEATHER_HELMET, "colorStr", "RED")).getItemStack());
        player.getInventory().setChestplate(new LabyrinthItem(Map.of("material", LEATHER_CHESTPLATE, "colorStr", "GOLD")).getItemStack());
        player.getInventory().setLeggings(new LabyrinthItem(Map.of("material", LEATHER_LEGGINGS, "colorStr", "GREEN")).getItemStack());
        player.getInventory().setBoots(new LabyrinthItem(Map.of("material", LEATHER_BOOTS, "colorStr", "PURPLE")).getItemStack());
    }
}
