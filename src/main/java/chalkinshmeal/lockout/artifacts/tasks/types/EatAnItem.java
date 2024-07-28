package chalkinshmeal.lockout.artifacts.tasks.types;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import chalkinshmeal.lockout.artifacts.tasks.LockoutTask;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTaskHandler;
import chalkinshmeal.lockout.data.ConfigHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class EatAnItem extends LockoutTask {
    private final Material material;

    //---------------------------------------------------------------------------------------------
    // Constructor, which takes lockouttaskhandler
    //---------------------------------------------------------------------------------------------
    public EatAnItem(ConfigHandler configHandler, LockoutTaskHandler lockoutTaskHandler, Material material) {
        super(configHandler, lockoutTaskHandler);
        this.material = material;
        this.name = "Eat A " + this.material.name();
        this.item = new ItemStack(this.material);
        this.setItemDisplayName(Component.text(this.name, NamedTextColor.GOLD));
    }

    //---------------------------------------------------------------------------------------------
    // Any listeners. Upon completion, LockoutTaskHandler.CompleteTask(player);
    //---------------------------------------------------------------------------------------------
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
        if (!event.getItem().getType().equals(this.material)) return;

        this.complete(event.getPlayer().getUniqueId());

    }
}
