package chalkinshmeal.labyrinth.artifacts.items.axolotl;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class AxolotlSlick extends LabyrinthItem {
    private final int chance = 4; // 1/4 chance to evade
    public AxolotlSlick(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victimEntity = event.getEntity();
        if (!isAPlayer(victimEntity)) return; Player victim = (Player) victimEntity;
        if (!isPlayerHoldingItemInInventory(victim)) return;
        if (getRandNum(0, chance) != 0) return;

        // Cosmetics
        victim.sendMessage(ChatColor.LIGHT_PURPLE + "You evaded an attack!");
        playSound(victim, Sound.ENTITY_AXOLOTL_IDLE_AIR);
        if (attacker instanceof Player)
            attacker.sendMessage(ChatColor.GOLD + victim.getName() + " " + ChatColor.LIGHT_PURPLE + "evaded your attack!");

        // Avoid attack
        event.setCancelled(true);
    }
}
