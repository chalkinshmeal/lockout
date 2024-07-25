package chalkinshmeal.labyrinth.artifacts.items.axolotl;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class AxolotlSteal extends LabyrinthItem {
    private final int minHealth = 8;
    public AxolotlSteal(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victimEntity = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isAPlayer(victimEntity)) return; Player victim = (Player) victimEntity;
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isItemCooldownReady(player)) return;
        // Steal if <4 hearts
        if (victim.getHealth() >= minHealth) {
            player.sendMessage(ChatColor.LIGHT_PURPLE + victim.getName() + " is not yet weak enough to steal from!");
            return;
        }

        // Reset state
        resetItemCooldown(player);

        // Steal item
        ItemStack item = removeAndGetRandItem(victim);
        if (item == null) return;

        playSound(player, Sound.ENTITY_ELDER_GUARDIAN_HURT);
        victim.sendMessage(ChatColor.LIGHT_PURPLE + player.getName() + " has stolen from you!");
        player.sendMessage(ChatColor.LIGHT_PURPLE + "You have stolen an item!");
        player.getInventory().addItem(item);
    }
}
