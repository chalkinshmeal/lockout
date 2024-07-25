package chalkinshmeal.labyrinth;

import chalkinshmeal.labyrinth.artifacts.labyrinth.LabyrinthHandler;
import chalkinshmeal.labyrinth.artifacts.classes.LabyrinthClassHandler;
import chalkinshmeal.labyrinth.artifacts.game.GameHandler;
import chalkinshmeal.labyrinth.artifacts.server.ServerEventHandler;
import chalkinshmeal.labyrinth.artifacts.wand.WandHandler;
import chalkinshmeal.labyrinth.cmdframework.command.ParentCommand;
import chalkinshmeal.labyrinth.cmdframework.handler.CommandHandler;
import chalkinshmeal.labyrinth.commands.HelpCommand;
import chalkinshmeal.labyrinth.commands.chest.AddChestCommand;
import chalkinshmeal.labyrinth.commands.chest.ClearChestCommand;
import chalkinshmeal.labyrinth.commands.labyrinth.AddLabyrinthCommand;
import chalkinshmeal.labyrinth.commands.labyrinth.ListLabyrinthCommand;
import chalkinshmeal.labyrinth.commands.labyrinth.RemoveLabyrinthCommand;
import chalkinshmeal.labyrinth.commands.game.*;
import chalkinshmeal.labyrinth.commands.queue.AddQueueCommand;
import chalkinshmeal.labyrinth.commands.queue.ClearQueueCommand;
import chalkinshmeal.labyrinth.commands.spawn.AddSpawnCommand;
import chalkinshmeal.labyrinth.commands.spawn.ClearSpawnCommand;
import chalkinshmeal.labyrinth.commands.wand.Pos1Command;
import chalkinshmeal.labyrinth.commands.wand.Pos2Command;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.listeners.server.ClickListener;
import chalkinshmeal.labyrinth.listeners.server.CreatureSpawnListener;
import chalkinshmeal.labyrinth.listeners.server.OnPlayerJoinListener;
import chalkinshmeal.labyrinth.listeners.server.OnPlayerQuitListener;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import static chalkinshmeal.labyrinth.artifacts.labyrinth.LabyrinthHandler.getLabyrinthsFromConfig;
import static chalkinshmeal.labyrinth.utils.Book.*;

@SuppressWarnings("deprecation")
public class Plugin extends JavaPlugin implements Listener {
	private CommandHandler cmdHandler;
	private ConfigHandler configHandler;
	private WandHandler wandHandler;
	private LabyrinthHandler labyrinthHandler;
	private GameHandler gameHandler;
	private LabyrinthClassHandler labyrinthClassHandler;
	private ServerEventHandler serverEventHandler;

	@Override
	public void onEnable() {
		super.onEnable();
		this.cmdHandler = new CommandHandler(this);
		this.configHandler = new ConfigHandler(this);
		this.wandHandler = new WandHandler();
		this.labyrinthHandler = new LabyrinthHandler(this, configHandler, cmdHandler);
		this.labyrinthHandler.labyrinths = getLabyrinthsFromConfig(this, configHandler, "labyrinths");
		this.gameHandler = new GameHandler(this, labyrinthHandler);
		this.labyrinthClassHandler = new LabyrinthClassHandler(this, null, null, null);
		this.serverEventHandler = new ServerEventHandler();

		// Register commands + listeners
		registerCommands();
		registerListeners();

		// Log some debug information
		this.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "Labyrinth successfully loaded!");
		this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "- Loaded " + this.labyrinthHandler.getNumLabyrinths() + " labyrinths");
		this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "- Loaded " + this.labyrinthClassHandler.getClassNames().size() + " classes");
		this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "- Loaded " + this.labyrinthClassHandler.getLabyrinthItemHandler().getTotalNumItems() + " items");
	}

	/** Register all commands within the /command directory */
	private void registerCommands() {
		// Create command
		ParentCommand labyrinthCmd = new ParentCommand("labyrinth");

		// Debug commands
		labyrinthCmd.addChild(new ClassCommand(this, labyrinthClassHandler));
		labyrinthCmd.addChild(new ItemCommand(this, labyrinthClassHandler));

		// Game commands
		labyrinthCmd.addChild(new ListGameCommand(this, gameHandler));
		labyrinthCmd.addChild(new JoinGameCommand(this, gameHandler));
		labyrinthCmd.addChild(new JoinAllGameCommand(this, gameHandler));
		labyrinthCmd.addChild(new SelectClassCommand(this, gameHandler, labyrinthClassHandler));
		labyrinthCmd.addChild(new ReadyUpCommand(this, gameHandler));
		labyrinthCmd.addChild(new StartGameCommand(this, gameHandler));
		labyrinthCmd.addChild(new LeaveGameCommand(this, gameHandler));

		// Labyrinth commands
		labyrinthCmd.addChild(new ListLabyrinthCommand(this, labyrinthHandler));
		labyrinthCmd.addChild(new AddLabyrinthCommand(this, wandHandler, labyrinthHandler));
		labyrinthCmd.addChild(new RemoveLabyrinthCommand(this, labyrinthHandler));

		// Spawn commands
		labyrinthCmd.addChild(new AddSpawnCommand(this, labyrinthHandler));
		labyrinthCmd.addChild(new ClearSpawnCommand(this, labyrinthHandler));

		// Queue commands
		labyrinthCmd.addChild(new AddQueueCommand(this, labyrinthHandler));
		labyrinthCmd.addChild(new ClearQueueCommand(this, labyrinthHandler));

		// Chest commands
		labyrinthCmd.addChild(new AddChestCommand(this, labyrinthHandler, wandHandler));
		labyrinthCmd.addChild(new ClearChestCommand(this, labyrinthHandler));

		// Others
		labyrinthCmd.addChild(new HelpCommand(this, cmdHandler));
		labyrinthCmd.addChild(new Pos1Command(this, wandHandler));
		labyrinthCmd.addChild(new Pos2Command(this, wandHandler));

		// Register command -> command handler
		this.cmdHandler.registerCommand(labyrinthCmd);
	}

	/** Register all listeners within the /listeners directory */
	private void registerListeners() {
		// Register listener -> server
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents(new ClickListener(this, wandHandler, gameHandler, labyrinthClassHandler, labyrinthHandler), this);
		manager.registerEvents(new CreatureSpawnListener(serverEventHandler), this);
		manager.registerEvents(new OnPlayerJoinListener(this, gameHandler), this);
		manager.registerEvents(new OnPlayerQuitListener(this, gameHandler), this);
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
