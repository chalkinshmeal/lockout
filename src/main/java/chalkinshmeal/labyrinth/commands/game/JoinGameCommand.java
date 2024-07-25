package chalkinshmeal.labyrinth.commands.game;

import chalkinshmeal.labyrinth.artifacts.game.GameHandler;
import chalkinshmeal.labyrinth.cmdframework.argument.ArgType;
import chalkinshmeal.labyrinth.cmdframework.argument.ArgValue;
import chalkinshmeal.labyrinth.cmdframework.argument.Argument;
import chalkinshmeal.labyrinth.cmdframework.command.ArgCommand;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;

public class JoinGameCommand extends ArgCommand {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final GameHandler gameHandler;

    // Constructor
    public JoinGameCommand(JavaPlugin plugin, GameHandler gameHandler) {
        super("join", false);
        setPlayerRequired(true);
        this.setHelpMsg(ChatColor.GOLD + this.getName() + ": " +
                        ChatColor.WHITE + "Joins game specified");
        this.plugin = plugin;
        this.config = new ConfigHandler(plugin);
        this.gameHandler = gameHandler;

        addArg(new Argument("game", ArgType.STRING, this.gameHandler.getLabyrinthNames()));
    }

    @Override
    protected void executeArgs(CommandSender sender, List<ArgValue> argValues, Set<String> usedFlags) {
        Player player = (Player) sender;
        String gameName = argValues.get(0).get();

        // Add player to the game
        this.gameHandler.joinGame(player, gameName);
    }
}
