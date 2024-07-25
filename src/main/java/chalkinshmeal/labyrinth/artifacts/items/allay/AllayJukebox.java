package chalkinshmeal.labyrinth.artifacts.items.allay;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.giveEffect;
import static chalkinshmeal.labyrinth.utils.Utils.removeEffect;
import static org.bukkit.potion.PotionEffectType.REGENERATION;

// NOTE:
// Minecraft regeneration (apparently) works like so:
// - Every n ticks, you will be given half a heart
// - However, if you lose the regen, the ticks reset - so you need to have regen for the whole
//   n ticks, in a row, to get your regen

public class AllayJukebox extends LabyrinthItem {
    public AllayJukebox(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (!isPlayerHoldingItemInInventory(player)) return;

        // If player switched to this, give player regen
        if (isPlayerHoldingItemInSlot(player, event.getNewSlot())) {
            // Calculate how many allays they have as summons
            List<Entity> summons = game.getSummonedEntities().get(player.getUniqueId());
            List<Allay> allays = new ArrayList<>();
            for (Entity summon : summons) {
                if (summon instanceof Allay && !summon.isDead()) {
                    Allay allay = (Allay) summon;
                    allay.startDancing();
                    allays.add(allay);
                }
            }

            if (allays.size() > 0) {
                giveEffect(player, REGENERATION, 100000, allays.size());
            }
        }

        // Else, take away regen
        else {
            removeEffect(player, REGENERATION);
            List<Entity> summons = game.getSummonedEntities().getOrDefault(player.getUniqueId(), new ArrayList<>());
            for (Entity summon : summons) {
                if (summon instanceof Allay) {
                    Allay allay = (Allay) summon;
                    allay.stopDancing();
                }
            }
        }

    }
}
