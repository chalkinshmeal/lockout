package chalkinshmeal.labyrinth.artifacts.items._general;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.isAPlayer;

public class LightningImmunity extends LabyrinthItem {
    public LightningImmunity(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victimEntity = event.getEntity();
        if (!isAPlayer(victimEntity)) return; Player victim = (Player) victimEntity;
        if (!(attacker instanceof LightningStrike)) return;
        if (!isPlayerHoldingItemInInventory(victim)) return;

        event.setCancelled(true);
    }
}
