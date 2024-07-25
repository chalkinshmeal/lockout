package chalkinshmeal.labyrinth.artifacts.items.drowned;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class DrownedRiptide extends LabyrinthItem {
    public DrownedRiptide(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victim = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!((Player)event.getDamager()).isRiptiding()) return;
        if (!isItemCooldownReady(player)) return;

        // Reset State
        resetItemCooldown(player);

        // Hurt and knockback victim
        subtractHealth((LivingEntity) victim, 2);
        Vector v = getVectorFromPlayerToEntity(player, victim);
        victim.setVelocity(v.multiply(2).setY(1.5));
    }
}
