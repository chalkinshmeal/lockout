package chalkinshmeal.labyrinth.artifacts.items.cactus;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.Particles;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.POISON;

public class CactusGreenDye extends LabyrinthItem {
    private final int chance = 3; // 1/3 chance
    public CactusGreenDye(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity entity = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isLiving(entity)) return; LivingEntity victim = (LivingEntity) entity;
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (getRandNum(0, chance-1) == 0) return;

        // Give poison
        giveEffect(victim, POISON, 3, 2);

        // Cosmetics
        Particles.spawnParticle(victim.getEyeLocation(), Particle.GLOW_SQUID_INK, 15);
    }
}
