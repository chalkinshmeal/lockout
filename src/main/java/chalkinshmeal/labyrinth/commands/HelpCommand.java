package chalkinshmeal.labyrinth.commands;

import chalkinshmeal.labyrinth.cmdframework.command.BaseCommand;
import chalkinshmeal.labyrinth.cmdframework.command.ParentCommand;
import chalkinshmeal.labyrinth.cmdframework.handler.CommandHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class HelpCommand extends BaseCommand {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final CommandHandler cmdHandler;

    // Constructor
    public HelpCommand(JavaPlugin plugin, CommandHandler cmdHandler) {
        super("help");
        setPlayerRequired(false);
        this.setHelpMsg(ChatColor.GOLD + this.getName() + ": " +
                ChatColor.WHITE + "Shows help message");
        this.plugin = plugin;
        this.config = new ConfigHandler(plugin);
        this.cmdHandler = cmdHandler;
    }

    /** /pedestal add-item [name] [compatible-item1:effect] [compatible-item2] ... */
    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.YELLOW + "--------- " + ChatColor.WHITE + "SuperCraftBros Help " + ChatColor.YELLOW + "---------");
        for (BaseCommand cmd : this.cmdHandler.getSortedCommands()) {
            if (cmd instanceof ParentCommand) {
                for (BaseCommand child_cmd : ((ParentCommand) cmd).getSortedChildren()) {
                    if (child_cmd.getHelpMsg() != null)
                        sender.sendMessage(ChatColor.GOLD + "/" +
                            cmd.getName() + " " + child_cmd.getHelpMsg());
                }
            }
            else if (cmd.getHelpMsg() != null)
                sender.sendMessage(ChatColor.GOLD + "/" + cmd.getHelpMsg());
        }
    }
}
