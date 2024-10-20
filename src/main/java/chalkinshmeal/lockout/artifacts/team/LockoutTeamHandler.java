package chalkinshmeal.lockout.artifacts.team;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class LockoutTeamHandler {
    private LinkedHashMap<String, HashSet<Player>> teams = new LinkedHashMap<>();
    private LinkedHashMap<String, Material> teamMaterials = new LinkedHashMap<>();

    public LockoutTeamHandler() {
        // Initialize default teams
        this.teams.put("Team 1", new HashSet<>());
        this.teams.put("Team 2", new HashSet<>());
        this.teams.put("Team 3", new HashSet<>());
        this.teams.put("Team 4", new HashSet<>());

        this.teamMaterials.put("Team 1", Material.BLUE_WOOL);
        this.teamMaterials.put("Team 2", Material.GREEN_WOOL);
        this.teamMaterials.put("Team 3", Material.RED_WOOL);
        this.teamMaterials.put("Team 4", Material.MAGENTA_WOOL);
    }
    
    //---------------------------------------------------------------------------------------------
    // Accessor/Mutator methods
    //---------------------------------------------------------------------------------------------
    // Materials
    public List<Material> getTeamMaterials() { return new ArrayList<>(this.teamMaterials.values()); }

    // Teams
    public void addTeam(String teamName, Material material) {
        this.teams.put(teamName, new HashSet<>());
        this.teamMaterials.put(teamName, material);
    }

    public int getNumTeams() { return this.teams.size(); }
    public HashSet<Player> getTeamPlayers(String teamName) { return this.teams.get(teamName); }
    public HashSet<Player> getTeamPlayers(int teamIndex) { return new ArrayList<>(this.teams.values()).get(teamIndex); }
    public List<String> getTeamNames() { return new ArrayList<>(this.teams.keySet()); }
    public String getTeamName(Player player) {
        System.out.println("[LockoutTeamHandler::getTeamName] Fetching team name for player " + player);
        for (String teamName : this.teams.keySet()) {
            System.out.println("- " + teamName);
            if (this.teams.get(teamName).contains(player)) return teamName;
        }
        System.out.println("COULDN'T FIND");
        return null;
    }
    public String getTeamName(int teamIndex) {
        return new ArrayList<>(this.teams.keySet()).get(teamIndex);
    }

    public Integer getTeamIndex(Player player) {
        int i = 0;
        for (String teamName : this.teams.keySet()) {
            if (this.teams.get(teamName).contains(player)) return i;
            i += 1;
        }
        return null;
    }

    public Integer getTeamIndex(String teamName) {
        int i = 0;
        for (String _teamName : this.teams.keySet()) {
            if (teamName.equals(_teamName)) return i;
            i += 1;
        }
        return null;
    }

    public int GetPlayerCount(int teamIndex) { return this.getTeamPlayers(teamIndex).size(); }
    public List<String> getPlayerNames(int teamIndex) {
        List<HashSet<Player>> teamPlayerList = new ArrayList<>(this.teams.values());
        HashSet<Player> teamPlayers = teamPlayerList.get(teamIndex);
        List<String> playerNames = new ArrayList<>();
        for (Player player : teamPlayers) playerNames.add(player.getName());
        return playerNames; 
    }
    public void addPlayer(Player player, String teamName) {
        if (!this.teams.containsKey(teamName)) return;

        this.teams.get(teamName).add(player);
    }
    public void addPlayer(Player player, int teamIndex) {
        if (teamIndex < 0) return;
        if (teamIndex >= this.teams.size()) return;

        String teamName = this.getTeamName(teamIndex);
        this.teams.get(teamName).add(player);
    }
    public void removePlayer(Player player) {
        String teamName = this.getTeamName(player);
        if (teamName == null) return;

        this.teams.get(teamName).remove(player);
    }

    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        for (String teamName : this.teams.keySet()) {
            for (Player player : this.teams.get(teamName)) {
                players.add(player);
            }
        }
        return players;
    }

}
