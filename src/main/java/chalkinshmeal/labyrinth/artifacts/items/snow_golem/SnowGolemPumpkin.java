package chalkinshmeal.labyrinth.artifacts.items.snow_golem;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.CARVED_PUMPKIN;

public class SnowGolemPumpkin extends LabyrinthItem {
    public SnowGolemPumpkin(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer(); Entity deadOrAliveEntity = event.getRightClicked();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isLiving(deadOrAliveEntity)) return; LivingEntity entity = (LivingEntity) deadOrAliveEntity;
        if (entity.getEquipment().getHelmet() != null) {
            if (entity.getEquipment().getHelmet().getType() == CARVED_PUMPKIN) return;
        }

        // Reset state
        event.setCancelled(true);
        useHeldItem(player);

        setHelmet(entity, CARVED_PUMPKIN);
        startSnowGolemReplaceHelmetTask(this.game, entity);
        if (isAPlayer(entity))
            entity.sendMessage(ChatColor.GOLD + "A pumpkin grew on your head!");
        player.sendMessage(ChatColor.GOLD + "You grew a pumpkin!");
    }
    public void startSnowGolemReplaceHelmetTask(Game game, LivingEntity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.getEquipment().getHelmet().getType().equals(Material.CARVED_PUMPKIN))
                    setHelmet(entity, Material.AIR);
            }
        }.runTaskLater(this.plugin, 20L*5L);
    }
}
