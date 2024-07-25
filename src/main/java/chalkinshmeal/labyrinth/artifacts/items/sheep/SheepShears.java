package chalkinshmeal.labyrinth.artifacts.items.sheep;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class SheepShears extends LabyrinthItem {
    public SheepShears(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer(); Entity deadOrAliveEntity = event.getRightClicked();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isLiving(deadOrAliveEntity)) return; LivingEntity entity = (LivingEntity) deadOrAliveEntity;
        if (!isWearingArmor(entity)) return;

        // Reset state
        event.setCancelled(true);
        useHeldItem(player);

        // Cosmetics
        playSound(player, Sound.ENTITY_SHEEP_SHEAR);
        if (entity instanceof Player) entity.sendMessage(ChatColor.GOLD + "You have been sheared!");

        // Shear
        if (getRandNum(0, 1) == 0) setHelmet(entity, Material.AIR);
        if (getRandNum(0, 1) == 0) setChestplate(entity, Material.AIR);
        if (getRandNum(0, 1) == 0) setLeggings(entity, Material.AIR);
        if (getRandNum(0, 1) == 0) setBoots(entity, Material.AIR);
    }
}
