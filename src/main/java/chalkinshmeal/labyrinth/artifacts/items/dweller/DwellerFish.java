package chalkinshmeal.labyrinth.artifacts.items.dweller;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class DwellerFish extends LabyrinthItem {
    public DwellerFish(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!isRightClick(event)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);

        // Increase health
        addHealth(player, 1);

        // Cosmetics
        playSound(player, Sound.ENTITY_GENERIC_EAT);
        player.sendMessage(ChatColor.GREEN + "Nom nom.");
    }
}
