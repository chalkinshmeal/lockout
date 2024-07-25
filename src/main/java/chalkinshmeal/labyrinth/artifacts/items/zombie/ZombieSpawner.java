package chalkinshmeal.labyrinth.artifacts.items.zombie;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class ZombieSpawner extends LabyrinthItem {
    private final int chance = 2; // 1/3 chance to evade
    public ZombieSpawner(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victimEntity = event.getEntity();
        if (!isAPlayer(victimEntity)) return; Player victim = (Player) victimEntity;
        if (!isPlayerHoldingItemInInventory(victim)) return;
        if (getRandNum(0, chance) != 0) return;

        // Spawn zombie
        Zombie z = (Zombie) victim.getWorld().spawnEntity(victim.getLocation(), EntityType.ZOMBIE);
        giveEffect(z, SPEED, 100000, 1);
        this.game.addPlayerSummons(victim, List.of(z));
    }
}
