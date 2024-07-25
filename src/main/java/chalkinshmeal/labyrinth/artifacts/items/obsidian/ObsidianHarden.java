package chalkinshmeal.labyrinth.artifacts.items.obsidian;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.*;

public class ObsidianHarden extends LabyrinthItem {
    public ObsidianHarden(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;
        if (!isItemCooldownReady(player)) return;

        // Reset state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Cosmetics
        playSound(player, Sound.BLOCK_DEEPSLATE_PLACE);

        // Schedule task to reset armor
        obsidianHardenDone(player, (int) getDurationInSeconds());

        // Give netherite armor temporarily
        setChestplate(player, NETHERITE_CHESTPLATE);
        setLeggings(player, NETHERITE_LEGGINGS);
        setBoots(player, NETHERITE_BOOTS);
    }

    public void obsidianHardenDone(Player player, int duration) {
        new BukkitRunnable() {
            float secsLeft = duration;
            ItemStack helmet = getHelmet(player);
            ItemStack chestplate = getChestplate(player);
            ItemStack leggings = getLeggings(player);
            ItemStack boots = getBoots(player);
            // Save previous armor state
            @Override
            public void run() {
                // End condition
                secsLeft -= 1;
                if (secsLeft < 0) {
                    // Cosmetics
                    playSound(player, Sound.BLOCK_DEEPSLATE_BREAK);

                    // Restore previous armor
                    setHelmet(player, helmet);
                    setChestplate(player, chestplate);
                    setLeggings(player, leggings);
                    setBoots(player, boots);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 20L*0L, 20L*1L);
    }
}
