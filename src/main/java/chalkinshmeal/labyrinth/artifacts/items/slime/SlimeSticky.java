package chalkinshmeal.labyrinth.artifacts.items.slime;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.AIR;

public class SlimeSticky extends LabyrinthItem {
    private final int chance = 20; // 1/20 chance
    public SlimeSticky(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity deadOrAliveAttacker = event.getDamager(); Entity victimEntity = event.getEntity();
        if (!isAPlayer(victimEntity)) return; Player victim = (Player) victimEntity;
        if (!isEntityAHumanEntity(deadOrAliveAttacker)) return; HumanEntity attacker = (HumanEntity) deadOrAliveAttacker;
        if (!isPlayerHoldingItemInInventory(victim)) return;
        if (getRandNum(0, chance-1) != 0) return;
        ItemStack item = attacker.getEquipment().getItemInMainHand();
        if (item == null || item.getType().equals(AIR)) return;

        // Cosmetics
        if (isAPlayer(attacker))
            attacker.sendMessage(ChatColor.GREEN + "Your item got stuck in " + ChatColor.GOLD + victim + "'s body!");
        victim.sendMessage(ChatColor.GREEN + "Your have disarmed your opponent!");
        playSound(victim, Sound.ENTITY_CHICKEN_EGG);

        // Disarm opponent
        attacker.getEquipment().setItemInMainHand(new ItemStack(AIR));
        attacker.getWorld().dropItem(attacker.getLocation(), item);
    }
}
