package chalkinshmeal.labyrinth.artifacts.items.bee;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.*;
import static org.bukkit.enchantments.Enchantment.PROTECTION;
import static org.bukkit.potion.PotionEffectType.RESISTANCE;

public class BeeUltimate extends LabyrinthItem {
    public BeeUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victim = event.getEntity();
        if (!isAPlayer(attacker)) return; Player player = (Player) attacker;
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!doesPlayerHaveUltimate(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);
        setUsedUltimate(player);

        // Cosmetics
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.RED + "The Bee has stung it's prey!");

        // Hurt victim
        ((LivingEntity) victim).damage(15);

        // Unsheathe stinger
        setHelmet(player, GOLDEN_HELMET);
        setChestplate(player, GOLDEN_CHESTPLATE);
        setLeggings(player, GOLDEN_LEGGINGS);
        setBoots(player, GOLDEN_BOOTS);
        addEnchantToHelmet(player, PROTECTION, 1);
        addEnchantToChestplate(player, PROTECTION, 1);
        addEnchantToLeggings(player, PROTECTION, 1);
        addEnchantToBoots(player, PROTECTION, 1);
        giveEffect(player, RESISTANCE, 30, 1);
    }
}
