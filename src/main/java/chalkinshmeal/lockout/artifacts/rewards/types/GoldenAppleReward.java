package chalkinshmeal.lockout.artifacts.rewards.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import chalkinshmeal.lockout.artifacts.rewards.LockoutReward;
import chalkinshmeal.lockout.utils.Utils;

public class GoldenAppleReward extends LockoutReward {
    //---------------------------------------------------------------------------------------------
    // Constructor
    //---------------------------------------------------------------------------------------------
    public GoldenAppleReward() {
        super();
        this.description = "Receive a golden apple";
    }

    //---------------------------------------------------------------------------------------------
    // Reward methods
    //---------------------------------------------------------------------------------------------
    public void giveReward(Player player) {
        Utils.giveItem(player, new ItemStack(Material.GOLDEN_APPLE, 1));
    }
}
