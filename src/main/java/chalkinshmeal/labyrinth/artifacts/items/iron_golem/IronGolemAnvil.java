package chalkinshmeal.labyrinth.artifacts.items.iron_golem;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.AIR;
import static org.bukkit.Material.ANVIL;

public class IronGolemAnvil extends LabyrinthItem {
    Map<UUID, Integer> anvilTaskIDs;
    Player dummyPlayer;
    public IronGolemAnvil(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
        this.anvilTaskIDs = new HashMap<>();
        this.dummyPlayer = null;
    }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        resetItemCooldown(player);
        this.dummyPlayer = player;

        // Create anvil
        FallingBlock anvil = player.getWorld().spawnFallingBlock(player.getEyeLocation(), ANVIL.createBlockData());
        anvil.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(2));
        anvil.setPersistent(false);
        anvil.setDropItem(false);
        anvil.setHurtEntities(true);
        anvil.setFallDistance(20);

        // Start anvil checking hit task
        this.anvilTaskIDs.put(player.getUniqueId(), ironGolemAnvilTask(this.game, player, anvil));
    }
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
        if (!event.getEntityType().equals(EntityType.FALLING_BLOCK)) return;
        if (!event.getTo().equals(ANVIL)) return;

        this.game.addPlayerSummons(this.dummyPlayer, event.getBlock(), AIR);
        //if (this.anvilTaskID != -1) this.plugin.getServer().getScheduler().cancelTask(this.anvilTaskID);
        //this.anvilTaskID = -1;
    }

    public int ironGolemAnvilTask(Game game, Player player, FallingBlock anvil) {
        BukkitTask task = new BukkitRunnable() {
            float secs = 5;
            float ticksLeft = secs*20;
            @Override
            public void run() {
                // Hit condition
                for (Entity e : anvil.getNearbyEntities(1, 1, 1)) {
                    if (e.equals(player)) continue;
                    if (!(e instanceof LivingEntity)) return;
                    anvil.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, anvil.getLocation(), 30);

                    Vector knockback = e.getLocation().toVector().subtract(anvil.getLocation().toVector());
                    e.setVelocity(e.getVelocity().add(knockback.normalize().multiply(2.5).setY(0.8)));
                    playSound(anvil.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT);

                    subtractHealth((LivingEntity) e, 6);
                    anvil.remove();
                    this.cancel();
                }

                // End condition
                ticksLeft -= 1;
                if (ticksLeft < 0) { this.cancel(); }
            }
        }.runTaskTimer(this.plugin, 20L*0L, 1L);

        return task.getTaskId();
    }
}
