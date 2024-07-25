package chalkinshmeal.labyrinth.artifacts.items.rabbit;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class RabbitSpawner extends LabyrinthItem {
    private final int chance = 4; // 3/4 chance to spawn
    public RabbitSpawner(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victimEntity = event.getEntity();
        if (!isAPlayer(victimEntity)) return; Player victim = (Player) victimEntity;
        if (!isPlayerHoldingItemInInventory(victim)) return;
        if (getRandNum(0, chance-1) == 0) return;

        // Spawn rabbit
        Rabbit r = (Rabbit) victim.getWorld().spawnEntity(victim.getLocation(), EntityType.RABBIT);
        giveEffect(r, SPEED, 100000, 3);
        this.game.addPlayerSummons(victim, List.of(r));
    }

    public void onEntityBreedEvent(EntityBreedEvent event) {
        LivingEntity breederEntity = event.getBreeder();
        LivingEntity father = event.getFather();
        LivingEntity mother = event.getMother();
        if (!isAPlayer(breederEntity)) return; Player breeder = (Player) breederEntity;
        if (!isPlayerHoldingItemInInventory(breeder)) return;
        if (!((father instanceof Rabbit) && (mother instanceof Rabbit))) return;

        event.setCancelled(true);

        // Kill the parents
        father.remove();
        mother.remove();

        // Set child to a killer rabbit
        Rabbit killerBunny = (Rabbit) father.getWorld().spawnEntity(father.getLocation(), EntityType.RABBIT);
        killerBunny.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
        giveEffect(killerBunny, SPEED, 100000, 10);
        this.game.addPlayerSummons(breeder, List.of(killerBunny));

        // Cosmetics
        strikeFakeLightning(killerBunny);
    }
}
