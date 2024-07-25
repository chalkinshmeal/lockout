package chalkinshmeal.labyrinth.commands.game;

import chalkinshmeal.labyrinth.artifacts.classes.LabyrinthClassHandler;
import chalkinshmeal.labyrinth.cmdframework.argument.ArgType;
import chalkinshmeal.labyrinth.cmdframework.argument.ArgValue;
import chalkinshmeal.labyrinth.cmdframework.argument.Argument;
import chalkinshmeal.labyrinth.cmdframework.command.ArgCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;

public class ClassCommand extends ArgCommand {
    private final JavaPlugin plugin;
    private final LabyrinthClassHandler labyrinthClassHandler;

    // Constructor
    public ClassCommand(JavaPlugin plugin, LabyrinthClassHandler labyrinthClassHandler) {
        super("class", false);
        setPlayerRequired(true);
        this.setHelpMsg(ChatColor.GOLD + this.getName() + ": " + ChatColor.WHITE + "Gives Labyrinth kit specified");
        this.plugin = plugin;
        this.labyrinthClassHandler = labyrinthClassHandler;

        addArg(new Argument("class", ArgType.STRING, this.labyrinthClassHandler.getClassNames()));
    }

    @Override
    protected void executeArgs(CommandSender sender, List<ArgValue> argValues, Set<String> usedFlags) {
        Player player = (Player) sender;
        String className = argValues.get(0).get();

        // Give player an item
        this.labyrinthClassHandler.giveClassDebug(player, className);
    }
}
