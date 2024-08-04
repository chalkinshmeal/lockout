package chalkinshmeal.lockout.artifacts.rewards;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import chalkinshmeal.lockout.artifacts.rewards.punishments.BlowUpPunishment;
import chalkinshmeal.lockout.artifacts.rewards.types.BowArrowReward;
import chalkinshmeal.lockout.artifacts.rewards.types.GoldenAppleReward;
import chalkinshmeal.lockout.artifacts.rewards.types.PotionEffectReward;
import chalkinshmeal.lockout.utils.Utils;

public class LockoutRewardHandler {
    public final JavaPlugin plugin;
    public List<LockoutReward> rewards = new ArrayList<>();
    public List<LockoutReward> punishments = new ArrayList<>();

    public LockoutRewardHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.createRewardsList();
        this.createPunishmentList();
    }

    public void createRewardsList() {
        this.rewards.add(new GoldenAppleReward());
        this.rewards.add(new BowArrowReward());
        this.rewards.add(new PotionEffectReward(PotionEffectType.SPEED, 10000, 2));
        this.rewards.add(new PotionEffectReward(PotionEffectType.STRENGTH, 10000, 1));
        this.rewards.add(new PotionEffectReward(PotionEffectType.HASTE, 10000, 1));
        this.rewards.add(new PotionEffectReward(PotionEffectType.REGENERATION, 10000, 1));
        this.rewards.add(new PotionEffectReward(PotionEffectType.RESISTANCE, 10000, 1));
        this.rewards.add(new PotionEffectReward(PotionEffectType.FIRE_RESISTANCE, 10000, 1));
        this.rewards.add(new PotionEffectReward(PotionEffectType.ABSORPTION, 10000, 2));
    }

    public void createPunishmentList() {
        this.punishments.add(new BlowUpPunishment());
        this.punishments.add(new BlowUpPunishment());
        this.punishments.add(new BlowUpPunishment());
        this.punishments.add(new PotionEffectReward(PotionEffectType.SLOWNESS, 120, 2));
        this.punishments.add(new PotionEffectReward(PotionEffectType.BLINDNESS, 120, 1));
        this.punishments.add(new PotionEffectReward(PotionEffectType.WEAKNESS, 120, 1));
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
