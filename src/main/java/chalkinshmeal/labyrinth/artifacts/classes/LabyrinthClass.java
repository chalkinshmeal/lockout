package chalkinshmeal.labyrinth.artifacts.classes;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItemHandler;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.LabyrinthEffect;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.List;

import static chalkinshmeal.labyrinth.utils.SpawnUtils.spawnFirework;
import static chalkinshmeal.labyrinth.utils.Utils.*;

public class LabyrinthClass {
    protected final JavaPlugin plugin;
    protected final ConfigHandler config;
    protected final String className;
    protected final Game game;
    protected final LabyrinthItemHandler labyrinthItemHandler;
    protected boolean isItemModeEnabled;

    // State
    protected ItemStack helmet; // Necessary because the dumb API doesn't allow you to create a PlayerInventory
    protected ItemStack chestplate;
    protected ItemStack leggings;
    protected ItemStack boots;
    protected ItemStack shield;
    protected ItemStack ultimate;
    protected Inventory defaultInventory;
    protected List<LabyrinthEffect> effects;
    protected int maxLives;
    protected int maxHealth;
    protected boolean isBoss;

    LabyrinthClass(JavaPlugin plugin, String className, Game game, LabyrinthItemHandler labyrinthItemHandler) {
        this.plugin = plugin;
        this.config = new ConfigHandler(plugin, "plugins/labyrinth/classes.yml");
        this.className = className;
        this.game = game;
        this.labyrinthItemHandler = labyrinthItemHandler;
        getClassFromConfig();
    }

    // -------------------------------------------------------------------------------------------
    // Commands
    // -------------------------------------------------------------------------------------------
    public void giveKit(Player player, boolean isItemModeEnabled) {
        this.isItemModeEnabled = isItemModeEnabled;

        boolean giveUltimate = (this.game == null) ? true : this.game.doesPlayerHaveUltimate(player);
        player.getInventory().setContents(this.defaultInventory.getContents());
        if (giveUltimate) player.getInventory().setItem(8, this.ultimate);
        player.getInventory().setHelmet(this.helmet);
        player.getInventory().setChestplate(this.chestplate);
        player.getInventory().setLeggings(this.leggings);
        player.getInventory().setBoots(this.boots);
        player.getInventory().setItemInOffHand(this.shield);
        for (LabyrinthEffect effect : this.effects) { player.addPotionEffect(effect.getPotionEffect()); }

        if (this.isItemModeEnabled) { player.getInventory().addItem(this.labyrinthItemHandler.getItem(89).getItemStack()); }
    }

    // -------------------------------------------------------------------------------------------
    // Manipulators
    // -------------------------------------------------------------------------------------------
    public int getMaxLives() { return this.maxLives; }
    public int getMaxHealth() { return this.maxHealth; }
    public boolean isBoss() { return this.isBoss; }

    // -------------------------------------------------------------------------------------------
    // Listeners
    // -------------------------------------------------------------------------------------------
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        this.preventDeathFireworkDamage(event);
    }
    public void onEntityDamageEvent(EntityDamageEvent event) {
        this.updateBossBar(event);
    }
    public void onEntityRegainHealthEvent(EntityRegainHealthEvent event) { this.updateBossBar(event); }
    public void onPlayerDeathEvent(PlayerDeathEvent event) { this.handleDeath(event); }
    public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event) {
        this.doubleJump(event);
    }

    // -------------------------------------------------------------------------------------------
    // Utils
    // -------------------------------------------------------------------------------------------
    public void doubleJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) return;
        if (!this.game.isPlayerInPlay(player)) return;
        event.setCancelled(true);

        player.setAllowFlight(false);
        player.setFlying(false);
        if (this.game.getCooldownHandler().isCooldownReady("double-jump", player)) {
            float power = (float) 1.5;
            player.setVelocity(player.getLocation().getDirection().multiply(power));
            player.playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 15);
            this.game.getCooldownHandler().resetCooldown("double-jump", player);
        }
    }
    public void handleDeath(PlayerDeathEvent event) {
        event.setCancelled(true);

        // Retrieve info about event
        Player player = event.getPlayer();
        if (!this.game.isPlayerInPlay(player)) return;

        // Fire firework
        spawnFirework(player.getLocation().add(0, 1, 0), Color.RED, 0);

        // Reset cooldown so that you don't leap to your death instantly
        this.game.getCooldownHandler().resetCooldown("on-death-freeze", event.getPlayer());
        this.game.getCooldownHandler().resetCooldown("on-death-immunity", event.getPlayer());
        this.game.getCooldownHandler().resetCooldown("double-jump", event.getPlayer());

        // Intercept death message
        for (Player p : this.game.getPlayers()) {
            String deathMsg = ChatColor.RED + event.getDeathMessage();
            p.sendMessage(deathMsg);
        }

        // Respawn player
        player.spigot().respawn();

        // Put player in spectator mode, if applicable
        // Update scoreboard of lives
        // End game, if applicable
        this.game.recordLifeLost(player);

        // Reset state
        player.setVelocity(new Vector(0, 0, 0));
        player.getActivePotionEffects().clear();
        player.setFireTicks(0);
        player.setFreezeTicks(0);
        player.lockFreezeTicks(false);
        player.setRemainingAir(-30);
        this.game.setUltimateInactive(player);
        resetCooldowns(player);
        this.game.killPlayerSummons(player);
        player.setMaxHealth(20);
        addHealth(player, (int) player.getMaxHealth());
        this.game.teleportPlayerToRandSpawn(player);

        // If we are going to spawn again:
        if (game.getScoreboard().getLives(player) != 0) {
            this.giveKit(player, this.isItemModeEnabled);
            this.game.getLabyrinthItemHandler().onSpawn(player);
        }
    }

    public void updateBossBar(EntityDamageEvent event) {
        if (!isAPlayer(event.getEntity())) return;
        if (!this.isBoss) return;
        Player player = (Player) event.getEntity();

        double livesLeft = this.game.getScoreboard().getLives(player);
        double livesMax = this.game.getLabyrinthClassHandler().getClassByPlayer(player).getMaxLives();
        double healthLeft = Math.max(player.getHealth() - event.getDamage(), 0);
        double healthMax = player.getMaxHealth();
        double lifeLeftOfAllLives = ((livesLeft-1)*healthMax) + healthLeft;
        double lifeTotalOfAllLives = healthMax*livesMax;
        double progressLeft = Math.min(1, lifeLeftOfAllLives / lifeTotalOfAllLives);
        this.game.getBossBarHandler().updateBossBar(player, progressLeft);
    }
    public void updateBossBar(EntityRegainHealthEvent event) {
        if (!isAPlayer(event.getEntity())) return;
        if (!this.isBoss) return;
        Player player = (Player) event.getEntity();

        double livesLeft = this.game.getScoreboard().getLives(player);
        double livesMax = this.game.getLabyrinthClassHandler().getClassByPlayer(player).getMaxLives();
        double healthLeft = Math.max(player.getHealth(), 0);
        double healthMax = player.getMaxHealth();
        double lifeLeftOfAllLives = ((livesLeft-1)*healthMax) + healthLeft;
        double lifeTotalOfAllLives = healthMax*livesMax;
        double progressLeft = Math.min(1, lifeLeftOfAllLives / lifeTotalOfAllLives);
        this.game.getBossBarHandler().updateBossBar(player, progressLeft);
    }

    public void preventDeathFireworkDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework && event.getDamager().getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            event.setCancelled(true);
        }
    }


    // -------------------------------------------------------------------------------------------
    // Config
    // -------------------------------------------------------------------------------------------
    public void getClassFromConfig() {
        String pathToClass = "classes." + this.className;
        if (!config.doesKeyExist(pathToClass)) { this.plugin.getLogger().warning("LabyrinthClass: Could not find class " + this.className); }

        // Get information
        this.maxLives = config.getInt(pathToClass + ".max-lives", 1);
        this.maxHealth = config.getInt(pathToClass + ".max-health", 20);
        this.isBoss = config.getInt(pathToClass + ".is-boss", 0) == 1;
        this.effects = LabyrinthEffect.getLabyrinthEffectsFromConfig(config, pathToClass + ".effects");

        // Get inventory
        this.defaultInventory = Bukkit.createInventory(null, InventoryType.PLAYER);
        if (config.doesKeyExist(pathToClass + ".items")) {
            for (String itemPosStr : config.getKeyListFromKey(pathToClass + ".items")) {
                String displayName = config.getString(pathToClass + ".items." + itemPosStr + ".display-name", "");
                switch (itemPosStr) {
                    case "helmet": this.helmet = this.labyrinthItemHandler.getItem(this.className, displayName, itemPosStr); break;
                    case "chestplate": this.chestplate = this.labyrinthItemHandler.getItem(this.className, displayName, itemPosStr); break;
                    case "leggings": this.leggings = this.labyrinthItemHandler.getItem(this.className, displayName, itemPosStr); break;
                    case "boots": this.boots = this.labyrinthItemHandler.getItem(this.className, displayName, itemPosStr); break;
                    case "shield": this.shield = this.labyrinthItemHandler.getItem(this.className, displayName, itemPosStr); break;
                    case "ultimate": this.ultimate = this.labyrinthItemHandler.getItem(this.className, displayName, itemPosStr); break;
                    default:
                        int itemPos = Integer.parseInt(itemPosStr);
                        this.defaultInventory.setItem(itemPos, this.labyrinthItemHandler.getItem(this.className, displayName, itemPosStr));
                        break;
                }
            }
        }
    }
}
