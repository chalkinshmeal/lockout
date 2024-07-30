package chalkinshmeal.lockout.artifacts.rewards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.rewards.types.GoldenAppleReward;
import chalkinshmeal.lockout.utils.Utils;

public class LockoutRewardHandler {
    public final JavaPlugin plugin;
    public List<LockoutReward> rewards = new ArrayList<>();
    public List<LockoutReward> punishments = new ArrayList<>();

    public LockoutRewardHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.rewards.add(new GoldenAppleReward());
    }

    //---------------------------------------------------------------------------------------------
    // Reward methods
    //---------------------------------------------------------------------------------------------
    public LockoutReward getRandomReward() {
        if (this.rewards.size() == 0) return null;
        return this.rewards.remove(Utils.getRandNum(0, this.rewards.size() - 1));
    }

    public LockoutReward getRandomPunishment() {
        if (this.punishments.size() == 0) return null;
        return this.punishments.remove(Utils.getRandNum(0, this.punishments.size() - 1));
    }
}
