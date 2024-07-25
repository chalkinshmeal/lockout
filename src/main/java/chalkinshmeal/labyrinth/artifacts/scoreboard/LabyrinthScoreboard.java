package chalkinshmeal.labyrinth.artifacts.scoreboard;

import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class LabyrinthScoreboard {
    private JavaPlugin plugin;
    private ConfigHandler config;
    private ScoreboardManager scoreboardManager;
    private Scoreboard scoreboard;
    private Objective lifeObjective;
    private List<Objective> healthObjectives;
    private Integer maxLives;

    public LabyrinthScoreboard(JavaPlugin plugin, String labyrinthName) {
        this.plugin = plugin;
        this.config = new ConfigHandler(plugin);
        this.scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = this.scoreboardManager.getNewScoreboard();
        this.lifeObjective = this.scoreboard.registerNewObjective(ChatColor.GREEN + labyrinthName, "dummy");
        this.maxLives = this.getMaxLivesFromConfig();

        this.lifeObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.healthObjectives = new ArrayList<>();
    }

    public int getLives(Player player) { return this.lifeObjective.getScore(player).getScore(); }
    public void setLives(Player player, int lives) {
        // <<<<<<<<<<<<<<<<<<< SETTING THIS SCORE IS NOT WORKING FOR SOME REASON >>>>>>>>>>>>>>>
        // ATTEMPT TO REVERT TO 1.20.2???
        this.lifeObjective.getScore(player.getName()).setScore(lives);
    }
    public void addTeam(String teamName) {
        this.scoreboard.registerNewTeam(teamName);
    }

    public void addPlayer(String teamName, Player player) {
        // Add player to their team
        Team team = this.scoreboard.getTeam(teamName);
        team.addPlayer(player);

        // Reset their score
        this.lifeObjective.getScore(player).setScore(this.maxLives);

        Objective healthObjective = this.scoreboard.registerNewObjective(player.getName(), "health", "");
        healthObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        this.healthObjectives.add(healthObjective);

        // Set player's scoreboard
        player.setScoreboard(this.scoreboard);
    }
    public void removePlayer(Player player) {
        // Remove player from team
        Team team = this.scoreboard.getPlayerTeam(player);
        if (team != null) team.removePlayer(player);

        // Reset player's score
        this.lifeObjective.getScore(player).setScore(0);

        // Reset player's scoreboard
        player.setScoreboard(this.scoreboardManager.getNewScoreboard());
    }

    public void recordLostLife(Player player) {
        int newLifeCount = this.lifeObjective.getScore(player).getScore() - 1;
        this.lifeObjective.getScore(player).setScore(newLifeCount);
    }

    // Config
    public int getMaxLivesFromConfig() {
        if (!config.doesKeyExist("max_lives")) { return 3; }
        return (int) config.getValue("max_lives");
    }
}
