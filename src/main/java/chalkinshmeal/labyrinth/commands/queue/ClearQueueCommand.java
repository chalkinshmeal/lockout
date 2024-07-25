package chalkinshmeal.labyrinth.commands.queue;

import chalkinshmeal.labyrinth.artifacts.labyrinth.LabyrinthHandler;
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

public class ClearQueueCommand extends ArgCommand {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final LabyrinthHandler labyrinthHandler;

    // Constructor
    public ClearQueueCommand(JavaPlugin plugin, LabyrinthHandler labyrinthHandler) {
        super("clear-queue", true);
        this.setPermission("labyrinth.clear-queue");
        setPlayerRequired(true);
        this.setHelpMsg(ChatColor.GOLD + this.getName() + " [labyrinth name]: " +
                        ChatColor.WHITE + "Clears all queues in an Labyrinth");
        this.plugin = plugin;
        this.config = new ConfigHandler(plugin);
        this.labyrinthHandler = labyrinthHandler;

        addArg(new Argument("labyrinth", ArgType.STRING, labyrinthHandler.getLabyrinthNames()));
    }

    @Override
    protected void executeArgs(CommandSender _sender, List<ArgValue> argValues, Set<String> usedFlags) {
        // Check if this is a player. If not, die.
        if (isPlayerRequired() && !(_sender instanceof Player)) {
            _sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return;
        }
        Player player = (Player) _sender;

        // Create labyrinth
        String labyrinthName = argValues.get(0).get();
        this.labyrinthHandler.clearQueues(player, labyrinthName);
    }
}
