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

public class ItemCommand extends ArgCommand {
    private final JavaPlugin plugin;
    private final LabyrinthClassHandler labyrinthClassHandler;

    // Constructor
    public ItemCommand(JavaPlugin plugin, LabyrinthClassHandler labyrinthClassHandler) {
        super("item", false);
        setPlayerRequired(true);
        this.setHelpMsg(ChatColor.GOLD + this.getName() + ": " + ChatColor.WHITE + "Gives Labyrinth item associated with a given specialID");
        this.plugin = plugin;
        this.labyrinthClassHandler = labyrinthClassHandler;

        addArg(new Argument("item", ArgType.STRING));
    }

    @Override
    protected void executeArgs(CommandSender sender, List<ArgValue> argValues, Set<String> usedFlags) {
        Player player = (Player) sender;
        int specialID = Integer.parseInt(argValues.get(0).get());

        // Give player an item
        player.getInventory().setItemInMainHand(this.labyrinthClassHandler.getLabyrinthItemHandler().getItem(specialID).getItemStack());
    }
}
