package chalkinshmeal.labyrinth.artifacts.items.sheep;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

import static chalkinshmeal.labyrinth.utils.Utils.getRandNum;
import static chalkinshmeal.labyrinth.utils.Utils.useHeldItem;
import static org.bukkit.Material.*;

public class SheepWool extends LabyrinthItem {
    public SheepWool(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!doesPlayerHaveUltimate(player)) return;

        // Reset state
        event.setCancelled(true);
        useHeldItem(player);

        // Give player a random wool
        int randNum = getRandNum(0, 100);
        LabyrinthItem wool = null;
        String color = "WHITE";
        if (randNum <= 30) { // Blue wool
            wool = new LabyrinthItem(Map.of(
                    "position", 0,
                    "material", BLUE_WOOL,
                    "enchants", Map.of("SHARPNESS", 2, "KNOCKBACK", 3),
                    "colorStr", "BLUE",
                    "displayName", "§1Blue boi"));
            color = "BLUE";
        }
        else if (randNum <= 60) {
            wool = new LabyrinthItem(Map.of(
                    "position", 0,
                    "material", RED_WOOL,
                    "enchants", Map.of("SHARPNESS", 2, "FIRE_ASPECT", 2),
                    "colorStr", "RED",
                    "displayName", "§4Red boi"));
            color = "RED";
        }
        else if (randNum <= 90) {
            wool = this.game.getLabyrinthItemHandler().getItem(56);
            color = "GREEN";
        }
        else if (randNum <= 100) {
            wool = new LabyrinthItem(Map.of(
                    "position", 0,
                    "material", PURPLE_WOOL,
                    "enchants", Map.of("SHARPNESS", 4, "KNOCKBACK", 4),
                    "colorStr", "PURPLE",
                    "displayName", "§5Purple boi"));
            color = "PURPLE";
        }
        player.getInventory().setItemInMainHand(wool.getItemStack());
        player.getInventory().setHelmet(new LabyrinthItem(Map.of("material", LEATHER_HELMET, "colorStr", color)).getItemStack());
        player.getInventory().setChestplate(new LabyrinthItem(Map.of("material", LEATHER_CHESTPLATE, "colorStr", color)).getItemStack());
        player.getInventory().setLeggings(new LabyrinthItem(Map.of("material", LEATHER_LEGGINGS, "colorStr", color)).getItemStack());
        player.getInventory().setBoots(new LabyrinthItem(Map.of("material", LEATHER_BOOTS, "colorStr", color)).getItemStack());
    }
}
