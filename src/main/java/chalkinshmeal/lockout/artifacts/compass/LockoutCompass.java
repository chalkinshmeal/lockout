package chalkinshmeal.lockout.artifacts.compass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import chalkinshmeal.lockout.artifacts.game.GameHandler;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTask;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTaskHandler;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class LockoutCompass {
    private final ConfigHandler configHandler;
    private final Inventory teams;
    private final Inventory tasks; 
    private final int taskCount;
    private boolean isActive;

    private final Component compassDisplayName = Component.text(
        "Lockout", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false);
    public final String teamsInvName = "Lockout Teams";
    private final List<Material> teamMaterials = new ArrayList<Material>(Arrays.asList(
        Material.BLUE_WOOL, Material.GREEN_WOOL, Material.RED_WOOL, Material.ORANGE_WOOL));

    //---------------------------------------------------------------------------------------------
    // Constructor
    //---------------------------------------------------------------------------------------------
    public LockoutCompass(ConfigHandler configHandler) {
        this.configHandler = configHandler;
        this.taskCount = Utils.getHighestMultiple((int) configHandler.getInt("taskCount", 27), 9);
        this.teams = Bukkit.createInventory(null, 9, Component.text(this.teamsInvName, NamedTextColor.LIGHT_PURPLE));
        this.tasks = Bukkit.createInventory(null, this.taskCount, Component.text("Lockout Tasks", NamedTextColor.LIGHT_PURPLE));
        this.isActive = false;

        this.updateTeamsInventory(null);
        this.updateTasksInventory(null);
    }

    //---------------------------------------------------------------------------------------------
    // Accessor/Mutator methods 
    //---------------------------------------------------------------------------------------------
    public String GetTeamInvName() { return ChatColor.stripColor(this.teamsInvName); }
    public void SetIsActive(boolean isActive) { this.isActive = isActive; }

    //---------------------------------------------------------------------------------------------
    // Inventory methods
    //---------------------------------------------------------------------------------------------
    public void updateTeamsInventory(GameHandler gameHandler) {
        this.teams.clear();
        for (int i = 0; i < this.teamMaterials.size(); i++) {
            int playerCount = 0;
            if (gameHandler != null) playerCount = gameHandler.GetPlayerCount(i);
            ItemStack item = new ItemStack(this.teamMaterials.get(i));
            ItemMeta meta = item.getItemMeta();
            TextComponent displayName = Component.text("Team " + i + " (" + playerCount + " players)", NamedTextColor.WHITE);
            if (gameHandler != null) {
                for (String playerName : gameHandler.GetPlayerNames(i)) {
                    displayName.append(Component.text(playerName + "\n", NamedTextColor.WHITE));
                }
            }
            meta.displayName(displayName);
            item.setItemMeta(meta);
            this.teams.addItem(item);
        }
    }

    public void updateTasksInventory(LockoutTaskHandler lockoutTaskHandler) {
        this.tasks.clear();
        if (lockoutTaskHandler == null) return;

        for (LockoutTask task : lockoutTaskHandler.GetTasks()) {
            this.tasks.addItem(task.getItem());
        }
    }

    //---------------------------------------------------------------------------------------------
    // Compass methods
    //---------------------------------------------------------------------------------------------
    public void giveCompass(Player player) {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(compassDisplayName);
        item.setItemMeta(meta);

        Utils.giveItem(player, item);
    }

    public void openInventory(Player player) {
        if (this.isActive) player.openInventory(this.tasks);
        else player.openInventory(this.teams);
    }

    //---------------------------------------------------------------------------------------------
    // Listener methods
    //---------------------------------------------------------------------------------------------
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!Utils.isRightClick(event.getAction())) return;
        if (event.getItem() == null) return;
        if (event.getItem().getItemMeta().displayName() == null) return;
        if (!event.getItem().getItemMeta().displayName().equals(this.compassDisplayName)) return;

        this.openInventory(event.getPlayer());
    }
}
