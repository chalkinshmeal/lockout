package chalkinshmeal.lockout.commands.game;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.game.Game;
import chalkinshmeal.lockout.artifacts.game.GameHandler;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.utils.cmdframework.command.BaseCommand;

public class ListGameCommand extends BaseCommand {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final GameHandler gameHandler;

    // Constructor
    public ListGameCommand(JavaPlugin plugin, GameHandler gameHandler) {
        super("list-game");
        this.setHelpMsg(ChatColor.GOLD + this.getName() + ": " +
                        ChatColor.WHITE + "Lists available games to join");
        this.plugin = plugin;
        this.config = new ConfigHandler(plugin);
        this.gameHandler = gameHandler;
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        //Player player = (Player) sender;

        //if (this.gameHandler.getNumGames() == 0) {
        //    player.sendMessage(ChatColor.RED + "No games are active");
        //    return;
        //}
        //player.sendMessage(ChatColor.YELLOW + "--- Games ---");
        //for (Game g : this.gameHandler.getGames()) {
        //    player.sendMessage(
        //        ChatColor.GREEN + "" + g.getName() +
        //        ChatColor.GRAY + " (" + g.getPlayerCount() + "/" + g.getMaxPlayerCount() + " players)");
        //}
    }
}
