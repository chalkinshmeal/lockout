package chalkinshmeal.labyrinth.artifacts.items.fox;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fox;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.UUID;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.AIR;
import static org.bukkit.entity.EntityType.FOX;
import static org.bukkit.entity.Fox.Type.RED;
import static org.bukkit.entity.Fox.Type.SNOW;
import static org.bukkit.potion.PotionEffectType.SPEED;

public class FoxTamer extends LabyrinthItem {
    public FoxTamer(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onSpawn(Player player) {
        if (!isPlayerHoldingItemInInventory(player)) return;

        // Spawn fighter fox
        Fox fighterFox = (Fox) player.getWorld().spawnEntity(player.getLocation(), FOX);
        fighterFox.setFoxType(RED);
        fighterFox.setFirstTrustedPlayer(player);
        fighterFox.getEquipment().setItemInMainHand(game.getLabyrinthItemHandler().getItem(108).getItemStack());
        giveEffect(fighterFox, SPEED, 100000, 3);
        this.game.addPlayerSummons(player, List.of(fighterFox));

        // Spawn snow fox
        for (int i = 0; i < 2; i++) {
            Fox stealerFox = (Fox) player.getWorld().spawnEntity(player.getLocation(), FOX);
            stealerFox.setFoxType(SNOW);
            stealerFox.setFirstTrustedPlayer(player);
            giveEffect(fighterFox, SPEED, 100000, 3);
            this.game.addPlayerSummons(player, List.of(stealerFox));
        }
    }

    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer(); Entity clickedEntity = event.getRightClicked();
        if (!isPlayerHoldingItemInInventory(player)) return;
        if (!event.getHand().equals(EquipmentSlot.HAND)) return;
        if (!isLiving(clickedEntity)) return; LivingEntity entity = (LivingEntity) clickedEntity;
        if (!game.getSummonedEntities().get(player.getUniqueId()).contains(entity)) return;
        if (!(entity instanceof Fox)) return;

        // Reset state
        event.setCancelled(true);

        Fox fox = (Fox) entity;
        ItemStack foxHeldItem = fox.getEquipment().getItemInMainHand();
        ItemStack playerHeldItem = player.getInventory().getItemInMainHand();

        // Give fox item
        if (foxHeldItem.getType().equals(AIR)) {
           fox.getEquipment().setItemInMainHand(playerHeldItem.asOne());
           player.getEquipment().setItemInMainHand(playerHeldItem.subtract(1));
        }
        else {
            fox.getEquipment().setItemInMainHand(null);
            player.getInventory().addItem(foxHeldItem);
        }
    }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity entity = event.getEntity();
        if (!(attacker instanceof Fox)) return; Fox fox = (Fox) attacker;
        if (!(fox.getFoxType().equals(SNOW))) return;
        if (!(entity instanceof LivingEntity || entity instanceof InventoryHolder)) return;
        //if (!(getRandNum(0, 4) == 0)) return;

        event.setCancelled(true);

        // Check if entity attacker is a summon fox
        for (UUID uuid : this.game.getInGamePlayerUUIDs()) {
            List<Entity> summons = this.game.getSummonedEntities().get(uuid);
            if (summons.contains(attacker)) {
                // Get random item from the victim
                Player master = this.plugin.getServer().getPlayer(uuid);

                ItemStack item;
                if (entity instanceof InventoryHolder) {
                    item = removeAndGetRandItem((InventoryHolder) entity);
                }
                else {
                    item = removeAndGetRandItem((LivingEntity) entity);
                }

                if (item == null || item.getType().equals(AIR)) return;

                fox.getEquipment().setItemInMainHand(item);
                // Cosmetics
                master.sendMessage(ChatColor.GOLD + "A fox has stolen item '" + ChatColor.RED + item.getType() + "" + ChatColor.GOLD + "'!");
            }
        }
    }
}
