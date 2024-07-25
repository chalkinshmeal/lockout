package chalkinshmeal.labyrinth.artifacts.items.bee;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.isDamagerACloud;
import static org.bukkit.potion.PotionEffectType.*;

public class BeeHoneyImmunity extends LabyrinthItem {
    public BeeHoneyImmunity(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityPotionEffectEvent(EntityPotionEffectEvent event) {
        if (!isDamagerACloud(event)) return;
        if (!List.of(SLOWNESS, WEAKNESS, POISON).contains(event.getModifiedType())) return;
        if (!isPlayerHoldingItemInInventory(event.getEntity())) return;

        // Reset State
        event.setCancelled(true);
    }
}
