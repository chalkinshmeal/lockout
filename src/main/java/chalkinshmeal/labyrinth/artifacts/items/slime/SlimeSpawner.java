package chalkinshmeal.labyrinth.artifacts.items.slime;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.JUMP_BOOST;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class SlimeSpawner extends LabyrinthItem {
    public SlimeSpawner(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
    }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victimEntity = event.getEntity();
        if (!isAPlayer(victimEntity)) return; Player victim = (Player) victimEntity;
        if (!isPlayerHoldingItemInInventory(victim)) return;

        // Immunity to slime attacks
        if (attacker instanceof Slime) { event.setCancelled(true); return; }

        // Spawn slime
        Slime s = (Slime) victim.getWorld().spawnEntity(victim.getLocation(), EntityType.SLIME);
        giveEffect(s, SPEED, 100000, 3);
        giveEffect(s, JUMP_BOOST, 100000, 3);
        s.setSize(0);
        if (getRandNum(0, 10) == 0)
            s.setSize(2);
        this.game.addPlayerSummons(victim, List.of(s));
    }

    public void onSlimeSplitEvent(SlimeSplitEvent event) {
        event.setCancelled(true);
    }
}
