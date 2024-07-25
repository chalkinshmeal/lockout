package chalkinshmeal.labyrinth.artifacts.items.ocelot;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.addHealth;
import static chalkinshmeal.labyrinth.utils.Utils.playSound;

public class OcelotEdibleFish extends LabyrinthItem {
    public OcelotEdibleFish(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer(); Entity entity = event.getRightClicked();
        if (!isPlayerHoldingItemInInventory(player)) return;

        // Reset/Record state
        event.setCancelled(true);

        // Heal
        if (entity.getType().equals(EntityType.TROPICAL_FISH) ||
                entity.getType().equals(EntityType.COD) ||
                entity.getType().equals(EntityType.SALMON) ||
                entity.getType().equals(EntityType.FROG) ||
                entity.getType().equals(EntityType.SQUID) ||
                entity.getType().equals(EntityType.GLOW_SQUID)) {
            addHealth(player, 5);
            playSound(player, Sound.ENTITY_GENERIC_EAT);
            entity.remove();
        }
    }
}
