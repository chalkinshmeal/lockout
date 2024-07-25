package chalkinshmeal.labyrinth.commands.labyrinth;

import chalkinshmeal.labyrinth.artifacts.labyrinth.Labyrinth;
import chalkinshmeal.labyrinth.artifacts.labyrinth.LabyrinthHandler;
import chalkinshmeal.labyrinth.cmdframework.command.BaseCommand;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ListLabyrinthCommand extends BaseCommand {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final LabyrinthHandler labyrinthHandler;

    // Constructor
    public ListLabyrinthCommand(JavaPlugin plugin, LabyrinthHandler labyrinthHandler) {
        super("list-labyrinth");
        this.setHelpMsg(ChatColor.GOLD + this.getName() + ": " +
                        ChatColor.WHITE + "Lists created labyrinths");
        this.plugin = plugin;
        this.config = new ConfigHandler(plugin);
        this.labyrinthHandler = labyrinthHandler;
    }

    @Override
    protected void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (this.labyrinthHandler.getNumLabyrinths() == 0) {
            player.sendMessage(ChatColor.RED + "No labyrinths have been created yet");
            return;
        }
        player.sendMessage(ChatColor.YELLOW + "--- Labyrinths (" + ChatColor.GREEN + "" + this.labyrinthHandler.getNumLabyrinths() + ChatColor.YELLOW + ") ---");
        for (Labyrinth a : this.labyrinthHandler.getLabyrinthsSortedByName()) {
            String spawnZero = "";
            if (a.getSpawnCount() != 0) {
                spawnZero = ChatColor.GRAY + ", [" + (int) a.getSpawn(0).getX() + ", " +
                        ChatColor.GRAY + (int) a.getSpawn(0).getY() + ", " +
                        ChatColor.GRAY + (int) a.getSpawn(0).getZ() + "]";
            }
            player.sendMessage(ChatColor.GREEN + "" + a.getName() +
                                ChatColor.GRAY + " (" + a.getSpawnCount() + " spawns, " +
                                ChatColor.GRAY + a.getQueueCount() + " queues, " +
                                ChatColor.GRAY + a.getChestCount() + " chests" +
                                spawnZero + ")");
        }
    }
}
