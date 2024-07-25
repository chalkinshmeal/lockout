package chalkinshmeal.labyrinth.commands.labyrinth;

import chalkinshmeal.labyrinth.artifacts.labyrinth.LabyrinthHandler;
import chalkinshmeal.labyrinth.artifacts.wand.WandHandler;
import chalkinshmeal.labyrinth.cmdframework.argument.ArgType;
import chalkinshmeal.labyrinth.cmdframework.argument.ArgValue;
import chalkinshmeal.labyrinth.cmdframework.argument.Argument;
import chalkinshmeal.labyrinth.cmdframework.command.ArgCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AddLabyrinthCommand extends ArgCommand {
    private final JavaPlugin plugin;
    private final LabyrinthHandler labyrinthHandler;
    private final WandHandler wandHandler;

    // Constructor
    public AddLabyrinthCommand(JavaPlugin plugin, WandHandler wandHandler, LabyrinthHandler labyrinthHandler) {
        super("add-labyrinth", true);
        this.setPermission("labyrinth.add-labyrinth");
        setPlayerRequired(true);
        this.setHelpMsg(ChatColor.GOLD + this.getName() + " [name]: " +
                        ChatColor.WHITE + "Adds an labyrinth with 'name'");
        this.plugin = plugin;
        this.labyrinthHandler = labyrinthHandler;
        this.wandHandler = wandHandler;

        List<String> dummyNameList = new ArrayList<>(List.of("[labyrinth name]"));
        addArg(new Argument("name", ArgType.STRING, dummyNameList));
    }

    @Override
    protected void executeArgs(CommandSender _sender, List<ArgValue> argValues, Set<String> usedFlags) {
        // Check if this is a player. If not, die.
        if (isPlayerRequired() && !(_sender instanceof Player)) {
            _sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return;
        }
        Player player = (Player) _sender;

        // Exit if pos1 + pos2 not set yet
        if (this.wandHandler.getWand(player.getUniqueId()).getPos1() == null ||
                this.wandHandler.getWand(player.getUniqueId()).getPos2() == null) {
            player.sendMessage(ChatColor.RED + "Wand positions not set yet. Cannot create labyrinth.");
            return;
        }

        // Create labyrinth
        String labyrinthName = argValues.get(0).get();
        this.labyrinthHandler.addLabyrinth(player, labyrinthName,
                                   this.wandHandler.getWand(player.getUniqueId()).getPos1().getLocation(),
                                   this.wandHandler.getWand(player.getUniqueId()).getPos2().getLocation());
    }
}
