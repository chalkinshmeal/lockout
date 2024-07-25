package chalkinshmeal.labyrinth.artifacts.items.rabbit;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.Particles;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class RabbitLuckyDiamond extends LabyrinthItem {
    public final int chance = 3; // 1/3 chance to hit
    public RabbitLuckyDiamond(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victim = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isPlayerHoldingItemInInventory(player)) return;
        if (getRandNum(0, chance-1) != 0) return;

        // Triple damage
        event.setDamage(event.getDamage() * 3);

        // Cosmetics
        playSound(player, Sound.ENTITY_PLAYER_ATTACK_CRIT);
        Particles.spawnParticle(victim.getLocation(), Particle.CRIT, 1000);
        giveEffect(player, PotionEffectType.SPEED, 5, 5);
    }
}
