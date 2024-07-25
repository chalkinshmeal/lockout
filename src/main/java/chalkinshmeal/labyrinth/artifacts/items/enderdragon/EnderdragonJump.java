package chalkinshmeal.labyrinth.artifacts.items.enderdragon;

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
import static org.bukkit.potion.PotionEffectType.JUMP_BOOST;

public class EnderdragonJump extends LabyrinthItem {
    public EnderdragonJump(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onStart(Player player) {
        if (!isPlayerHoldingItemInInventory(player)) return;

        giveEffect(player, JUMP_BOOST, 1000000, 6);
    }

    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!isPlayerHoldingItemInInventory(player)) return;

        giveEffect(player, JUMP_BOOST, 1000000, 6);
    }
    public void onInventoryDragEvent(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!isPlayerHoldingItemInInventory(player)) return;

        giveEffect(player, JUMP_BOOST, 1000000, 6);
    }

    public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
        Entity entity = event.getEntity(); ItemStack item = event.getItem().getItemStack();
        if (!isAPlayer(entity)) return; Player player = (Player) entity;
        if (!isItemThisItem(item)) return;

        giveEffect(player, JUMP_BOOST, 1000000, 6);
    }
}
