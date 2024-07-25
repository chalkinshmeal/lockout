package chalkinshmeal.labyrinth.artifacts.items.zombie;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.POISON;
import static org.bukkit.potion.PotionEffectType.WEAKNESS;

public class ZombieBite extends LabyrinthItem {
    public ZombieBite(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victimEntity = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isAPlayer(victimEntity)) return; Player victim = (Player) victimEntity;
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        resetItemCooldown(player);

        // Bite
        giveEffect(victim, POISON, 3, 3);
        giveEffect(victim, WEAKNESS, 3, 2);
        addHealth(player, 3);

        if (victim instanceof Player)
            victim.sendMessage(ChatColor.AQUA + "You have been bitten! You feel weak.");
    }
}
