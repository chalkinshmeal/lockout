package chalkinshmeal.labyrinth.artifacts.game;

import chalkinshmeal.labyrinth.artifacts.labyrinth.Labyrinth;
import chalkinshmeal.labyrinth.artifacts.bossbar.BossBarHandler;
import chalkinshmeal.labyrinth.artifacts.classes.LabyrinthClassHandler;
import chalkinshmeal.labyrinth.artifacts.cooldown.CooldownHandler;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItemHandler;
import chalkinshmeal.labyrinth.artifacts.scoreboard.LabyrinthScoreboard;
import chalkinshmeal.labyrinth.listeners.game.*;
import chalkinshmeal.labyrinth.listeners.queue.QueueEntityDamageListener;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.util.*;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.GameRule.*;

public class Game {
    private final JavaPlugin plugin;
    private final Labyrinth labyrinth;
    private final LabyrinthEventHandler labyrinthEventHandler;
    private final LabyrinthClassHandler labyrinthClassHandler;
    private final LabyrinthItemHandler labyrinthItemHandler;
    private final CooldownHandler cooldownHandler;
    private final BossBarHandler bossBarHandler;
    private final List<Listener> inQueueListeners = new ArrayList<>();
    private final List<Listener> inGameListeners = new ArrayList<>();

    // Previous information
    private final Map<UUID, String> classSelections = new HashMap<>();

    // Player queues
    private final List<UUID> queuedPlayerUUIDs = new ArrayList<>();
    private List<UUID> inGamePlayerUUIDs = new ArrayList<>();
    private final List<UUID> spectatingPlayerUUIDs = new ArrayList<>();

    // Game state
    private GameState gameState = GameState.QUEUE;
    private final Map<UUID, Boolean> isReady = new HashMap<>();
    private final LabyrinthScoreboard scoreboard;
    private final Map<UUID, Boolean> hasUltimate = new HashMap<>();
    private final Map<UUID, Boolean> isUltimateActive = new HashMap<>();
    private final Map<UUID, List<Entity>> summonedEntities = new HashMap<>();
    private final Map<UUID, Map<Block, Material>> summonedBlocks = new HashMap<>();

    public Game(JavaPlugin plugin, Labyrinth labyrinth) {
        this.plugin = plugin;
        this.labyrinth = labyrinth;
        this.cooldownHandler = new CooldownHandler(plugin);
        this.bossBarHandler = new BossBarHandler(plugin);
        this.labyrinthEventHandler = new LabyrinthEventHandler(plugin, this, cooldownHandler);
        this.labyrinthItemHandler = new LabyrinthItemHandler(this.plugin, this);
        this.labyrinthClassHandler = new LabyrinthClassHandler(plugin, this, cooldownHandler, labyrinthItemHandler);
        this.scoreboard = new LabyrinthScoreboard(plugin, this.labyrinth.getName());
        this.registerInQueueListeners();
    }

    // -------------------------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------------------------
    public Labyrinth getLabyrinth() { return this.labyrinth; }
    public CooldownHandler getCooldownHandler() { return this.cooldownHandler; }
    public LabyrinthEventHandler getLabyrinthEventHandler() { return this.labyrinthEventHandler; }
    public LabyrinthClassHandler getLabyrinthClassHandler() { return this.labyrinthClassHandler; }
    public LabyrinthItemHandler getLabyrinthItemHandler() { return this.labyrinthItemHandler; }
    public Map<UUID, List<Entity>> getSummonedEntities() { return this.summonedEntities; }
    public Map<UUID, String> getClassSelections() { return this.classSelections; }
    public List<UUID> getInGamePlayerUUIDs() { return this.inGamePlayerUUIDs; }
    public List<UUID> getPlayerUUIDs() {
        List<UUID> allPlayers = new ArrayList<>();
        allPlayers.addAll(this.queuedPlayerUUIDs);
        allPlayers.addAll(this.inGamePlayerUUIDs);
        allPlayers.addAll(this.spectatingPlayerUUIDs);
        return allPlayers;
    }
    public List<Player> getInGamePlayers() { return getPlayersFromUUIDs(this.plugin, this.inGamePlayerUUIDs); }
    public List<Player> getPlayers() { return getPlayersFromUUIDs(this.plugin, this.getPlayerUUIDs()); }
    public LabyrinthScoreboard getScoreboard() { return this.scoreboard; }
    public BossBarHandler getBossBarHandler() { return this.bossBarHandler; }
    public GameState getGameState() { return this.gameState; }
    public String getName() { return this.labyrinth.getName(); }
    public BoundingBox getBoundingBox() { return this.labyrinth.getBoundingBox(); }
    public Location getSpawn(int index) { return this.labyrinth.getSpawn(index); }
    public Location getRandSpawn() { return this.labyrinth.getRandSpawn(); }
    public Location getRandQueue() { return this.labyrinth.getRandQueue(); }
    public int getQueuedPlayerCount() { return this.queuedPlayerUUIDs.size(); }
    public int getReadyPlayerCount() {
        int readyCount = 0;
        for (boolean isReady : this.isReady.values()) {
            if (isReady) readyCount += 1;
        }
        return readyCount;
    }
    public int getInGamePlayerCount() {
        if (this.inGamePlayerUUIDs == null) return 0;
        return this.inGamePlayerUUIDs.size();
    }
    public int getMaxPlayerCount() { return this.labyrinth.getSpawnCount(); }
    public int getPlayerCount() {
        if (this.gameState == GameState.QUEUE) return this.getQueuedPlayerCount();
        else return this.getInGamePlayerCount();
    }
    public boolean doesPlayerHaveUltimate(Player player) { return this.hasUltimate.get(player.getUniqueId()); }
    public boolean isUltimateActive(Player player) { return this.isUltimateActive.get(player.getUniqueId()); }
    public boolean isPlayerInQueue(Player player) { return this.queuedPlayerUUIDs.contains(player.getUniqueId()); }
    public boolean isPlayerInPlay(Player player) { return this.inGamePlayerUUIDs.contains(player.getUniqueId()); }
    public boolean isPlayerSpectating(Player player) { return this.spectatingPlayerUUIDs.contains(player.getUniqueId()); }
    public boolean isPlayerInGame(Player player) {
        return this.isPlayerInQueue(player) || this.isPlayerInPlay(player) || this.isPlayerSpectating(player);
    }
    public boolean isGameDone() { return this.gameState.equals(GameState.DONE); }
    public boolean isEverybodyDeadExceptOne() {
        return (this.inGamePlayerUUIDs.size() <= 1);
    }
    public boolean isBlockASpawn(Location loc) { return this.labyrinth.isBlockASpawn(loc);}
    public boolean areAllPlayersReady() {
        for (boolean isReady : this.isReady.values()) {
            if (!isReady) return false;
        }
        return true;
    }
    public boolean isLocationInLabyrinth(Location location) { return this.labyrinth.isLocationInLabyrinth(location); }
    public void toggleIsItemModeEnabled() {
        for (Player p : this.getPlayers()) {
            if (this.labyrinthClassHandler.isItemModeEnabled()) { p.sendMessage(ChatColor.GRAY + "Items are now " + ChatColor.RED + "OFF"); }
            else { p.sendMessage(ChatColor.GRAY + "Items are now " + ChatColor.GREEN + "ON"); }
        }
        this.labyrinthClassHandler.toggleIsItemModeEnabled();
    }
    public void setUsedUltimate(Player player) {
        this.hasUltimate.put(player.getUniqueId(), false);
        this.isUltimateActive.put(player.getUniqueId(), true);
    }
    public void setUltimateInactive(Player player) {
        this.isUltimateActive.put(player.getUniqueId(), false);
    }
    public boolean doesPlayerHaveLives(Player player, int livesLeft) {
        return this.scoreboard.getLives(player) == livesLeft;
    }

    // -------------------------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------------------------
    public void setClassSelection(Player player, String className) {
        className = className.equalsIgnoreCase("random") ? this.labyrinthClassHandler.getRandClassName() : className;
        if (this.labyrinthClassHandler.getClassByName(className) == null) {
            player.sendMessage(ChatColor.RED + "Class " + ChatColor.GOLD + className + " " + ChatColor.RED + "does not exist!");
            return;
        }
        player.sendMessage(ChatColor.GRAY + "You selected class " + ChatColor.GOLD + "" + className);
        className = ChatColor.stripColor(className.strip().toLowerCase().replace(' ', '_'));
        this.classSelections.put(player.getUniqueId(), className);
    }
    public void setHasUltimate(UUID uuid, boolean state) { this.hasUltimate.put(uuid, state); }
    public void setIsUltimateActive(UUID uuid, boolean state) { this.isUltimateActive.put(uuid, state); }

    // -------------------------------------------------------------------------------------------
    // Game State
    // -------------------------------------------------------------------------------------------
    public void joinGame(Player player) {
        if (isPlayerInQueue(player) || isPlayerInPlay(player)) {
            player.sendMessage(ChatColor.RED + "Already in this game!");
            return;
        }

        // Set player state
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        player.setFreezeTicks(0);
        for (PotionEffect effect : player.getActivePotionEffects()) { player.removePotionEffect(effect.getType()); }
        this.teleportPlayerToQueue(player);

        // Add to queue and teleport to labyrinth
        this.queuedPlayerUUIDs.add(player.getUniqueId());
        this.isReady.put(player.getUniqueId(), false);

        for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
            onlinePlayer.sendMessage(
                ChatColor.GOLD + player.getName() + ChatColor.GRAY + " joined labyrinth " + ChatColor.GOLD + this.getName() +
                ChatColor.GRAY + " (" + this.getQueuedPlayerCount() + "/" + this.getMaxPlayerCount() + " players joined)");
        }
    }
    public void readyUp(Player player) {
        if (!this.classSelections.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Cannot ready up - you must choose a class!");
            return;
        }

        if (!this.isReady.get(player.getUniqueId())) {
            this.isReady.put(player.getUniqueId(), true);

            // Send "(1/4) players ready!" message to all queued players
            for (UUID uuid : this.queuedPlayerUUIDs) {
                Player queuePlayer = this.plugin.getServer().getPlayer(uuid);
                if (queuePlayer == null) continue;
                queuePlayer.sendMessage(ChatColor.GOLD + player.getName() + ChatColor.GRAY + " is ready (" + this.getReadyPlayerCount() + "/" + this.getQueuedPlayerCount() + " players ready)");
            }
        }

        if (this.areAllPlayersReady()) {
            this.startGame();
            this.countdown(this, 3);
            this.teleportAndGiveKitToPlayers(this);
        }
    }
    public void startGame() {
        // Transfer players from queue -> game
        this.inGamePlayerUUIDs = new ArrayList<>(this.queuedPlayerUUIDs);
        this.queuedPlayerUUIDs.clear();

        this.getCooldownHandler().addCooldown("on-death-freeze", 0.5F);
        this.getCooldownHandler().addCooldown("on-death-immunity", 2F);
        this.getCooldownHandler().addCooldown("double-jump", 100000);
        this.getCooldownHandler().addCooldown("regen-health", 6);
        this.getCooldownHandler().addCooldown("regen-air", 3);
        for (UUID uuid : this.getInGamePlayerUUIDs()) {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player == null) continue;
            String className = this.getClassSelections().get(player.getUniqueId());

            // Set hasUltimate to true
            this.setHasUltimate(uuid, true);
            this.setIsUltimateActive(uuid, false);
            for (PotionEffect effect : player.getActivePotionEffects()) { player.removePotionEffect(effect.getType()); }

            // Add all players to double-jump cooldown
            this.getCooldownHandler().getCooldownByName("on-death-freeze").addPlayer(player);
            this.getCooldownHandler().getCooldownByName("on-death-immunity").addPlayer(player);
            this.getCooldownHandler().getCooldownByName("double-jump").addPlayer(player);
            this.getCooldownHandler().getCooldownByName("regen-health").addPlayer(player);
            this.getCooldownHandler().getCooldownByName("regen-air").addPlayer(player);

            // Set scoreboards
            this.getScoreboard().addTeam(player.getName());
            this.getScoreboard().addPlayer(player.getName(), player);

            // Set gamerules
            player.getWorld().setGameRule(DO_DAYLIGHT_CYCLE, false);
            player.getWorld().setGameRule(NATURAL_REGENERATION, false);
            player.getWorld().setGameRule(ANNOUNCE_ADVANCEMENTS, false);

            // Set air
            player.setRemainingAir(-30);
            player.lockFreezeTicks(false);

            // Set gamemode to survival
            player.setGameMode(GameMode.SURVIVAL);

            // Add boss bar
            this.bossBarHandler.clearBossBar(player);
            this.bossBarHandler.addBossBar(player);
            if (this.labyrinthClassHandler.getClassByName(className).isBoss()) {
                this.bossBarHandler.setBossBarVisible(player, this.getInGamePlayers());
            }

            // Set base lives, if not 5
            int maxLives = this.labyrinthClassHandler.getClassByName(className).getMaxLives();
            this.scoreboard.setLives(player, maxLives);
        }

        // Set labyrinth state
        this.labyrinth.removeEntities();
        this.removeSummonsFromLabyrinth();

        // Start regen task
        this.startNaturalRegenTask(this);

        // Change game state
        this.gameState = GameState.PLAY;
    }
    public void leaveGame(Player player) {
        // Revert labyrinth to state before game
        this.labyrinth.removeEntities();
        this.killPlayerSummons(player);
        player.getWorld().setStorm(false);
        player.getWorld().setThundering(false);

        // Remove player from lists
        if (isPlayerInQueue(player)) this.queuedPlayerUUIDs.remove(player.getUniqueId());
        if (isPlayerInPlay(player)) this.inGamePlayerUUIDs.remove(player.getUniqueId());
        if (isPlayerSpectating(player)) this.spectatingPlayerUUIDs.remove(player.getUniqueId());

        // Reset their scoreboard
        this.scoreboard.removePlayer(player);
        this.bossBarHandler.clearBossBar(player);

        // Reset their gamemode
        player.setGameMode(GameMode.SURVIVAL);

        // End player ultimate, if it is active
        this.setIsUltimateActive(player.getUniqueId(), false);

        // Revert player to state before game
        player.teleport(player.getWorld().getSpawnLocation());
        player.getInventory().clear();
        for (PotionEffect effect : player.getActivePotionEffects()) { player.removePotionEffect(effect.getType()); }
        player.setFireTicks(0);
        player.setFreezeTicks(0);
        player.setRemainingAir(300);
        player.setFlySpeed(1);
        player.setFlying(false);
        player.setTotalExperience(0);
        player.resetMaxHealth();

        // Set game state, if necessary
        if (this.getPlayerCount() == 0) { this.endGameTask(this); }

        player.sendMessage(ChatColor.GRAY + "Left the game!");
    }
    public void joinSpectators(Player player) {
        // Transfer player from game -> spectator
        if (isPlayerInGame(player)) this.inGamePlayerUUIDs.remove(player.getUniqueId());
        this.spectatingPlayerUUIDs.add(player.getUniqueId());
        player.setGameMode(GameMode.SPECTATOR);
    }
    public void endGame() {
        if (this.inGamePlayerUUIDs.size() != 0) {
            Player winner = this.plugin.getServer().getPlayer(this.inGamePlayerUUIDs.get(0));
            for (UUID uuid : this.getPlayerUUIDs()) {
                Player p = this.plugin.getServer().getPlayer(uuid);
                if (p != null) {
                    if (winner != null) {
                        p.sendMessage(ChatColor.GOLD + winner.getName() + " has won the game!");
                    }
                    this.leaveGame(p);
                }
            }
        }

        // Change game state
        this.gameState = GameState.DONE;

        // Unregister listeners
        this.unRegisterInGameListeners();
    }
    public void endGameTask(Game game) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (game == null) return;
                game.endGame();
            }
        }.runTaskLater(this.plugin, 20L*1L);
    }
    public void teleportAndGiveKitToPlayers(Game game) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : game.getPlayers()) {
                    game.teleportPlayerToSpawn(p);
                    game.getLabyrinthClassHandler().giveKit(p);
                    // Update item events on start
                    p.setMaxHealth(20);
                    game.getLabyrinthItemHandler().onSpawn(p);
                    addHealth(p, (int) p.getMaxHealth());
                    p.setRemainingAir(-30);
                    p.sendMessage(ChatColor.GREEN + "Game started!");
                }
                game.unRegisterInQueueListeners();
                game.registerInGameListeners();
            }
        }.runTaskLater(this.plugin, 20L*3L);
    }

    // -------------------------------------------------------------------------------------------
    // Utilities
    // -------------------------------------------------------------------------------------------
    public void removeSummonsFromLabyrinth() { for (Player p : this.getPlayers()) { this.killPlayerSummons(p); } }
    public void teleportPlayerToSpawn(Player player) {
        int index = this.inGamePlayerUUIDs.indexOf(player.getUniqueId());
        player.teleport(this.getSpawn(index));
    }
    public void teleportPlayerToRandSpawn(Player player) {
        player.teleport(this.getRandSpawn());
    }
    public void teleportPlayerToQueue(Player player) {
        player.teleport(this.getRandQueue());
    }
    public void recordLifeLost(Player player) {
        // Record lost life in scoreboard
        this.scoreboard.recordLostLife(player);
        int livesLeft = this.scoreboard.getLives(player);

        // Send player a life update
        String lifeStr = (livesLeft == 1) ? " life " : " lives ";

        // Send everybody a death message
        String lifeMsg;
        if (livesLeft >= 1) {
            lifeMsg = ChatColor.GOLD + player.getName() + ChatColor.GRAY + " has " + livesLeft + lifeStr + "remaining.";
        }
        else {
            lifeMsg = ChatColor.GOLD + player.getName() + ChatColor.GRAY + " has lost!";
        }
        for (UUID uuid : this.getPlayerUUIDs()) {
            Player p = this.plugin.getServer().getPlayer(uuid);
            if (p == null) continue;
            p.sendMessage(lifeMsg);
        }

        // If player is out of lives, put in spectator mode
        if (livesLeft <= 0) { this.joinSpectators(player); }

        // If everybody is dead, end the game
        if (this.isEverybodyDeadExceptOne()) { this.endGameTask(this); }
    }
    public void addPlayerSummons(Player player, List<Entity> entities) {
        if (!this.summonedEntities.containsKey(player.getUniqueId())) {
            this.summonedEntities.put(player.getUniqueId(), new ArrayList<>());
        }
        for (Entity e : entities) {
            e.setPersistent(false);
            this.summonedEntities.get(player.getUniqueId()).add(e);
        }
    }
    public void addPlayerSummons(Player player, Block block, Material originalMaterial) {
        Map<Block, Material> blockMaterialMap = this.summonedBlocks.getOrDefault(player.getUniqueId(), null);
        if (blockMaterialMap == null) {
            this.summonedBlocks.put(player.getUniqueId(), Map.of(block, originalMaterial));
        }
        else {
            Map<Block, Material> newBlockMaterialMap = new HashMap<>(blockMaterialMap);
            newBlockMaterialMap.put(block, originalMaterial);
            this.summonedBlocks.put(player.getUniqueId(), newBlockMaterialMap);
        }
    }
    public void removePlayerSummon(Block block) {
        for (UUID uuid : this.summonedBlocks.keySet()) {
            Map<Block, Material> blockMap = this.summonedBlocks.get(uuid);
            for (Block summon : new ArrayList<>(this.summonedBlocks.get(uuid).keySet())) {
                if (summon.getLocation().equals(block.getLocation())) {
                    // Get previous material
                    Material prevMaterial = blockMap.get(block);

                    // Remove from map
                    if (this.summonedBlocks.get(uuid).size() == 1) {
                        this.summonedBlocks.clear();
                    }
                    else {
                        this.summonedBlocks.get(uuid).remove(summon);
                    }

                    // Set material
                    setMaterial(summon, prevMaterial);
                }
            }
        }
    }
    // For some reason, need to delay this by 1 tick
    public void setMaterial(Block block, Material material) {
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(material);
            }
        }.runTaskLater(this.plugin, 1L);
    }
    public LivingEntity getNearestEntityToSummon(Player summoner, Entity entity) {
        LivingEntity nearestEntity = null;
        float shortestDistance = 100000;
        for (LivingEntity e : entity.getLocation().getNearbyLivingEntities(20, 20, 20)) {
            float distance = (float) (e.getLocation().toVector().subtract(entity.getLocation().toVector())).length();
            if (distance >= shortestDistance) continue;
            if (summoner.equals(e)) continue;
            if (this.summonedEntities.get(summoner.getUniqueId()).contains(e)) continue;
            if (e instanceof Player player && !player.getGameMode().equals(GameMode.SURVIVAL)) continue;

            shortestDistance = distance;
            nearestEntity = e;
        }
        return nearestEntity;
    }
    public void killPlayerSummons(Player player) {
        if (this.summonedEntities.containsKey(player.getUniqueId())) {
            for (Entity e : this.summonedEntities.get(player.getUniqueId())) { e.remove(); }
            this.summonedEntities.put(player.getUniqueId(), new ArrayList<>());
        }

        Map<Block, Material> blockMaterialMap = this.summonedBlocks.getOrDefault(player.getUniqueId(), null);
        if (blockMaterialMap != null) {
            for (Block b : blockMaterialMap.keySet()) {
                b.setType(blockMaterialMap.get(b));
            }
        }
    }

    public void startNaturalRegenTask(Game game) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // End condition
                if (game.isGameDone()) {
                    this.cancel();
                    return;
                }

                for (Player player : game.getInGamePlayers()) {
                    addHealth(player, 1);
                }

            }
        }.runTaskTimer(this.plugin, 20L * 0L, (long) (20 * 7F));
    }
    public void countdown(Game game, int secs) {
        new BukkitRunnable() {
            int secsLeft = secs;
            @Override
            public void run() {
                for (Player p : game.getPlayers()) {
                    p.sendMessage(ChatColor.GRAY + "Teleporting to the labyrinth in " + ChatColor.GOLD + secsLeft);
                    playSound(p, Sound.BLOCK_NOTE_BLOCK_HAT);
                }
                secsLeft -= 1;
                if (secsLeft <= 0) { this.cancel(); }
            }
        }.runTaskTimer(this.plugin, 20L*0L, 20L*1L);
    }

    public void preventDamageInQueue(EntityDamageEvent event) {
        if (!isAPlayer(event.getEntity())) return;
        event.setCancelled(true);
    }

    // -------------------------------------------------------------------------------------------
    // Listeners
    // -------------------------------------------------------------------------------------------
    public void registerInQueueListeners() {
        this.inQueueListeners.add(new QueueEntityDamageListener(this));
        PluginManager manager = this.plugin.getServer().getPluginManager();
        for (Listener l : this.inQueueListeners) { manager.registerEvents(l, this.plugin); }
    }
    public void unRegisterInQueueListeners() {
        for (Listener l : this.inQueueListeners) { HandlerList.unregisterAll(l); }
    }
    public void registerInGameListeners() {
        // Register listener -> server
        this.inGameListeners.add(new BlockBreakListener(this.labyrinthEventHandler));
        this.inGameListeners.add(new BlockExplodeListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new BlockIgniteListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new BlockPlaceListener(this.plugin, this.labyrinthEventHandler, this.labyrinthClassHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new BlockRedstoneListener(this));
        this.inGameListeners.add(new EntityAirChangeListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new EntityBreedListener(this.plugin, this.labyrinthItemHandler));
        this.inGameListeners.add(new EntityChangeBlockListener(this.plugin, this.labyrinthEventHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new EntityCombustListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new EntityDamageByEntityListener(this.plugin, this.labyrinthEventHandler, this.labyrinthClassHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new EntityDamageListener(this.plugin, this, this.labyrinthEventHandler, this.labyrinthClassHandler));
        this.inGameListeners.add(new EntityDeathListener(this.plugin, this.labyrinthEventHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new EntityDropItemListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new EntityExplodeListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new EntityInteractListener(this));
        this.inGameListeners.add(new EntityMoveListener(this.plugin, this.labyrinthEventHandler, this.cooldownHandler));
        this.inGameListeners.add(new EntityPickupItemListener(this.plugin, this.labyrinthEventHandler, this.labyrinthClassHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new EntityTargetListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new EntityToggleGlideListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new EntityPotionEffectListener(this.plugin, this.labyrinthEventHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new EntityRegainHealthListener(this.plugin, this.labyrinthEventHandler, this.labyrinthClassHandler));
        this.inGameListeners.add(new EntityShootBowListener(this.labyrinthEventHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new InventoryClickListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new InventoryDragListener(this.plugin, this.labyrinthEventHandler, this.labyrinthClassHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new InventoryOpenListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new ItemSpawnListener(this.plugin, this.labyrinthEventHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new PlayerInteractListener(this.plugin, this.labyrinthEventHandler, this.labyrinthClassHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new PlayerItemConsumeListener(this.plugin, this.labyrinthEventHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new PlayerItemHeldListener(this.plugin, this.labyrinthItemHandler));
        this.inGameListeners.add(new PlayerInteractEntityListener(this.plugin, this.labyrinthEventHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new PlayerDeathListener(this.plugin, this, this.labyrinthEventHandler, this.labyrinthClassHandler));
        this.inGameListeners.add(new PlayerDropItemListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new PlayerLeashEntityListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new PlayerItemDamageListener(this.plugin, this, this.labyrinthEventHandler));
        this.inGameListeners.add(new PlayerMoveListener(this.plugin, this.labyrinthEventHandler, this.labyrinthClassHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new PlayerTeleportListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new PlayerToggleFlightListener(this.plugin, this, this.labyrinthClassHandler));
        this.inGameListeners.add(new ProjectileHitListener(this.labyrinthEventHandler, this.labyrinthItemHandler));
        this.inGameListeners.add(new SlimeSplitListener(this.labyrinthItemHandler));
        this.inGameListeners.add(new ThunderChangeListener(this.plugin, this.labyrinthEventHandler));
        this.inGameListeners.add(new WeatherChangeListener(this.plugin, this.labyrinthEventHandler));

        PluginManager manager = this.plugin.getServer().getPluginManager();
        for (Listener l : this.inGameListeners) { manager.registerEvents(l, this.plugin); }
    }
    private void unRegisterInGameListeners() {
        for (Listener l : this.inGameListeners) { HandlerList.unregisterAll(l); }
    }
}