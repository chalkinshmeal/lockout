package chalkinshmeal.labyrinth.artifacts.items.chorus_flower;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.CHORUS_FRUIT;

public class ChorusFlowerFruit extends LabyrinthItem {
    public final int chance = 4;
    public ChorusFlowerFruit(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victim = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (getRandNum(0, chance-1) == 0) return;

        // Cosmetics
        playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT);

        // Calculate new random location
        int dx = getRandNum(-2, 2);
        int dz = getRandNum(-2, 2);
        int dx2 = getRandNum(-2, 2);
        int dz2 = getRandNum(-2, 2);
        victim.teleport(victim.getLocation().add(dx, 0, dz));
        player.teleport(victim.getLocation().add(dx2, 0, dz2));

        // Give chorus fruit, in case they need to escape
        if (victim instanceof Player)
            ((Player) victim).getInventory().addItem(new ItemStack(CHORUS_FRUIT, 3));
    }
}
