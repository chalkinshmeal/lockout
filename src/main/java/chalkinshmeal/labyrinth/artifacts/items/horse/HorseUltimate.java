package chalkinshmeal.labyrinth.artifacts.items.horse;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.SHIELD;
import static org.bukkit.Material.STONE_HOE;
import static org.bukkit.potion.PotionEffectType.SLOW_FALLING;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class HorseUltimate extends LabyrinthItem {
    public HorseUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

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
        broadcastGameMsg(ChatColor.GOLD + "The Horse has summoned its rider...");

        // Spawn horse and put player on it
        Horse horse = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
        horse.setJumpStrength(5);
        horse.setTamed(true);
        horse.setOwner(player);
        horse.setPersistent(false);
        horse.addPassenger(player);
        horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
        horse.getInventory().setArmor(new ItemStack(Material.IRON_HORSE_ARMOR));
        giveEffect(horse, SPEED, 100000, 2);
        giveEffect(horse, SLOW_FALLING, 100000, 1);

        // Give player knight items
        LabyrinthItem sword = new LabyrinthItem(Map.of(
                "position", 0,
                "material", STONE_HOE,
                "enchants", Map.of("SHARPNESS", 5, "KNOCKBACK", 5),
                "displayName", "§6Lance"));
        player.getInventory().setItem(0, sword.getItemStack());
        player.getInventory().setItemInOffHand(new ItemStack(SHIELD));
        setHelmet(player, Material.CHAINMAIL_HELMET);
        setChestplate(player, Material.CHAINMAIL_CHESTPLATE);
        setLeggings(player, Material.CHAINMAIL_LEGGINGS);
        setBoots(player, Material.CHAINMAIL_BOOTS);
    }
}
