package chalkinshmeal.lockout;

import static chalkinshmeal.lockout.utils.Book.*;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.game.GameHandler;
import chalkinshmeal.lockout.artifacts.server.ServerEventHandler;
import chalkinshmeal.lockout.commands.HelpCommand;
import chalkinshmeal.lockout.commands.game.*;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.listeners.server.PlayerJoinListener;
import chalkinshmeal.lockout.utils.cmdframework.command.ParentCommand;
import chalkinshmeal.lockout.utils.cmdframework.handler.CommandHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@SuppressWarnings("deprecation")
public class Plugin extends JavaPlugin implements Listener {
	private CommandHandler cmdHandler;
	//private ConfigHandler configHandler;
	private GameHandler gameHandler;
	private ServerEventHandler serverEventHandler;
    private Component welcomeMsg = Component.text()
        .append(Component.text("Lockout successfully loaded", NamedTextColor.GOLD))
        .build();

	@Override
	public void onEnable() {
		super.onEnable();
		this.cmdHandler = new CommandHandler(this);
		//this.configHandler = new ConfigHandler(this);
		this.gameHandler = new GameHandler(this);
		this.serverEventHandler = new ServerEventHandler();

		// Register commands + listeners
		registerCommands();
		registerListeners();

		// Log some debug information
		this.getServer().getConsoleSender().sendMessage(this.welcomeMsg);
	}

	/** Register all commands within the /command directory */
	private void registerCommands() {
		// Create command
		ParentCommand labyrinthCmd = new ParentCommand("labyrinth");

		// Game commands
		labyrinthCmd.addChild(new ListGameCommand(this, gameHandler));

		// Others
		labyrinthCmd.addChild(new HelpCommand(this, cmdHandler));

		// Register command -> command handler
		this.cmdHandler.registerCommand(labyrinthCmd);
	}

	/** Register all listeners within the /listeners directory */
	private void registerListeners() {
		// Register listener -> server
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents(new PlayerJoinListener(), this);
	}

	public void sendUpdate(PlayerJoinEvent event) {
		ItemStack book = createBook();
		book = addText(book, 1, "  --- Changelog ---\n", ChatColor.GOLD);
		book = addText(book, 1, "+ ", ChatColor.DARK_GREEN);
		book = addText(book,1, "Upgraded to 1.20.4\n", ChatColor.BLACK);
		book = addText(book, 1, "+ ", ChatColor.DARK_GREEN);
		book = addText(book,1, "Added 2s of on-death dmg immunity\n", ChatColor.BLACK);
		book = addText(book, 1, "+ ", ChatColor.DARK_GREEN);
		book = addText(book, 1,"Added Bat class\n", ChatColor.BLACK);
		book = addText(book, 1, "+ ", ChatColor.DARK_GREEN);
		book = addText(book, 1,"Added Piglin abilities\n", ChatColor.BLACK);
		book = addText(book, 1, "+ ", ChatColor.DARK_GREEN);
		book = addText(book, 1,"Added Spider abilities\n", ChatColor.BLACK);
		book = addText(book, 1, "+ ", ChatColor.DARK_GREEN);
		book = addText(book, 1,"Added changelog\n", ChatColor.BLACK);
		book = addText(book, 1, "- ", ChatColor.DARK_RED);
		book = addText(book, 1,"Added Spider web duration\n", ChatColor.BLACK);
		book = addText(book, 1, "- ", ChatColor.DARK_RED);
		book = addText(book, 1,"Spawn went missing\n", ChatColor.BLACK);
		book = addText(book, 1, "o ", ChatColor.AQUA);
		book = addText(book, 1,"Fixed Allay\n", ChatColor.BLACK);
		book = setTitle(book, ChatColor.GOLD + "Changelog");
		book = setAuthor(book, ChatColor.DARK_PURPLE + "Spoingus");

		event.getPlayer().getInventory().clear();
		event.getPlayer().getInventory().setItem(0, book);
	}
}
