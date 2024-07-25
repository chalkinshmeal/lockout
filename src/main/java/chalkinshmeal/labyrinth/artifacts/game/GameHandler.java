package chalkinshmeal.labyrinth.artifacts.game;

import chalkinshmeal.labyrinth.artifacts.labyrinth.Labyrinth;
import chalkinshmeal.labyrinth.artifacts.labyrinth.LabyrinthHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GameHandler {
    private final JavaPlugin plugin;
    private final LabyrinthHandler labyrinthHandler;
    private List<Game> games;

    public GameHandler(JavaPlugin plugin, LabyrinthHandler labyrinthHandler) {
        this.plugin = plugin;
        this.labyrinthHandler = labyrinthHandler;
        this.games = new ArrayList<>();
    }

    //-------------------------------------------------------------------------
    // Getters
    //-------------------------------------------------------------------------
    public List<String> getLabyrinthNames() { return this.labyrinthHandler.getLabyrinthNames(); }
    public int getNumGames() { return this.games.size(); }
    public List<Game> getGames() { return this.games; }
    public Game getGameByName(String name) {
        for (Game g : this.games) { if (g.getName().equalsIgnoreCase(name)) return g; }
        return null;
    }
    public Game getGameByPlayer(Player player) {
        for (Game g : this.games) { if (g.isPlayerInGame(player)) { return g; } }
        return null;
    }

    //-------------------------------------------------------------------------
    // Game Operations
    //-------------------------------------------------------------------------
    public String createGame(Player player, String labyrinthName) {
        Labyrinth labyrinth = this.labyrinthHandler.getLabyrinthByName(labyrinthName);
        if (labyrinth == null) return null;

        // Check if labyrinth is valid
        if (labyrinth.getQueueCount() == 0) {
            player.sendMessage(ChatColor.RED + "Could not create game: Labyrinth '" + labyrinthName + "' has no queues");
            return null;
        }
        if (labyrinth.getSpawnCount() == 0) {
            player.sendMessage(ChatColor.RED + "Could not create game: Labyrinth '" + labyrinthName + "' has no spawns");
            return null;
        }

        Game game = new Game(this.plugin, labyrinth);
        this.games.add(game);

        return game.getName();
    }
    public void joinGame(Player player, String labyrinthName) {
        // Clear empty games
        List<Game> games = new ArrayList<>();
        for (Game game : this.games) {
            if (!game.getGameState().equals(GameState.DONE)) games.add(game);
        }
        this.games = games;

        // If player in a game, do not join
        if (this.getGameByPlayer(player) != null) {
            player.sendMessage(ChatColor.RED + "You are already part of a game! Leave it first.");
            return;
        }

        Game game = this.getGameByName(labyrinthName);

        // If game not created already, create it
        if (game == null) {
            String gameName = this.createGame(player, labyrinthName);
            game = this.getGameByName(gameName);
            if (game == null) {
                player.sendMessage(ChatColor.RED + "Labyrinth '" + ChatColor.GOLD + labyrinthName + ChatColor.RED + "' does not exist!");
                return;
            }
        }

        // Add player to game
        game.joinGame(player);
    }
    public void readyUp(Player player) {
        Game game = this.getGameByPlayer(player);
        if (game == null) {
            player.sendMessage(ChatColor.RED + "You are not part of a game!");
            return;
        }
        game.readyUp(player);
    }
    public void toggleAreItemsEnabled(Player player) {
        Game game = this.getGameByPlayer(player);
        if (game == null) {
            player.sendMessage(ChatColor.RED + "You are not part of a game!");
            return;
        }
        game.toggleIsItemModeEnabled();
    }
    public void startGame(Player player) {
        Game game = this.getGameByPlayer(player);
        if (game == null) {
            player.sendMessage(ChatColor.RED + "You are not part of a game!");
            return;
        }
        game.startGame();
    }
    public void leaveGame(Player player) {
        Game game = this.getGameByPlayer(player);
        if (game == null) {
            player.sendMessage(ChatColor.RED + "You are not part of a game!");
            return;
        }

        game.leaveGame(player);
        if (game.getPlayerCount() == 0) {
            this.games.remove(game);
            game.removeSummonsFromLabyrinth();
        }
    }

    //-------------------------------------------------------------------------
    // Utilities
    //-------------------------------------------------------------------------
    public void setClassSelection(Player player, String className) {
        Game game = this.getGameByPlayer(player);
        if (game == null) {
            player.sendMessage(ChatColor.RED + "You are not part of a game!");
            return;
        }
        game.setClassSelection(player, className);
    }
    public void leaveGameOnQuit(PlayerQuitEvent event) {
        Game game = this.getGameByPlayer(event.getPlayer());
        if (game == null) return;
        game.leaveGame(event.getPlayer());
    }
}