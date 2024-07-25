package chalkinshmeal.labyrinth.commands.game;

import chalkinshmeal.labyrinth.artifacts.classes.LabyrinthClassHandler;
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

public class SelectClassCommand extends ArgCommand {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final GameHandler gameHandler;
    private final LabyrinthClassHandler labyrinthClassHandler;

    // Constructor
    public SelectClassCommand(JavaPlugin plugin, GameHandler gameHandler, LabyrinthClassHandler labyrinthClassHandler) {
        super("select", false);
        setPlayerRequired(true);
        this.setHelpMsg(ChatColor.GOLD + this.getName() + ": " +
                        ChatColor.WHITE + "Selects Labyrinth class specified for the current game");
        this.plugin = plugin;
        this.config = new ConfigHandler(plugin);
        this.gameHandler = gameHandler;
        this.labyrinthClassHandler = labyrinthClassHandler;

        addArg(new Argument("class", ArgType.STRING));
    }

    @Override
    protected void executeArgs(CommandSender sender, List<ArgValue> argValues, Set<String> usedFlags) {
        Player player = (Player) sender;
        String className = argValues.get(0).get();

        // Give player an item
        this.gameHandler.setClassSelection(player, className);
    }
}
