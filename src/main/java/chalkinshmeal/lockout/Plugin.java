package chalkinshmeal.lockout;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.compass.LockoutCompass;
import chalkinshmeal.lockout.artifacts.game.GameHandler;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTaskHandler;
import chalkinshmeal.lockout.commands.CompassCommand;
import chalkinshmeal.lockout.commands.HelpCommand;
import chalkinshmeal.lockout.commands.StartCommand;
import chalkinshmeal.lockout.commands.StopCommand;
import chalkinshmeal.lockout.data.ConfigHandler;
import chalkinshmeal.lockout.listeners.server.InventoryClickListener;
import chalkinshmeal.lockout.listeners.server.PlayerInteractListener;
import chalkinshmeal.lockout.listeners.server.PlayerJoinListener;
import chalkinshmeal.lockout.utils.cmdframework.command.ParentCommand;
import chalkinshmeal.lockout.utils.cmdframework.handler.CommandHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class Plugin extends JavaPlugin implements Listener {
	private CommandHandler cmdHandler;
    private ConfigHandler configHandler;
    private GameHandler gameHandler;
    private LockoutTaskHandler lockoutTaskHandler;
    private LockoutCompass lockoutCompass;


	@Override
	public void onEnable() {
		super.onEnable();
		this.cmdHandler = new CommandHandler(this);
        this.configHandler = new ConfigHandler(this);
        this.lockoutCompass = new LockoutCompass(this.configHandler);
        this.lockoutTaskHandler = new LockoutTaskHandler(this, this.configHandler, this.lockoutCompass);
        this.gameHandler = new GameHandler(this, this.lockoutCompass, this.lockoutTaskHandler);

		// Register commands + listeners
		registerCommands();
		registerListeners();

		// Log some debug information
        Component welcomeMsg = Component.text()
            .append(Component.text("Lockout successfully loaded", NamedTextColor.GOLD))
            .build();
		this.getServer().getConsoleSender().sendMessage(welcomeMsg);
	}

	/** Register all commands within the /command directory */
	private void registerCommands() {
		// Create command
		ParentCommand lockoutCmd = new ParentCommand("lockout");

        lockoutCmd.addChild(new CompassCommand(this, cmdHandler, lockoutCompass));
        lockoutCmd.addChild(new StartCommand(this, cmdHandler, gameHandler));
        lockoutCmd.addChild(new StopCommand(this, cmdHandler, gameHandler));
		lockoutCmd.addChild(new HelpCommand(this, cmdHandler));

		// Register command -> command handler
		this.cmdHandler.registerCommand(lockoutCmd);
	}

    //---------------------------------------------------------------------------------------------
	// Register all server-wide listeners
    //---------------------------------------------------------------------------------------------
	private void registerListeners() {
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents(new InventoryClickListener(this.gameHandler), this);
		manager.registerEvents(new PlayerInteractListener(this.lockoutCompass), this);
		manager.registerEvents(new PlayerJoinListener(this.lockoutCompass), this);
	}
}
