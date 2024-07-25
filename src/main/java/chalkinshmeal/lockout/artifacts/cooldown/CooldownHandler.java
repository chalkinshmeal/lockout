package chalkinshmeal.lockout.artifacts.cooldown;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CooldownHandler {
    private final JavaPlugin plugin;
    private final List<Cooldown> cooldowns;

    public CooldownHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cooldowns = new ArrayList<>();
    }

    // Status
    public boolean isCooldownReady(String name, Player player) {
        Cooldown c = this.getCooldownByName(name);
        if (c == null) {
            this.plugin.getLogger().warning(
            "There is no cooldown by the name of '" + name + "'. Ignoring isCooldownReady request.");
            return false;
        }
        return c.isCooldownReady(player);
    }
    public boolean isUltimateDone(String name, Player player) { return isCooldownReady(name, player); }

    // Manipulators
    public void resetCooldown(String name, Player player) {
        Cooldown c = this.getCooldownByName(name);
        if (c == null) {
            this.plugin.getLogger().warning(
                    "There is no cooldown by the name of '" + name +
                            "'. Ignoring resetCooldown request.");
            return;
        }
        c.resetCooldown(player);
    }
    public void startUltimate(String name, Player player) { this.resetCooldown(name, player); }
    public void addCooldown(String name, float cooldownTime) {
        this.cooldowns.add(new Cooldown(name, cooldownTime));
    }
    public void setCooldown(String name, Player player, float secs) {
        Cooldown c = this.getCooldownByName(name);
        if (c == null) {
            this.plugin.getLogger().warning(
                    "There is no cooldown by the name of '" + name +
                            "'. Ignoring setCooldown request.");
            return;
        }
        c.setCooldown(player, (long) secs);
    }
    public Cooldown getCooldownByName(String name) {
        for (Cooldown c : this.cooldowns) { if (c.getName().equals(name)) { return c; } }
        return null;
    }
    public float getCooldownTimeByName(String name) {
        Cooldown c = this.getCooldownByName(name);
        if (c != null) return c.getLengthInSecs();
        return 0L;
    }
    public int getCooldownLeft(String name, Player player) {
        Cooldown c = this.getCooldownByName(name);
        return c.getCooldownLeft(player);
    }
}
