package chalkinshmeal.labyrinth.commands.wand;

import chalkinshmeal.labyrinth.artifacts.wand.Wand;
import chalkinshmeal.labyrinth.artifacts.wand.WandHandler;
import chalkinshmeal.labyrinth.cmdframework.command.BaseCommand;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Pos1Command extends BaseCommand {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final WandHandler wandHandler;

    // Constructor
    public Pos1Command(JavaPlugin plugin, WandHandler wandHandler) {
        super("pos1");
        this.setPermission("labyrinth.pos1");
        this.setHelpMsg(ChatColor.GOLD + this.getName() + ": " +
                        ChatColor.WHITE + "Sets Labyrinth wand's pos1");
        this.plugin = plugin;
        this.config = new ConfigHandler(plugin);
        this.wandHandler = wandHandler;
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        // Retrieve info about event
        Wand wand = this.wandHandler.getWand(player.getUniqueId());
        wand.setPos1(player.getLocation().getBlock(), player);
    }
}
