package chalkinshmeal.labyrinth.artifacts.items.zombie_pigman;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class ZombiePigmanBling extends LabyrinthItem {
    public ZombiePigmanBling(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;

        // Reset state
        event.setCancelled(true);
        useHeldItem(player);

        // Bling!
        boolean is_armor = (getRandNum(0, 1) == 0);
        String chosenItem = null;
        Enchantment enchantment = null;
        int lvl = getRandNum(1, 5);

        if (is_armor) {
            enchantment = getRandGoodEnchantment("armor");
            int armorPiece = getRandNum(0, 3);
            if (armorPiece == 0) addEnchantToHelmet(player, enchantment, lvl);
            if (armorPiece == 1) addEnchantToChestplate(player, enchantment, lvl);
            if (armorPiece == 2) addEnchantToLeggings(player, enchantment, lvl);
            if (armorPiece == 3) addEnchantToBoots(player, enchantment, lvl);

            if (armorPiece == 0) chosenItem = "helmet";
            if (armorPiece == 1) chosenItem = "chestplate";
            if (armorPiece == 2) chosenItem = "leggings";
            if (armorPiece == 3) chosenItem = "boots";
        }
        else {
            enchantment = getRandGoodEnchantment("weapon");
            addEnchantToItem(player, 0, enchantment, lvl);
            chosenItem = "weapon";
        }
        String lvlStr = "";
        if (lvl == 1) lvlStr = "I";
        if (lvl == 2) lvlStr = "II";
        if (lvl == 3) lvlStr = "III";
        if (lvl == 4) lvlStr = "IV";
        if (lvl == 5) lvlStr = "V";
        String enchantName = (enchantment.getKey().toString()).replace("minecraft:", "");
        enchantName = Character.toUpperCase(enchantName.charAt(0)) + enchantName.substring(1);
        player.sendMessage(ChatColor.GOLD + "Your " + chosenItem + " have been enchanted with " + enchantName + " " + lvlStr);
    }
}
