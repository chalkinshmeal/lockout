package chalkinshmeal.labyrinth.artifacts.items.squid;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.giveEffect;
import static chalkinshmeal.labyrinth.utils.Utils.isAPlayer;
import static org.bukkit.potion.PotionEffectType.BLINDNESS;

public class SquidInkSac extends LabyrinthItem {
    public SquidInkSac(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victim = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isPlayerHoldingItemInMainHand(player)) return;

        // Give blindness
        giveEffect(victim, BLINDNESS, 1.5F, 2);
    }
}
