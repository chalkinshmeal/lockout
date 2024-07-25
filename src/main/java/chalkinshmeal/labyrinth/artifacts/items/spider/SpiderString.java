package chalkinshmeal.labyrinth.artifacts.items.spider;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static chalkinshmeal.labyrinth.utils.Utils.isAPlayer;

public class SpiderString extends LabyrinthItem {
    Set<UUID> spiderPlayers = new HashSet<>();
    public SpiderString(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    // On spawn, generate web
    public void onSpawn(Player player) {
        if (!isPlayerHoldingItemInInventory(player)) return;

        if (!spiderPlayers.contains(player.getUniqueId())) {
            spiderPlayers.add(player.getUniqueId());
            startGenerateWebTask(game, player, getCooldownInSeconds(), this);
        }
    }

    public void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!isPlayerHoldingItemInInventory(player)) return;
        if (!spiderPlayers.contains(player.getUniqueId())) {
            spiderPlayers.add(player.getUniqueId());
            startGenerateWebTask(game, player, getCooldownInSeconds(), this);
        }
    }

    public void onInventoryDragEvent(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!isPlayerHoldingItemInInventory(player)) return;
        if (!spiderPlayers.contains(player.getUniqueId())) {
            spiderPlayers.add(player.getUniqueId());
            startGenerateWebTask(game, player, getCooldownInSeconds(), this);
        }
    }

    public void onEntityPickupItemEvent(EntityPickupItemEvent event) {
        Entity entity = event.getEntity(); ItemStack item = event.getItem().getItemStack();
        if (!isAPlayer(entity)) return; Player player = (Player) entity;
        if (!isItemThisItem(item)) return;
        if (!spiderPlayers.contains(player.getUniqueId())) {
            spiderPlayers.add(player.getUniqueId());
            startGenerateWebTask(game, player, getCooldownInSeconds(), this);
        }
    }

    // Life Sapper Damage
    public void startGenerateWebTask(Game game, Player player, float interval, SpiderString item) {
        new BukkitRunnable() {
            final int livesLeft = game.getScoreboard().getLives(player);
            @Override
            public void run() {
                // End conditions
                boolean isGameDone = game.isGameDone();
                boolean didPlayerDie = game.getScoreboard().getLives(player) != livesLeft;
                boolean isHoldingItem = item.isPlayerHoldingItemInInventory(player);
                if (isGameDone || didPlayerDie || !isHoldingItem) {
                    item.spiderPlayers.remove(player.getUniqueId());
                    this.cancel();
                    return;
                }

                // Give web
                player.getInventory().addItem(game.getLabyrinthItemHandler().getItem(2).getItemStack().asOne());
            }
        }.runTaskTimer(this.plugin, 20L * 0L, (long) (20L * interval));
    }
}