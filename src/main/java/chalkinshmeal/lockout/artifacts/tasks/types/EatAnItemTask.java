package chalkinshmeal.lockout.artifacts.tasks.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.tasks.LockoutTask;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTaskHandler;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class EatAnItemTask extends LockoutTask {
    private final Material material;
    private final int rewardCount = 10;

    //---------------------------------------------------------------------------------------------
    // Constructor, which takes lockouttaskhandler
    //---------------------------------------------------------------------------------------------
    public EatAnItemTask(JavaPlugin plugin, ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler, Material material) {
        super(plugin, configHandler, lockoutTaskHandler);
        this.material = material;
        this.name = "Eat a " + Utils.getReadableMaterialName(material);
        this.item = new ItemStack(this.material);
        this.rewardStr = "Receive " + rewardCount + " " + Utils.getReadableMaterialName(material);
        this.setItemDisplayName(Component.text(this.name, NamedTextColor.BLUE).decoration(TextDecoration.ITALIC, false));
        this.addLore();
        this.addListeners();
    }

    //---------------------------------------------------------------------------------------------
    // Abstract methods
    //---------------------------------------------------------------------------------------------
    public void addListeners() {
		this.listeners.add(new EatAnItemTaskPlayerItemConsumeListener(this));
    }

    //---------------------------------------------------------------------------------------------
    // Any listeners. Upon completion, LockoutTaskHandler.CompleteTask(player);
    //---------------------------------------------------------------------------------------------
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        if (!event.getItem().getType().equals(this.material)) return;

        this.complete(event.getPlayer());
    }

    //---------------------------------------------------------------------------------------------
    // Reward methods
    //---------------------------------------------------------------------------------------------
    public void reward(LockoutTaskHandler lockoutTaskHandler, Player player) {
        Utils.giveItem(player, new ItemStack(this.material, this.rewardCount));
    }
}

//---------------------------------------------------------------------------------------------
// Private classes - any listeners that this task requires
//---------------------------------------------------------------------------------------------
class EatAnItemTaskPlayerItemConsumeListener implements Listener {
    private final EatAnItemTask task;

    public EatAnItemTaskPlayerItemConsumeListener(EatAnItemTask task) {
        this.task = task;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        if (this.task.isComplete()) return;
        this.task.onPlayerItemConsumeEvent(event);
    }
}

