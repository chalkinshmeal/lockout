package chalkinshmeal.labyrinth.artifacts.items.allay;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.GameMode.*;

public class AllaySoulLantern extends LabyrinthItem {
    public final float damage = 4;
    public AllaySoulLantern(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer(); Entity entity = event.getRightClicked();
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isAPlayer(entity)) return; Player victim = (Player) entity;
        if (!isItemCooldownReady(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        resetItemCooldown(player);

        // Set player's gamemode to spectator
        victim.setGameMode(SPECTATOR);
        victim.setSpectatorTarget(player);
        victim.sendMessage(ChatColor.AQUA + "Your soul has been trapped!");
        player.sendMessage(ChatColor.AQUA + "You have trapped " + ChatColor.RED + victim.getName() + " " + ChatColor.AQUA + "'s soul!");

        // Start task to reset player's
        startFreeSoulTask(game, player, victim, getDurationInSeconds(), damage);
    }

    public void startFreeSoulTask(Game game, Player player, Player victim, float duration, float damage) {
        new BukkitRunnable() {
            float secsLeft = duration;
            @Override
            public void run() {
                // End condition
                secsLeft -= 1;
                if (game.isGameDone() || secsLeft <= 0) {
                    victim.setGameMode(SURVIVAL);
                    victim.damage(damage);
                    if (game.isPlayerInPlay(player)) {
                        victim.teleport(player);
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(this.plugin, 20L * 0L, (long) (20L * 1));
    }
}
