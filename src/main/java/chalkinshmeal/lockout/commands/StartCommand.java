package chalkinshmeal.lockout.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.game.GameHandler;
import chalkinshmeal.lockout.utils.cmdframework.command.BaseCommand;
import chalkinshmeal.lockout.utils.cmdframework.handler.CommandHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class StartCommand extends BaseCommand {
    private final GameHandler gameHandler;

    // Constructor
    public StartCommand(JavaPlugin plugin, CommandHandler cmdHandler, GameHandler gameHandler) {
        super("start");
        this.setPlayerRequired(false);
        this.setHelpMsg(Component.text()
            .append(Component.text(this.getName() + ": ", NamedTextColor.GOLD))
            .append(Component.text("Starts a Lockout game", NamedTextColor.WHITE))
            .build());
        
        this.gameHandler = gameHandler;
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        this.gameHandler.start();
    }
}
