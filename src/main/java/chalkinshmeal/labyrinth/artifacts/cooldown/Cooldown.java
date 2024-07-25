package chalkinshmeal.labyrinth.artifacts.cooldown;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Cooldown {
    private final String name;
    private final float lengthInSecs;
    private final Map<UUID, Long> timers;

    public Cooldown(String name, float lengthInSecs) {
        this.name = name;
        this.lengthInSecs = lengthInSecs;
        this.timers = new HashMap<>();
    }

    // Getters + Setters
    public String getName() { return this.name; }
    public float getLengthInSecs() { return this.lengthInSecs; }
    public int getCooldownLeft(Player player) {
        return (int) (this.lengthInSecs - TimeUnit.MILLISECONDS.toMillis(System.currentTimeMillis() - this.getCooldown(player))/1000);
    }
    public Long getCooldown(Player player) {
        if (!isPlayerInCooldown(player, true)) return 0L;
        return this.timers.get(player.getUniqueId());
    }
    public void setCooldown(Player player, Long time) {
        if (!isPlayerInCooldown(player, true)) return;
        this.timers.put(player.getUniqueId(), time);
    }
    public void resetCooldown(Player player) {
        if (!isPlayerInCooldown(player, true)) return;
        this.setCooldown(player, System.currentTimeMillis());
    }

    // Status
    public boolean isCooldownReady(Player player) {
        if (!isPlayerInCooldown(player, true)) return false;

        long timeLeft = System.currentTimeMillis() - this.getCooldown(player);
        return (((float) TimeUnit.MILLISECONDS.toMillis(timeLeft)/1000) >= this.lengthInSecs);
    }

    // Utilities
    public void addPlayer(Player player) {
        if (isPlayerInCooldown(player, false)) return;
        this.timers.put(player.getUniqueId(), 0L);
    }
    public boolean isPlayerInCooldown(Player player, boolean shouldBe) {
        return this.timers.containsKey(player.getUniqueId());
    }
}
