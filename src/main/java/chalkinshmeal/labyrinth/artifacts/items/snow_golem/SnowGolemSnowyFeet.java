package chalkinshmeal.labyrinth.artifacts.items.snow_golem;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.Material.AIR;
import static org.bukkit.Material.SNOW;

public class SnowGolemSnowyFeet extends LabyrinthItem {
    Map<UUID, Block> beacons;
    public SnowGolemSnowyFeet(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
        this.beacons = new HashMap<>();
    }

    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInInventory(player)) return;

        Location loc = player.getLocation();
        Location oneUnder = player.getLocation().subtract(0, 1, 0);
        if (loc.getBlock().getType().isAir() && !oneUnder.getBlock().getType().isAir() && !oneUnder.getBlock().getType().equals(SNOW)) {
            loc.getBlock().setType(SNOW);
            this.game.addPlayerSummons(player, loc.getBlock(), AIR);
        }
    }
}
