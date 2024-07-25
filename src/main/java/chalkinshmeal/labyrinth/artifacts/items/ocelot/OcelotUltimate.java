package chalkinshmeal.labyrinth.artifacts.items.ocelot;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class OcelotUltimate extends LabyrinthItem {
    private final int spiderCount = 10;

    public OcelotUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!doesPlayerHaveUltimate(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);
        setUsedUltimate(player);

        // Cosmetics
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.AQUA + "It's feeding time...");

        // Spawn fish
        BoundingBox bb = this.game.getBoundingBox();
        int numFish = Math.min(150, (int) (bb.getWidthX() * bb.getWidthZ() / 5));

        List<Entity> summons = new ArrayList<>();
        for (int i = 0; i < numFish; i++) {
            int x = getRandNum((int) bb.getMinX(), (int) bb.getMaxX());
            int y = (int) bb.getMaxY();
            int z = getRandNum((int) bb.getMinZ(), (int) bb.getMaxZ());
            int randFish = getRandNum(0, 5);
            EntityType eType = null;
            if (randFish == 0)  eType = EntityType.TROPICAL_FISH;
            if (randFish == 1)  eType = EntityType.COD;
            if (randFish == 2)  eType = EntityType.SALMON;
            if (randFish == 3)  eType = EntityType.FROG;
            if (randFish == 4)  eType = EntityType.SQUID;
            if (randFish == 5)  eType = EntityType.GLOW_SQUID;
            Entity spawnedFish = player.getWorld().spawnEntity(new Location(player.getWorld(), x, y, z), eType);
            ((LivingEntity) spawnedFish).setHealth(1);
            summons.add(spawnedFish);
        }
        this.game.addPlayerSummons(player, summons);
    }
}
