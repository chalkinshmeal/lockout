package chalkinshmeal.lockout.artifacts.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import chalkinshmeal.lockout.artifacts.compass.LockoutCompass;
import chalkinshmeal.lockout.artifacts.tasks.LockoutTaskHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameHandler {
    private final JavaPlugin plugin;
    private final LockoutCompass lockoutCompass;
    private final LockoutTaskHandler lockoutTaskHandler;
    private final int maxTeams = 9;
    private boolean isActive = false;
    private Map<Integer, HashSet<UUID>> teams;

    //---------------------------------------------------------------------------------------------
    // Constructor
    //---------------------------------------------------------------------------------------------
    public GameHandler(JavaPlugin plugin, LockoutCompass lockoutCompass, LockoutTaskHandler lockoutTaskHandler) {
        this.plugin = plugin;
        this.lockoutCompass = lockoutCompass;
        this.lockoutTaskHandler = lockoutTaskHandler;
        this.teams = new HashMap<Integer, HashSet<UUID>>();
        for (int i = 0; i < this.maxTeams; i++) this.teams.put(i, new HashSet<UUID>());
    }

    //---------------------------------------------------------------------------------------------
    // Accessor/Mutator methods
    //---------------------------------------------------------------------------------------------
    public int GetPlayerCount(int teamIndex) { return this.teams.get(teamIndex).size(); }
    public List<String> GetPlayerNames(int teamIndex) {
        List<String> playerNames = new ArrayList<>();
        HashSet<UUID> team = this.teams.get(teamIndex);
        if (team == null) return playerNames;

        List<UUID> teamList = new ArrayList<UUID>(team);
        for (int i = 0; i < teamList.size(); i++) {
            playerNames.add(Bukkit.getPlayer(teamList.get(i)).getName());
        }
        System.out.println(playerNames);
        return playerNames;

    }
    public Integer GetTeam(UUID uuid) {
        for (int i = 0; i < this.teams.size(); i++) {
            if (this.teams.get(i).contains(uuid)) return i;
        }
        return null;
    }

    public void AddPlayer(UUID uuid, int teamIndex) {
        HashSet<UUID> team = this.teams.getOrDefault(teamIndex, null);
        if (team == null) return;

        team.add(uuid);
        this.lockoutCompass.updateTeamsInventory(this);
    }

    public void RemovePlayer(UUID uuid) {
        Integer teamIndex = this.GetTeam(uuid);
        if (teamIndex == null) return;

        this.teams.get(teamIndex).remove(uuid);
        this.lockoutCompass.updateTeamsInventory(this);
    }

    //---------------------------------------------------------------------------------------------
    // Game methods
    //---------------------------------------------------------------------------------------------
    public void start() {
        this.isActive = true;
        this.lockoutCompass.SetIsActive(true);
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("Starting game!");
        }

        this.lockoutCompass.updateTasksInventory(this.lockoutTaskHandler);
        this.lockoutTaskHandler.registerListeners();
    }

    public void stop() {
        this.isActive = false;
        this.lockoutCompass.SetIsActive(false);
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("Stopping game!");
        }

        this.lockoutTaskHandler.unRegisterListeners();
    }

    //---------------------------------------------------------------------------------------------
    // Listener methods
    //---------------------------------------------------------------------------------------------
    public void onInventoryClickEvent(InventoryClickEvent event) {
        String invName = ChatColor.stripColor(event.getView().getTitle());
        if (!invName.equals(this.lockoutCompass.GetTeamInvName())) return;
        int slot = event.getRawSlot();
        if (slot < 0) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        if (this.GetTeam(player.getUniqueId()) != null)
            this.RemovePlayer(player.getUniqueId());
        this.AddPlayer(player.getUniqueId(), slot);
        player.updateInventory();

        for (int i = 0; i < this.maxTeams; i++) {
            System.out.println("Team " + i + ": " + this.teams.get(i).size());
        }
    }
}