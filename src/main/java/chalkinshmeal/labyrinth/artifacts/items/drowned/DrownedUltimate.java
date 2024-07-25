package chalkinshmeal.labyrinth.artifacts.items.drowned;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.potion.PotionEffectType.SLOWNESS;

public class DrownedUltimate extends LabyrinthItem {
    public DrownedUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) { super(plugin, config, className, position, game); }

    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager(); Entity victim = event.getEntity();
        if (!isEntityATrident(attacker)) return;
        Trident trident = (Trident) event.getDamager(); ProjectileSource projectileSource = (ProjectileSource) trident.getShooter();
        if (!isShooterAPlayer(projectileSource)) return;
        if (!isItemThisItem(trident.getItem())) return;
        Player player = (Player) projectileSource;
        if (!doesPlayerHaveUltimate(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);
        setUsedUltimate(player);

        // Cosmetics
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.AQUA + "Lightning very very frightening...");

        // Strike all players
        for (Player p : this.game.getPlayers()) {
            if (p.equals(player)) continue;
            strikeLightning(p);
            p.setFreezeTicks(220);
            giveEffect(p, SLOWNESS, 10, 2);
            subtractHealth(p, 2);
        }
    }
}
