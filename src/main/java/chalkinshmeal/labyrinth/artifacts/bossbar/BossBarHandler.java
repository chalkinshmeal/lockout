package chalkinshmeal.labyrinth.artifacts.bossbar;

import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BossBarHandler {
    private JavaPlugin plugin;
    private Map<UUID, BossBar> bossBars;

    public BossBarHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.bossBars = new HashMap<>();
    }

    public BossBar getBossBar(Player player) { return this.bossBars.getOrDefault(player.getUniqueId(), null); }

    public void addBossBar(Player player) {
        UUID uuid = player.getUniqueId();
        BossBar bossBar = this.plugin.getServer().createBossBar(
            ChatColor.GOLD + player.getName(), BarColor.PURPLE, BarStyle.SOLID);
        this.bossBars.put(uuid, bossBar);
    }

    public void setBossBarVisible(Player player, List<Player> visibleTo) {
        for (Player p : visibleTo) { this.getBossBar(player).addPlayer(p); }
        this.getBossBar(player).setVisible(true);
    }

    public void updateBossBar(Player player, double progress) {
        this.bossBars.get(player.getUniqueId()).setProgress(progress);
    }

    // General
    public void clearBossBar(Player player) {
        if (!this.bossBars.containsKey(player.getUniqueId())) return;

        this.bossBars.get(player.getUniqueId()).removeAll();
        this.bossBars.get(player.getUniqueId()).setVisible(false);
        this.bossBars.remove(player.getUniqueId());
    }
}