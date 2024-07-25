package chalkinshmeal.labyrinth.artifacts.items._general;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import static chalkinshmeal.labyrinth.utils.Utils.isAPlayer;

public class WitherProtection extends LabyrinthItem {
    public WitherProtection(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityPotionEffectEvent(EntityPotionEffectEvent event) {
        if (!isAPlayer(event.getEntity())) return; Player player = (Player) event.getEntity();
        if (!isPlayerHoldingItemInInventory(player)) return;

        // Check if attacker is either a potion of wither or a cloud of wither
        if (!event.getModifiedType().equals(PotionEffectType.WITHER)) return;

        event.setCancelled(true);
    }
}
