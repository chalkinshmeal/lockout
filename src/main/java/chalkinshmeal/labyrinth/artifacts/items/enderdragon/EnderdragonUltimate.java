package chalkinshmeal.labyrinth.artifacts.items.enderdragon;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class EnderdragonUltimate extends LabyrinthItem {
    Map<UUID, List<Entity>> endercrystals;
    public EnderdragonUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
        this.endercrystals = new HashMap<>();
    }

    public Map<UUID, List<Entity>> getEnderCrystals() { return this.endercrystals; }

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
        broadcastGameMsg(ChatColor.GOLD + player.getName() + ChatColor.DARK_PURPLE + " has been reborn...");

        // Get 5 random locations within the map
        int numCrystals = 5;
        List<Entity> enderCrystals = new ArrayList<>();
        for (int i = 0; i < numCrystals; i++) {
            Location randLoc = this.game.getLabyrinth().getRandSurfaceLocation();
            EnderCrystal enderCrystal = (EnderCrystal) player.getWorld().spawnEntity(randLoc, EntityType.END_CRYSTAL);
            enderCrystal.setBeamTarget(player.getLocation());
            enderCrystals.add(enderCrystal);
        }
        this.game.addPlayerSummons(player, enderCrystals);
        this.endercrystals.put(player.getUniqueId(), enderCrystals);
        startEnderDragonCrystalTrackTask(this, player);
    }

    public static void startEnderDragonCrystalTrackTask(EnderdragonUltimate labyrinthItem, Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<Entity> crystals = labyrinthItem.getEnderCrystals().getOrDefault(player.getUniqueId(), new ArrayList<>());
                boolean isOneAlive = false;
                for (Entity crystal : crystals) {
                    if (!crystal.isDead()) {
                        ((EnderCrystal) crystal).setBeamTarget(player.getLocation().subtract(0, 2, 0));
                        addHealth(player, 1);
                        isOneAlive = true;
                    }
                }

                // End condition
                if (!isOneAlive) { this.cancel(); }
            }
        }.runTaskTimer(labyrinthItem.getPlugin(), 20L*0L, 20L*1L);
    }
}
