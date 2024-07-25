package chalkinshmeal.labyrinth.artifacts.items.allay;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static org.bukkit.entity.EntityType.ALLAY;
import static org.bukkit.entity.memory.MemoryKey.LIKED_PLAYER;

public class AllaySwarm extends LabyrinthItem {
    public final int allayCount = 5;
    public AllaySwarm(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onSpawn(Player player) {
        if (!isPlayerHoldingItemInInventory(player)) return;
        for (int i = 0; i < allayCount; i++) {
            Allay allay = (Allay) player.getWorld().spawnEntity(player.getLocation(), ALLAY);
            allay.setMaxHealth(1);
            allay.setMemory(LIKED_PLAYER, player.getUniqueId());
            this.game.addPlayerSummons(player, List.of(allay));
        }
    }
}
