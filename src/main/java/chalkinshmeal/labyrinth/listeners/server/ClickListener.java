package chalkinshmeal.labyrinth.listeners.server;

import chalkinshmeal.labyrinth.artifacts.classes.LabyrinthClassHandler;
import chalkinshmeal.labyrinth.artifacts.game.GameHandler;
import chalkinshmeal.labyrinth.artifacts.labyrinth.LabyrinthHandler;
import chalkinshmeal.labyrinth.artifacts.wand.Wand;
import chalkinshmeal.labyrinth.artifacts.wand.WandHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * When a block is clicked, perform any wand operations
 */
public class ClickListener implements Listener {
    private final JavaPlugin plugin;
    private final ConfigHandler configHandler;
    private final WandHandler wandHandler;
    private final GameHandler gameHandler;
    private final LabyrinthClassHandler labyrinthClassHandler;
    private final LabyrinthHandler labyrinthHandler;

    /** Constructor */
    public ClickListener(JavaPlugin plugin, WandHandler wandHandler, GameHandler gameHandler, LabyrinthClassHandler labyrinthClassHandler, LabyrinthHandler labyrinthHandler) {
        this.plugin = plugin;
        this.configHandler = new ConfigHandler(plugin);
        this.wandHandler = wandHandler;
        this.gameHandler = gameHandler;
        this.labyrinthClassHandler = labyrinthClassHandler;
        this.labyrinthHandler = labyrinthHandler;
    }

    /** Event Handler */
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        this.clickedSign(event);

        // Retrieve info about event
        Wand wand = this.wandHandler.getWand(player.getUniqueId());
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        Block clickedBlock = event.getClickedBlock();

        // Return if held item is not the wand item or not left click
        if (!this.isWand(heldItem)) return;

        // Set pos1 if left click, else pos2 if right click
        if (this.isLeftClick(event.getAction())) { wand.setPos1(clickedBlock, player); }
        else { wand.setPos2(clickedBlock, player); }

        // Cancel the event - we are using a wand, not destroying things
        event.setCancelled(true);
    }

    /** Return T if is wand-item, else F */
    private boolean isWand(ItemStack heldItem) {
        return (heldItem.getType() == configHandler.getMaterialFromKey("wand-item"));
    }

    /** Return T if left click, else F */
    private boolean isLeftClick(Action action) {
        return action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
    }

    public void clickedSign(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (!(block.getState() instanceof Sign)) return;
        if (!(event.getAction().isRightClick())) return;
        Sign sign = (Sign) block.getState();

        event.setCancelled(true);

        // First line should be [Labyrinth]
        String cmd = ChatColor.stripColor(sign.getLine(0).strip());
        String arg = ChatColor.stripColor(sign.getLine(1).strip());
        if (cmd.contains("[Join]")) this.gameHandler.joinGame(player, arg);
        else if (cmd.contains("[Leave]")) this.gameHandler.leaveGame(player);
        else if (cmd.contains("[Ready]")) this.gameHandler.readyUp(player);
        else if (cmd.contains("[Toggle Items]")) this.gameHandler.toggleAreItemsEnabled(player);
        else if (cmd.contains("[Select]")) {
            this.gameHandler.setClassSelection(player, arg);
            this.labyrinthClassHandler.giveClassDebug(player, arg);
        }
    }
}