package chalkinshmeal.labyrinth.artifacts.items.enderdragon;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.INSTANT_DAMAGE;

public class EnderdragonImmunity extends LabyrinthItem {
    public EnderdragonImmunity(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victimEntity = event.getEntity();
        if (!isAPlayer(victimEntity)) return; Player victim = (Player) victimEntity;
        if (!isDamagerACloud(event)) return;
        if (!doesCloudHaveEffects(event.getDamager(), List.of(INSTANT_DAMAGE))) return;
        if (!isPlayerHoldingItemInInventory(victim)) return;

        // Avoid attack
        event.setCancelled(true);
    }
}
