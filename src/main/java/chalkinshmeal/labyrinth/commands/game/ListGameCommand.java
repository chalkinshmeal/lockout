package chalkinshmeal.labyrinth.commands.game;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.game.GameHandler;
import chalkinshmeal.labyrinth.cmdframework.command.BaseCommand;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
        Player player = (Player) sender;

        if (this.gameHandler.getNumGames() == 0) {
            player.sendMessage(ChatColor.RED + "No games are active");
            return;
        }
        player.sendMessage(ChatColor.YELLOW + "--- Games ---");
        for (Game g : this.gameHandler.getGames()) {
            player.sendMessage(
                ChatColor.GREEN + "" + g.getName() +
                ChatColor.GRAY + " (" + g.getPlayerCount() + "/" + g.getMaxPlayerCount() + " players)");
        }
    }
}
