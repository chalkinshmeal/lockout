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
        if (this.gameHandler.isActive) {
            sender.sendMessage(
                Component.text("Lockout ", NamedTextColor.GOLD)
                    .append(Component.text("game is already in progress", NamedTextColor.GRAY)));
            return;
        }
        if (this.gameHandler.getNumTeams() <= 0) {
            sender.sendMessage(
                Component.text("Create at least 1 team to start a ", NamedTextColor.GRAY)
                    .append(Component.text("Lockout ", NamedTextColor.GOLD))
                    .append(Component.text("game", NamedTextColor.GRAY)));
            return;
        }
        this.gameHandler.queue();
    }
}
