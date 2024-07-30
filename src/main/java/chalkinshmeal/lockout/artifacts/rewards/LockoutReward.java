package chalkinshmeal.lockout.artifacts.rewards;

import org.bukkit.entity.Player;

public abstract class LockoutReward {
    protected String description = "No description";

    public LockoutReward() {}

    //---------------------------------------------------------------------------------------------
    // Accessor/Mutator methods
    //---------------------------------------------------------------------------------------------
    public String getDescription() { return this.description; }

    //---------------------------------------------------------------------------------------------
    // Reward methods
    //---------------------------------------------------------------------------------------------
    public abstract void giveReward(Player player);
}
