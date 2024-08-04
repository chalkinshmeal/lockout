package chalkinshmeal.lockout.artifacts.rewards.types;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import chalkinshmeal.lockout.artifacts.rewards.LockoutReward;
import chalkinshmeal.lockout.utils.Utils;

public class PotionEffectReward extends LockoutReward {
    private final PotionEffectType type;
    private final int duration;
    private final int level;

    //---------------------------------------------------------------------------------------------
    // Constructor
    //---------------------------------------------------------------------------------------------
    public PotionEffectReward(PotionEffectType type, int duration, int level) {
        super();
        this.type = type;
        this.duration = duration;
        this.level = level;
        this.description = "Receive " + Utils.toTitleCase(type.getKey().asMinimalString().replace('_', ' ')) + " " + Utils.intToRomanNumerals(level);
    }

    //---------------------------------------------------------------------------------------------
    // Reward methods
    //---------------------------------------------------------------------------------------------
    public void giveReward(Player player) {
        Utils.giveEffect(player, this.type, this.duration, this.level);
    }
}
