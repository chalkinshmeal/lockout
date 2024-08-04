package chalkinshmeal.lockout.artifacts.rewards.punishments;

import org.bukkit.entity.Player;
import chalkinshmeal.lockout.artifacts.rewards.LockoutReward;

public class BlowUpPunishment extends LockoutReward {
    //---------------------------------------------------------------------------------------------
    // Constructor
    //---------------------------------------------------------------------------------------------
    public BlowUpPunishment() {
        super();
        this.description = "Blow up";
    }

    //---------------------------------------------------------------------------------------------
    // Reward methods
    //---------------------------------------------------------------------------------------------
    public void giveReward(Player player) {
        player.getLocation().getWorld().createExplosion(player.getLocation(), 10.0F, true, true);
        player.damage(100);
    }
}
