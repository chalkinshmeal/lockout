package chalkinshmeal.labyrinth.artifacts.items.bat;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.Particles;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class BatFangs extends LabyrinthItem {
    public BatFangs(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
    }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victimEntity = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isLiving(victimEntity)) return; LivingEntity victim = (LivingEntity) victimEntity;
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (player.getHealthScale() == 1) return;

        player.sendMessage("Hit a player, gaining a health");

        addHealth(player, 1);

        // Cosmetics
        playSound(player, Sound.ENTITY_PANDA_BITE, 0.5F);
        Particles.spawnParticle(victim.getEyeLocation(), Particle.CRIMSON_SPORE, 20);
        Particles.spawnParticle(player.getEyeLocation(), Particle.HEART, 1);
    }
}
