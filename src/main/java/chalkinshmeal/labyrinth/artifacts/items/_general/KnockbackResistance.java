package chalkinshmeal.labyrinth.artifacts.items._general;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.getChestplate;
import static chalkinshmeal.labyrinth.utils.Utils.setAttribute;

public class KnockbackResistance extends LabyrinthItem {
    public KnockbackResistance(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (!isPlayerHoldingItemInInventory(event.getEntity())) return;

        // Add knockback resistance - should probably be in another function, if used again
        Player player = (Player) event.getEntity();
        ItemStack item = getChestplate(player);
        setAttribute(item, Attribute.GENERIC_KNOCKBACK_RESISTANCE, "generic.knockbackResistance", 0.5F);
    }
}
