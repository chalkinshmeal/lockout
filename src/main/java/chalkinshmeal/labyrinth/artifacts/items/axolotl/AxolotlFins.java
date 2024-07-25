package chalkinshmeal.labyrinth.artifacts.items.axolotl;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Sound.ENTITY_COD_DEATH;
import static org.bukkit.potion.PotionEffectType.*;

public class AxolotlFins extends LabyrinthItem {
    public AxolotlFins(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victim = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isPlayerHoldingItemInMainHand(player)) return;

        // Give entity bubbles
        addBubbles((LivingEntity) victim, 2);

        // If >=4 bubbles, give weakness + slowness
        if (getBubbles((LivingEntity) victim) >= 4) {
            if (victim instanceof Player && !((LivingEntity) victim).hasPotionEffect(SLOWNESS)) {
                victim.sendMessage(ChatColor.LIGHT_PURPLE + "You struggle to breathe!");
                player.sendMessage(ChatColor.LIGHT_PURPLE + "You opponent struggles to breathe!");
                giveEffect(victim, SLOWNESS, 10, 1);
                giveEffect(victim, WEAKNESS, 10, 1);
                playSound(player, ENTITY_COD_DEATH);
            }
        }

        // If >=8 bubbles, give extra damage
        if (getBubbles((LivingEntity) victim) >= 8) {
            if (victim instanceof Player) {
                player.sendMessage(ChatColor.LIGHT_PURPLE + "You opponent seems extra weak!");
                victim.sendMessage(ChatColor.LIGHT_PURPLE + "The air stings to breathe!");
                subtractHealth((LivingEntity) victim, 4);
                giveEffect(victim, GLOWING, 1, 1);
            }
        }

        // Give entity bubbles
        addBubbles((LivingEntity) attacker, 2);
    }
}
