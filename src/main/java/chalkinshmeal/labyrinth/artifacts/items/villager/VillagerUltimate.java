package chalkinshmeal.labyrinth.artifacts.items.villager;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.*;

public class VillagerUltimate extends LabyrinthItem {
    public VillagerUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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
        broadcastGameMsg(ChatColor.AQUA + "The village has called upon its hero... ");

        // Give player knight items
        LabyrinthItem ironBlock = this.game.getLabyrinthItemHandler().getItem(38);

        player.getInventory().setItemInOffHand(ironBlock.getItemStack());
        player.getInventory().setItem(0, ironBlock.getItemStack());
        setHelmet(player, CHAINMAIL_HELMET);
        setChestplate(player, CHAINMAIL_CHESTPLATE);
        setLeggings(player, CHAINMAIL_LEGGINGS);
        setBoots(player, CHAINMAIL_BOOTS);
    }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victim = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isUltimateActive(player)) return;

        // Cosmetics
        playSound(victim.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.5F);
        playSound(victim.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5F);
        victim.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, victim.getLocation(), 30);

        // Throw
        victim.setVelocity(new Vector(victim.getVelocity().getX(), 1.5, victim.getVelocity().getZ()));
    }
}
