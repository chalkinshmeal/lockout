package chalkinshmeal.labyrinth.artifacts.items.piglin;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.*;
import static org.bukkit.enchantments.Enchantment.*;
import static org.bukkit.potion.PotionEffectType.*;

public class PiglinBarter extends LabyrinthItem {
    public PiglinBarter(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    // Increase by 1 if hit or hit someone
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victimEntity = event.getEntity();
        if (isPlayerHoldingItemInInventory(victimEntity)) this.changeItemCount(victimEntity, 1);
        if (isPlayerHoldingItemInInventory(attacker)) this.changeItemCount(attacker, 1);
        if (attacker instanceof Arrow arrow) {
            Entity shooter = (Entity) arrow.getShooter();
            if (isPlayerHoldingItemInInventory(shooter)) this.changeItemCount(shooter, 1);
        }
    }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isMainHand(event)) return;

        // Reset/Record state
        event.setCancelled(true);

        // Barter

        int goldAmount = this.getItemCount(player);
        String itemString = "";
        int cost = 0;

        // Barter items at cost = 15
        if (goldAmount >= 20) {
            cost = 20;

            int randNum = getRandNum(0, 4);
            switch (randNum) {
                case 0:
                    itemString = "Strength II (0:15)";
                    giveEffect(player, STRENGTH, 15, 2);
                    break;
                case 1:
                    itemString = "Multishot to your crossbow";
                    addEnchantToItem(getItem(player, CROSSBOW), MULTISHOT, 1);
                    break;
                case 2:
                    itemString = "Quick Charge IV to your crossbow";
                    addEnchantToItem(getItem(player, CROSSBOW), QUICK_CHARGE, 4);
                    break;
                case 3:
                    itemString = "Absorption V (0:15)";
                    giveEffect(player, ABSORPTION, 15, 5);
                    break;
                case 4:
                    itemString = "a Netherite Chestplate";
                    player.getInventory().addItem(new ItemStack(NETHERITE_CHESTPLATE, 1));
                    break;
            }
        }

        // Barter items at cost = 10
        else if (goldAmount >= 10) {
            cost = 10;

            int randNum = getRandNum(0, 5);
            switch (randNum) {
                case 0:
                    itemString = "a couple of Golden Apples";
                    player.getInventory().addItem(new ItemStack(GOLDEN_APPLE, 2));
                    break;
                case 1:
                    itemString = "Speed III (0:15)";
                    giveEffect(player, SPEED, 15, 3);
                    break;
                case 2:
                    itemString = "Quick Charge II to your crossbow";
                    addEnchantToItem(getItem(player, CROSSBOW), QUICK_CHARGE, 2);
                    break;
                case 3:
                    itemString = "Sharpness III to your crossbow";
                    addEnchantToItem(getItem(player, CROSSBOW), SHARPNESS, 3);
                    break;
                case 4:
                    itemString = "Multishot I to your crossbow";
                    addEnchantToItem(getItem(player, CROSSBOW), MULTISHOT, 1);
                    break;
                case 5:
                    itemString = "Knockback II to your crossbow";
                    addEnchantToItem(getItem(player, CROSSBOW), KNOCKBACK, 2);
                    break;
            }
        }

        // Barter items at cost = 5
        else if (goldAmount >= 5) {
            cost = 5;

            int randNum = getRandNum(0, 2);
            switch (randNum) {
                case 0:
                    itemString = "a Black Stone";
                    player.getInventory().addItem(this.game.getLabyrinthItemHandler().getItem(115).getItemStack().asOne());
                    break;
                case 1:
                    itemString = "some Golden Traps";
                    player.getInventory().addItem(this.game.getLabyrinthItemHandler().getItem(116).getItemStack());
                    break;
                case 2:
                    itemString = "5 fireworks";
                    ItemStack firework = new ItemStack(FIREWORK_ROCKET, 5);
                    FireworkMeta fwm = (FireworkMeta) firework.getItemMeta();
                    fwm.setPower(10);
                    fwm.addEffect(FireworkEffect.builder()
                            .withColor(Color.RED)
                            .build());
                    firework.setItemMeta(fwm);
                    player.getInventory().addItem(firework);
                    break;
                default:
                    break;
            }
        }
        else {
            player.sendMessage(ChatColor.RED + "You do not have enough gold to barter.");
            return;
        }

        this.setItemCount(player, Math.max(this.getItemCount(player) - cost, 1));
        player.sendMessage(ChatColor.GRAY + "You have bartered " + ChatColor.GOLD + cost + " gold " + ChatColor.GRAY + "for " + ChatColor.GOLD + itemString);
    }
}

//package chalkinshmeal.labyrinth.artifacts.items.piglin;
//
//        import chalkinshmeal.labyrinth.artifacts.game.Game;
//        import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
//        import chalkinshmeal.labyrinth.data.ConfigHandler;
//        import org.bukkit.ChatColor;
//        import org.bukkit.Material;
//        import org.bukkit.entity.Arrow;
//        import org.bukkit.entity.Entity;
//        import org.bukkit.entity.Player;
//        import org.bukkit.event.entity.EntityDamageByEntityEvent;
//        import org.bukkit.event.player.PlayerInteractEvent;
//        import org.bukkit.inventory.ItemStack;
//        import org.bukkit.plugin.java.JavaPlugin;
//
//        import java.util.ArrayList;
//        import java.util.Arrays;
//        import java.util.List;
//
//        import static chalkinshmeal.labyrinth.utils.Utils.getRandNum;
//        import static org.bukkit.Material.GOLDEN_APPLE;
//
//class BarterItem {
//    int cost;
//    String displayName;
//    Material material;
//    int amount;
//
//    public BarterItem(int cost, String displayName, Material material, int amount) {
//        this.cost = cost;
//        this.displayName = displayName;
//        this.material = material;
//        this.amount = amount;
//    }
//
//    public ItemStack getItemStack() {
//        return new ItemStack(material, amount);
//    }
//}
//
//class BarterItemHandler {
//    List<BarterItem> barterItems = Arrays.asList(
//            new BarterItem(10, ChatColor.GREEN + "a Golden Apple", GOLDEN_APPLE, 1)
//    );
//
//    public void giveRandBarterItem(Player player, int cost) {
//        List<BarterItem> eligibleBarterItems = this.getEligibleBarterItems(cost);
//        if (eligibleBarterItems.size() == 0) {
//            player.sendMessage(ChatColor.RED + "You do not have enough gold to barter.");
//            return;
//        }
//
//        int randIndex = getRandNum(0, eligibleBarterItems.size() - 1);
//        BarterItem chosenItem = eligibleBarterItems.get(randIndex);
//        player.getInventory().addItem(chosenItem.getItemStack());
//        player.sendMessage(ChatColor.GOLD + "You have bartered for " + ChatColor.GREEN + chosenItem.displayName);
//    }
//
//    private List<BarterItem> getEligibleBarterItems(int cost) {
//        List<BarterItem> eligibleItems = new ArrayList<>();
//        for (BarterItem item : this.barterItems) {
//            if (cost >= item.cost) {
//                eligibleItems.add(item);
//            }
//        }
//        return eligibleItems;
//    }
//}
//
//public class PiglinBarter extends LabyrinthItem {
//    BarterItemHandler barterItemHandler;
//    public PiglinBarter(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
//        super(plugin, config, className, position, game);
//        this.barterItemHandler = new BarterItemHandler();
//    }
//
//    // Increase by 1 if hit or hit someone
//    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
//        Entity attacker = event.getDamager(); Entity victimEntity = event.getEntity();
//        if (isPlayerHoldingItemInInventory(victimEntity)) this.changeItemCount(victimEntity, 1);
//        if (isPlayerHoldingItemInInventory(attacker)) this.changeItemCount(attacker, 1);
//        if (attacker instanceof Arrow arrow) {
//            Entity shooter = (Entity) arrow.getShooter();
//            if (isPlayerHoldingItemInInventory(shooter)) this.changeItemCount(shooter, 1);
//        }
//    }
//
//    public void onPlayerInteractEvent(PlayerInteractEvent event) {
//        Player player = event.getPlayer();
//        if (!isPlayerHoldingItemInMainHand(player)) return;
//        if (!isRightClick(event)) return;
//
//        // Reset/Record state
//        event.setCancelled(true);
//
//        // Barter
//        int goldAmount = this.getItemCount(player);
//        barterItemHandler.giveRandBarterItem(player, goldAmount);
//    }
//}

