package chalkinshmeal.labyrinth.artifacts.items.snow_golem;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.SLOWNESS;

public class SnowGolemStick extends LabyrinthItem {
    public SnowGolemStick(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity deadOrAliveVictim = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isLiving(deadOrAliveVictim)) return; LivingEntity victim = (LivingEntity) event.getEntity();
        if (!isPlayerHoldingItemInMainHand(player)) return;

        if (isUltimateActive(player)) addFreeze(victim, 200);
        else addFreeze(victim, 6);

        if (victim.getFreezeTicks() == victim.getMaxFreezeTicks() && !victim.isFreezeTickingLocked()) {
            if (victim instanceof Player) {
                victim.sendMessage(ChatColor.AQUA + "You feel... cold...");
                startGolemHitFreezeTask(this.game, (Player) victim, isUltimateActive(player));
            }
            else {
                startGolemHitFreezeTask(this.game, (LivingEntity) victim, isUltimateActive(player));
            }
            attacker.sendMessage(ChatColor.AQUA + "You opponent is freezing!");
        }

        if (isUltimateActive(player)) {
            List<Location> locs = new ArrayList<>(List.of(
                    victim.getLocation(),
                    victim.getLocation().add(0, 1, 0),
                    victim.getLocation().add(0, 2, 0),
                    victim.getLocation().add(0, -1, 0)
            ));
            List<Location> airLocs = new ArrayList<>();
            for (Location loc : locs) {
                if (loc.getBlock().getType().isAir() || loc.getBlock().getType().equals(Material.SNOW)) {
                    loc.getBlock().setType(Material.ICE);
                    airLocs.add(loc);
                }
            }
            giveEffect(victim, SLOWNESS, 5, 100);
            victim.teleport(new Location(victim.getWorld(), ((int) locs.get(0).getX())+0.5, ((int) locs.get(0).getY()), ((int) locs.get(0).getZ())+0.5));
            victim.setVelocity(new Vector(0, 0, 0));
            startSnowGolemUltimateRemoveIceTask(this.game, airLocs);
            playSound(victim.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL);
            if (victim instanceof Player)
                ((Player) victim).sendMessage(ChatColor.AQUA + "You have been frozen!");
            event.setCancelled(true);
        }
    }
    public void startGolemHitFreezeTask(Game game, Player ent, boolean isUltimateActive) {
        new BukkitRunnable() {
            float freezeHitsLeft = 4;
            int currentLivesLeft = game.getScoreboard().getLives((Player) ent);

            @Override
            public void run() {
                // Check if this player is still alive
                if (ent instanceof Player) {
                    int livesLeft = game.getScoreboard().getLives((Player) ent);
                    if (livesLeft != currentLivesLeft) {
                        ent.lockFreezeTicks(false);
                        this.cancel();
                    }
                }

                // Ultimate properties
                ent.lockFreezeTicks(true);
                if (isUltimateActive) ent.damage(3);
                else ent.damage(1);

                // End condition
                freezeHitsLeft -= 1;
                if (freezeHitsLeft < 0) {
                    ent.lockFreezeTicks(false);
                    this.cancel();
                }
            }
        }.runTaskTimer(this.plugin, 20L * 0L, 20L * 2L);
    }
    public void startGolemHitFreezeTask(Game game, LivingEntity ent, boolean isUltimateActive) {
        new BukkitRunnable() {
            float freezeHitsLeft = 4;

            @Override
            public void run() {
                // Ultimate properties
                ent.lockFreezeTicks(true);
                if (isUltimateActive) ent.damage(3);
                else ent.damage(1);

                // End condition
                freezeHitsLeft -= 1;
                if (freezeHitsLeft < 0) {
                    ent.lockFreezeTicks(false);
                    this.cancel();
                }
            }
        }.runTaskTimer(this.plugin, 20L * 0L, 20L * 2L);
    }
    public void startSnowGolemUltimateRemoveIceTask(Game game, List<Location> locations) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Location loc : locations) {
                    loc.getBlock().setType(Material.AIR);
                }
            }
        }.runTaskLater(this.plugin, 20L*5L);
    }
}
