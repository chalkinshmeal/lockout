package chalkinshmeal.labyrinth.artifacts.items._general;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.giveEffect;
import static chalkinshmeal.labyrinth.utils.Utils.isAPlayer;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class SpeedI extends LabyrinthItem {
    public SpeedI(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onSpawn(Player player) {
        if (!isPlayerHoldingItemInInventory(player)) return;

        giveEffect(player, SPEED, 1000000, 1);
    }

    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!isPlayerHoldingItemInInventory(player)) return;

        giveEffect(player, SPEED, 1000000, 1);
    }
    public void onInventoryDragEvent(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!isPlayerHoldingItemInInventory(player)) return;

        giveEffect(player, SPEED, 1000000, 1);
    }

    public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
        Entity entity = event.getEntity(); ItemStack item = event.getItem().getItemStack();
        if (!isAPlayer(entity)) return; Player player = (Player) entity;
        if (!isItemThisItem(item)) return;

        giveEffect(player, SPEED, 1000000, 1);
    }
}
