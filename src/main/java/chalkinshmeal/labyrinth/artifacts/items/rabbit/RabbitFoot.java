package chalkinshmeal.labyrinth.artifacts.items.rabbit;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static chalkinshmeal.labyrinth.utils.Utils.getVectorFromPlayerToEntity;
import static chalkinshmeal.labyrinth.utils.Utils.isAPlayer;

public class RabbitFoot extends LabyrinthItem {
    public RabbitFoot(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
    }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Kick self away
        float selfKickPower = 1F;
        player.setVelocity(player.getLocation().getDirection().multiply(-1 * selfKickPower));
    }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victim = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isPlayerHoldingItemInMainHand(player)) return;

        // Kick self away
        float selfKickPower = 1F;
        player.setVelocity(player.getLocation().getDirection().multiply(-1 * selfKickPower));

        // Kick entity away
        float kickPower = (float) (((player.getMaxHealth() - player.getHealth()) / player.getMaxHealth())) * 1F;
        kickPower += 1.5F;
        if (isUltimateActive(player)) kickPower *= 3;
        victim.setVelocity(player.getLocation().getDirection().multiply(kickPower));
        Vector playerEntityVec = getVectorFromPlayerToEntity(player, victim);
        victim.setVelocity(victim.getVelocity().add(playerEntityVec.normalize().multiply(kickPower).setY(0.8)));
    }
}
