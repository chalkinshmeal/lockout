package chalkinshmeal.labyrinth.artifacts.items.horse;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static chalkinshmeal.labyrinth.utils.Utils.isAirBetweenPoints;

public class HorseLasso extends LabyrinthItem {
    public HorseLasso(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);

        Vector playerLookDir = player.getEyeLocation().getDirection();
        Vector playerEyeLoc = player.getEyeLocation().toVector();
        for (Entity entity : player.getNearbyEntities(20, 20, 20)) {
            if (!isAirBetweenPoints(entity.getLocation(), player.getEyeLocation())) continue;
            Vector playerEntityVec = entity.getLocation().toVector().subtract(playerEyeLoc);
            float angle = playerLookDir.angle(playerEntityVec);
            float angle2 = playerLookDir.angle(playerEntityVec.add(new Vector(0, 1, 0)));
            if (angle <= 0.2f || angle2 <= 0.2f) {
                entity.setVelocity(entity.getVelocity().add(playerEntityVec.normalize().multiply(4).setY(0.8)));
                resetItemCooldown(player);
                if (entity instanceof Player)
                    entity.sendMessage(ChatColor.DARK_AQUA + "You have been lassoed!");
            }
        }
    }
}
