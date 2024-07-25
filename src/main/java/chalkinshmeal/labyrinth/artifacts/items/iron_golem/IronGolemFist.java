package chalkinshmeal.labyrinth.artifacts.items.iron_golem;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static chalkinshmeal.labyrinth.utils.Utils.isAPlayer;
import static chalkinshmeal.labyrinth.utils.Utils.playSound;

public class IronGolemFist extends LabyrinthItem {
    public IronGolemFist(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victim = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isPlayerHoldingItemInMainHand(player)) return;

        // Cosmetics
        playSound(victim.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.5F);
        playSound(victim.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5F);
        victim.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, victim.getLocation(), 30);

        // Throw
        victim.setVelocity(new Vector(victim.getVelocity().getX(), 1.5, victim.getVelocity().getZ()));
    }
}
