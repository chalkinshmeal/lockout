package chalkinshmeal.labyrinth.commands.game;

import chalkinshmeal.labyrinth.artifacts.game.GameHandler;
import chalkinshmeal.labyrinth.cmdframework.argument.ArgValue;
import chalkinshmeal.labyrinth.cmdframework.command.ArgCommand;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;

public class StartGameCommand extends ArgCommand {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final GameHandler gameHandler;

    // Constructor
    public StartGameCommand(JavaPlugin plugin, GameHandler gameHandler) {
        super("force-start", false);
        this.setPermission("labyrinth.force-start");
        setPlayerRequired(true);
        this.setHelpMsg(ChatColor.GOLD + this.getName() + ": " +
                        ChatColor.WHITE + "Force starts game player is currently in");
        this.plugin = plugin;
        this.config = new ConfigHandler(plugin);
        this.gameHandler = gameHandler;
    }

    @Override
    protected void executeArgs(CommandSender sender, List<ArgValue> argValues, Set<String> usedFlags) {
        Player player = (Player) sender;

        // Add player to the game
        this.gameHandler.startGame(player);
    }
}
