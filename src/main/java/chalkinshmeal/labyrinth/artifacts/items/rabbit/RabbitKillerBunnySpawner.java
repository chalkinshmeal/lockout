package chalkinshmeal.labyrinth.artifacts.items.rabbit;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.giveEffect;
import static chalkinshmeal.labyrinth.utils.Utils.useHeldItem;
import static org.bukkit.potion.PotionEffectType.JUMP_BOOST;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class RabbitKillerBunnySpawner extends LabyrinthItem {
    public RabbitKillerBunnySpawner(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
    }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        useHeldItem(player);

        // Spawn killer rabbit
        Rabbit killerBunny = (Rabbit) player.getWorld().spawnEntity(player.getLocation(), EntityType.RABBIT);
        killerBunny.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);
        giveEffect(killerBunny, SPEED, 100000, 15);
        giveEffect(killerBunny, JUMP_BOOST, 100000, 2);
        this.game.addPlayerSummons(player, List.of(killerBunny));
    }
}
