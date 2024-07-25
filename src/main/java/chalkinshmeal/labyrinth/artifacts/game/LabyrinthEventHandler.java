package chalkinshmeal.labyrinth.artifacts.game;

import chalkinshmeal.labyrinth.artifacts.cooldown.CooldownHandler;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.FALL;

public class LabyrinthEventHandler {
    private final JavaPlugin plugin;
    private final CooldownHandler cooldownHandler;
    private final Game game;

    public LabyrinthEventHandler(JavaPlugin plugin, Game game, CooldownHandler cooldownHandler) {
        this.plugin = plugin;
        this.cooldownHandler = cooldownHandler;
        this.game = game;
    }

    // -------------------------------------------------------------------------------------------
    // Manipulators
    // -------------------------------------------------------------------------------------------
    public JavaPlugin getPlugin() { return this.plugin; }

    // -------------------------------------------------------------------------------------------
    // Iterators for all classes' event listeners - purely for listeners!
    // -------------------------------------------------------------------------------------------
    public void onBlockBreakEvent(BlockBreakEvent event) { this.preventBreakBlock(event); }
    public void onBlockExplodeEvent(BlockExplodeEvent event) { this.preventBlockExplode(event); }
    public void onBlockIgniteEvent(BlockIgniteEvent event) { this.preventBlockIgnite(event); }
    public void onBlockPlaceEvent(BlockPlaceEvent event) { this.preventBlockPlace(event); }
    public void onEntityAirChangeEvent(EntityAirChangeEvent event) { this.preventAirRecovery(event); }
    public void onEntityCombustEvent(EntityCombustEvent event) { this.preventEntityCombust(event); }
    public void onEntityDamageEvent(EntityDamageEvent event) {
        this.preventImmunityDamage(event);
        this.preventFallDamage(event);
        this.killOnVoidDamage(event);
    }
    public void onEntityDeathEvent(EntityDeathEvent event) {
        this.preventEntityDropItems(event);
        this.removeFromSummons(event);
    }
    public void onEntityExplodeEvent(EntityExplodeEvent event) { this.preventEntityExplodeBlock(event); }
    public void onEntityTargetEvent(EntityTargetEvent event) { this.preventSummonBetrayal(event); }
    public void onInventoryOpenEvent(InventoryOpenEvent event) { this.preventAnvilOpen(event); }
    public void onPlayerInteractEvent(PlayerInteractEvent event) {}
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) { this.preventItemDamage(event); }
    public void onPlayerLeashEntityEvent(PlayerLeashEntityEvent event) { this.preventLead(event); }
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        this.freezePlayer(event);
        this.setFlight(event);
        this.setSummonTargets();
    }

    // -------------------------------------------------------------------------------------------
    // Event utilities
    // -------------------------------------------------------------------------------------------
    public void preventAirRecovery(EntityAirChangeEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;
        if (!this.game.isPlayerInPlay(player)) return;

        // Count bubbles down rather than up over time
        if (this.cooldownHandler.isCooldownReady("regen-air", player)) {
            this.cooldownHandler.resetCooldown("regen-air", player);
            subtractBubbles(player, 1);
        }

        // Prevent passive regen by just not allowing the normal increment
        if (event.getAmount() % 10 != 0) {
            event.setCancelled(true);
        }
    }
    public void preventAnvilOpen(InventoryOpenEvent event) {
        if (event.getInventory() instanceof AnvilInventory) event.setCancelled(true);
    }
    public void preventBlockPlace(BlockPlaceEvent event) {
        if (!this.game.isPlayerInGame(event.getPlayer())) return;
        event.setCancelled(true);
    }
    public void preventBlockIgnite(BlockIgniteEvent event) { event.setCancelled(true); }
    public void preventBreakBlock(BlockBreakEvent event) {
        if (!(this.game.isLocationInLabyrinth(event.getBlock().getLocation()))) return;
        if (!this.game.isPlayerInGame(event.getPlayer())) return;
        event.setCancelled(true);
    }
    public void preventBlockExplode(BlockExplodeEvent event) {
        if (!(this.game.isLocationInLabyrinth(event.getBlock().getLocation()))) return;
        event.setCancelled(true);
    }
    public void preventEntityCombust(EntityCombustEvent event) {
        if (event instanceof EntityCombustByBlockEvent) return;
        if (event instanceof EntityCombustByEntityEvent) return;
        event.setCancelled(true);
    }
    public void preventEntityDropItems(EntityDeathEvent event) { event.getDrops().clear(); }
    public void preventEntityExplodeBlock(EntityExplodeEvent event) {
        if (!(this.game.isLocationInLabyrinth(event.getEntity().getLocation()))) return;
        event.blockList().clear(); // Clear block list to break
    }
    public void preventFallDamage(EntityDamageEvent event) {
        EntityDamageEvent.DamageCause cause = event.getCause();
        if (!(cause.equals(FALL))) return;

        event.setCancelled(true);
    }
    public void preventItemDamage(PlayerItemDamageEvent event) {
        if (event.getItem().getDurability() == 0) {
            event.setCancelled(true);
        }
    }
    public void preventLead(PlayerLeashEntityEvent event) {
        event.setCancelled(true);
        event.getPlayer().updateInventory();
    }
    public void preventImmunityDamage(EntityDamageEvent event) {
        if (!isAPlayer(event.getEntity())) return; Player player = (Player) event.getEntity();
        if (!this.game.isPlayerInPlay(player)) return;
        if (this.cooldownHandler.isCooldownReady("on-death-immunity", player)) return;

        // Cancel the event, but change yaw + pitch
        event.setCancelled(true);

    }
    public void preventSummonBetrayal(EntityTargetEvent event) {
        Entity e = event.getEntity(); Entity t = event.getTarget();

        // Prevent summons from targeting summoner
        if (t == null) return;
        for (Map.Entry<UUID, List<Entity>> entry : this.game.getSummonedEntities().entrySet()) {
            UUID uuid = entry.getKey();
            List<Entity> entities = entry.getValue();
            if (entities.contains(e) && t.equals(this.plugin.getServer().getPlayer(uuid))) event.setCancelled(true);
        }
    }
    public void setSummonTargets() {
        for (UUID uuid : this.game.getInGamePlayerUUIDs()) {
            Player summoner = this.plugin.getServer().getPlayer(uuid);
            if (summoner == null) continue;
            if (!this.game.getSummonedEntities().containsKey(summoner.getUniqueId())) return;
            List<Entity> playerSummons = this.game.getSummonedEntities().get(summoner.getUniqueId());

            for (Entity summon : playerSummons) {
                // ALlay class logic
                // Making an exception for allays - in the future, this should be a SummonManager
                LivingEntity target = this.game.getNearestEntityToSummon(summoner, summon);
                if (summon instanceof Allay) {
                    ((Allay) summon).setTarget(summoner);
                }

                // Fox class logic
                //else if (summon instanceof Fox) {
                //    Fox fox = (Fox) summon;
                //    if (fox.getFoxType().equals(SNOW)) {
                //        ItemStack foxHeldItem = fox.getEquipment().getItemInMainHand();
                //        if (!foxHeldItem.equals(Material.AIR)) {
                //            fox.setTarget(summoner);
                //        }
                //        else {
                //            fox.setTarget(target);
                //        }
                //    }
                //}
                else if (target != null && !playerSummons.contains(target) && summon instanceof Mob summonedMob) {
                    summonedMob.setTarget(target);
                }

            }
        }
    }
    public void freezePlayer(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!this.game.isPlayerInPlay(player)) return;
        if (this.cooldownHandler.isCooldownReady("on-death-freeze", player)) return;

        // Cancel the event, but change yaw + pitch
        event.setCancelled(true);
        player.setAllowFlight(true);
        Location from = event.getFrom();
        Location to = event.getTo();
        from.setPitch(to.getPitch());
        from.setYaw(to.getYaw());
    }
    public void setFlight(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!this.game.isPlayerInPlay(player)) return;
        boolean isFlightReady = cooldownHandler.isCooldownReady("double-jump", player);
        player.setAllowFlight(isFlightReady);
    }
    public void killOnVoidDamage(EntityDamageEvent event) {
        if (!isLiving(event.getEntity())) return;
        if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID))
            event.setDamage(100);
    }
    public void removeFromSummons(EntityDeathEvent event) {
        Entity e = event.getEntity();
        for (Map.Entry<UUID, List<Entity>> entry : this.game.getSummonedEntities().entrySet()) {
            UUID uuid = entry.getKey();
            List<Entity> entities = entry.getValue();
            if (entities.contains(e)) {
                this.game.getSummonedEntities().get(uuid).remove(e); return;
            }
        }

    }
}