package chalkinshmeal.labyrinth.artifacts.items;

import io.papermc.paper.event.entity.EntityMoveEvent;
import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.LabyrinthEffect;
import chalkinshmeal.labyrinth.utils.LabyrinthPotion;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

/**
 * LabyrinthItem
 * In order to add an item into the game, you must:
 * 1. Add a specialID in the classes.yml
 * 2. Create a LabyrinthItem class
 * 3. Add it to LabyrinthItemHandler with its specialID
 * 4. If necessary, connect any new listeners via LabyrinthItemHandler+LabyrinthItem
 */
public class LabyrinthItem {
    protected final JavaPlugin plugin;
    private final ConfigHandler config;
    protected final Game game;
    private final String className;
    private final String position;
    private int specialID;
    protected Material material;
    private int amount;
    private Map<String, Integer> enchants;
    private List<LabyrinthEffect> potionEffects;
    private String colorStr;
    private String displayName;
    private String lore;
    private TrimPattern trimPattern;
    private TrimMaterial trimMaterial;
    private int cooldownInTicks;
    protected int durationInTicks;
    private ItemStack item;
    private boolean hideEnchants;
    private boolean isUltimate;

    public LabyrinthItem(JavaPlugin plugin, ConfigHandler config, String className, String invPosStr, Game game) {
        this.plugin = plugin;
        this.config = config;
        this.game = game;
        this.className = className;
        this.position = invPosStr;
        this.enchants = new HashMap<>();
        this.potionEffects = new ArrayList<>();
        this.item = this.getItemFromConfig("classes." + className + ".items." + this.position);
        this.isUltimate = (this.position.equals("ultimate"));
    }
    public LabyrinthItem(Map<String, Object> args) {
        this.plugin = (JavaPlugin) args.getOrDefault("plugin", null);
        this.config = null;
        this.game = null;
        this.className = null;
        this.position = null;
        this.specialID = -1;
        this.material = (Material) args.getOrDefault("material", AIR);
        this.amount = (int) args.getOrDefault("amount", 1);
        this.enchants = (Map<String, Integer>) args.getOrDefault("enchants", new HashMap<>());
        this.potionEffects = (List<LabyrinthEffect>) args.getOrDefault("potionEffects", new ArrayList<>());
        this.colorStr = (String) args.getOrDefault("colorStr", "");
        this.displayName = (String) args.getOrDefault("displayName", "");
        this.lore = (String) args.getOrDefault("lore", "");
        this.trimPattern = null;
        this.trimMaterial = null;
        this.cooldownInTicks = (int) args.getOrDefault("cooldownInTicks", 0);
        this.durationInTicks = (int) args.getOrDefault("durationInTicks", 0);
        this.isUltimate = false;
        this.hideEnchants = (boolean) args.getOrDefault("hide-enchants", false);
        this.item = this.createItem();
    }
    // -------------------------------------------------------------------------------------------
    // Manipulators
    // -------------------------------------------------------------------------------------------
    public JavaPlugin getPlugin() { return this.plugin; }
    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(this.item);
        item.setAmount(this.amount);
        return item;
    }
    public String getDisplayName() { return this.displayName; }
    public String getPosition() { return this.position; }
    public String getClassName() { return this.className; }
    public float getCooldownInSeconds() { return ((float) this.cooldownInTicks) / 20; }
    public float getCooldownInTicks() { return this.cooldownInTicks; }
    public float getDurationInSeconds() { return ((float) this.durationInTicks) / 20; }
    public void setMaterial(Material m) { this.material = m; }
    public boolean isUltimate() { return this.isUltimate; }

    // -------------------------------------------------------------------------------------------
    // Listeners that can be overridden by individual items
    // -------------------------------------------------------------------------------------------
    public void onSpawn(Player player) {}
    public void onBlockBreakEvent(BlockBreakEvent event) {}
    public void onBlockExplodeEvent(BlockExplodeEvent event) {}
    public void onBlockPlaceEvent(BlockPlaceEvent event) {}
    public void onBlockIgniteEvent(BlockIgniteEvent event) {}
    public void onBlockRedstoneEvent(BlockRedstoneEvent event) {}
    public void onChunkLoadEvent(ChunkLoadEvent event) {}
    public void onEntityAirChangeEvent(EntityAirChangeEvent event) {}
    public void onEntityBreedEvent(EntityBreedEvent event) {}
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {}
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {}
    public void onEntityDamageEvent(EntityDamageEvent event) {}
    public void onEntityDeathEvent(EntityDeathEvent event) {}
    public void onEntityDropItemEvent(EntityDropItemEvent event) {}
    public void onEntityExplodeEvent(EntityExplodeEvent event) {}
    public void onEntityInteractEvent(EntityInteractEvent event) {}
    public void onEntityMoveEvent(EntityMoveEvent event) {}
    public void onEntityPickupItemEvent(EntityPickupItemEvent event) {}
    public void onEntityPotionEffectEvent(EntityPotionEffectEvent event) {}
    public void onEntityRegainHealthEvent(EntityRegainHealthEvent event) {}
    public void onEntityShootBowEvent(EntityShootBowEvent event) {}
    public void onEntitySpawnEvent(EntitySpawnEvent event) {}
    public void onEntityTargetEvent(EntityTargetEvent event) {}
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent event) {}
    public void onInventoryClickEvent(InventoryClickEvent event) {}
    public void onInventoryDragEvent(InventoryDragEvent event) {}
    public void onInventoryOpenEvent(InventoryOpenEvent event) {}
    public void onItemSpawnEvent(ItemSpawnEvent event) throws ReflectiveOperationException {}
    public void onLightningStrikeEvent(LightningStrikeEvent event) {}
    public void onPlayerDeathEvent(PlayerDeathEvent event) {}
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {}
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {}
    public void onPlayerInteractEvent(PlayerInteractEvent event) {}
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {}
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) {}
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) {}
    public void onPlayerLeashEntityEvent(PlayerLeashEntityEvent event) {}
    public void onPlayerMoveEvent(PlayerMoveEvent event) {}
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) {}
    public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event) {}
    public void onProjectileHitEvent(ProjectileHitEvent event) {}
    public void onSlimeSplitEvent(SlimeSplitEvent event) {}

    // -------------------------------------------------------------------------------------------
    // Utils
    // -------------------------------------------------------------------------------------------
    // Inventory
    public ItemStack getItemInInventory(Entity e) {
        if (!(e instanceof Player)) return null;
        Player player = (Player) e;
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item != null && !item.getType().equals(AIR)) {
                boolean matchesMaterial = item.getType().equals(this.material);
                String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
                String expectedName = ChatColor.stripColor(this.displayName);
                boolean matchesDisplayName = name.equals(expectedName);
                if (matchesMaterial & matchesDisplayName) return item;
            }
        }
        return null;
    }
    public int getItemCount(Entity e) {
        if (!(e instanceof Player)) return -1;
        Player player = (Player) e;
        ItemStack item = getItemInInventory(player);
        if (item == null) return 0;
        return item.getAmount();
    }
    public boolean isItemThisItem(ItemStack item) {
        boolean matchesMaterial = item.getType().equals(this.material);
        boolean matchesDisplayName = item.getItemMeta().getDisplayName().equals(this.displayName);
        return (matchesMaterial & matchesDisplayName);
    }
    public boolean isPlayerHoldingItemInMainHand(Entity e) {
        if (!(e instanceof Player)) return false;
        Player player = (Player) e; ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().equals(AIR)) return false;
        boolean matchesMaterial = item.getType().equals(this.material);
        String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        String expectedName = ChatColor.stripColor(this.displayName);
        boolean matchesDisplayName = name.equals(expectedName);
        return (matchesMaterial & matchesDisplayName);
    }
    public boolean isPlayerHoldingItemInMainHand(Entity e, Material m) {
        if (!(e instanceof Player)) return false;
        Player player = (Player) e; ItemStack item = player.getInventory().getItemInMainHand();
        return item.getType().equals(m);
    }
    public boolean isPlayerHoldingItemInInventory(Entity e) {
        if (!(e instanceof Player)) return false;
        Player player = (Player) e;
        ItemStack item = this.getItemInInventory(player);
        return (item != null);
    }
    public boolean isPlayerHoldingItemInSlot(Entity e, int newSlot) {
        if (!(e instanceof Player)) return false;
        Player player = (Player) e; ItemStack item = player.getInventory().getItem(newSlot);
        if (item == null || item.getType().equals(AIR)) return false;
        boolean matchesMaterial = item.getType().equals(this.material);
        String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        String expectedName = ChatColor.stripColor(this.displayName);
        boolean matchesDisplayName = name.equals(expectedName);
        return (matchesMaterial & matchesDisplayName);
    }

    public boolean isRightClick(PlayerInteractEvent event) { return event.getAction().equals(RIGHT_CLICK_BLOCK) || event.getAction().equals(RIGHT_CLICK_AIR); }
    public void changeItemCount(Entity e, int change) {
        if (!(e instanceof Player)) return;
        Player player = (Player) e;
        ItemStack item = this.getItemInInventory(player);
        if (item == null) return;
        item.add(change);
    }
    public void setItemCount(Entity e, int amount) {
        if (!(e instanceof Player)) return;
        Player player = (Player) e;
        ItemStack item = this.getItemInInventory(player);
        if (item == null) return;
        item.setAmount(amount);
    }
    public void setItemType(Entity e, Material material) {
        if (!(e instanceof Player)) return;
        Player player = (Player) e;
        ItemStack item = this.getItemInInventory(player);
        if (item == null) return;
        item.setAmount(amount);
        item.setType(material);
    }

    // Cooldowns
    public boolean isItemCooldownReady(Player player) { return (player.getCooldown(this.material) <= 0); }
    public boolean isItemCooldownBetween(Player player, float lo, float hi) {
        return ((player.getCooldown(this.material) <= hi*20) && (player.getCooldown(this.material) >= lo*20));
    }
    public void resetItemCooldown(Player player) { player.setCooldown(this.material, this.cooldownInTicks); }
    public void setItemCooldown(Player player, int ticks) { player.setCooldown(this.material, ticks); };

    // Ultimate
    public void setUsedUltimate(Player p) { this.game.setUsedUltimate(p); }
    public boolean isUltimateActive(Player p) {
        if (!this.game.isPlayerInPlay(p)) return false;
        return this.game.isUltimateActive(p);
    }
    public boolean doesPlayerHaveUltimate(Player p) { return this.game.doesPlayerHaveUltimate(p); }

    // Plugin
    public void broadcastGameMsg(String msg) { for (Player p : this.game.getPlayers()) p.sendMessage(msg); }

    // -------------------------------------------------------------------------------------------
    // Config
    // -------------------------------------------------------------------------------------------
    public ItemStack getItemFromConfig(String pathToItem) {
        // Check for the existence of this item ID
        if (!this.config.doesKeyExist(pathToItem)) { this.plugin.getLogger().warning("LabyrinthItem: Could not return find item ID " + this.specialID); }

        this.specialID = config.getInt(pathToItem + ".specialID", -1);
        this.material = config.getMaterialFromKey(pathToItem + ".material");
        this.amount = config.getInt(pathToItem + ".amount", 1);
        this.displayName = config.getString(pathToItem + ".display-name", "");
        this.lore = config.getString(pathToItem + ".lore", "");
        this.cooldownInTicks = (int) (config.getFloat(pathToItem + ".cooldown", 0) * 20);
        this.durationInTicks = (int) (config.getFloat(pathToItem + ".duration", 0) * 20);
        this.colorStr = config.getString(pathToItem + ".color", "BLACK");
        this.enchants = new HashMap<>();
        if (config.doesKeyExist(pathToItem + ".enchants")) {
            for (String enchantName : config.getKeyListFromKey(pathToItem + ".enchants")) {
                String pathToEnchantLevel = pathToItem + ".enchants." + enchantName;
                this.enchants.put(enchantName, config.getInt(pathToEnchantLevel, 1));
            }
        }
        this.potionEffects = new ArrayList<>();
        if (config.doesKeyExist(pathToItem + ".potion-type"))
            this.potionEffects = LabyrinthPotion.getEffectsFromConfig(config, pathToItem + ".potion-type");

        if (config.doesKeyExist(pathToItem + ".trim-pattern"))
            this.trimPattern = getTrimPatternFromStr(config.getString(pathToItem + ".trim-pattern", ""));

        if (config.doesKeyExist(pathToItem + ".trim-material"))
            this.trimMaterial = getTrimMaterialFromStr(config.getString(pathToItem + ".trim-material", ""));

        return this.createItem();
    }

    // -------------------------------------------------------------------------------------------
    // Internal Helper Commands
    // -------------------------------------------------------------------------------------------
    private ItemStack createItem() {
        ItemStack item = new ItemStack(this.material, this.amount);

        // Add enchants
        ItemMeta im = item.getItemMeta();
        if (!displayName.isBlank()) { im.setDisplayName(displayName); }
        if (!lore.isBlank()) { im.setLore(List.of(this.lore.split("\n"))); }
        if (!enchants.isEmpty()) {
            for (Map.Entry<String, Integer> m : enchants.entrySet()) {
                String enchantName = m.getKey();
                Integer enchantLevel = m.getValue();
                Enchantment enchantment = Enchantment.getByName(enchantName);
                if (enchantment == null) {
                    this.plugin.getServer().getLogger().warning("Could not find enchantment " + enchantName);
                }
                im.addEnchant(enchantment,  enchantLevel, true);
            }
        }
        item.setItemMeta(im);

        // Add color to item, if possible
        if (!colorStr.isBlank()) {
            Color color = null;
            if (colorStr.contains("#")) { color = Color.fromRGB(Integer.parseInt(colorStr.replaceFirst("#", ""), 16)); }
            else { color = getColorFromStr(colorStr); }

            if (im instanceof LeatherArmorMeta) {
                LeatherArmorMeta lam = (LeatherArmorMeta) item.getItemMeta();
                lam.setColor(color);
                item.setItemMeta(lam);
            }
            if (im instanceof PotionMeta) {
                PotionMeta pm = (PotionMeta) item.getItemMeta();
                pm.setColor(color);
                item.setItemMeta(pm);
            }
        }

        // Add potion effects
        if (!potionEffects.isEmpty()) {
            PotionMeta pm = (PotionMeta) item.getItemMeta();
            for (LabyrinthEffect effect : potionEffects) { pm.addCustomEffect(effect.getPotionEffect(), false); }
            item.setItemMeta(pm);
        }

        // Add armor trim, if applicable
        addTrim(item, this.trimPattern, this.trimMaterial);

        // Hide enchants, if applicable
        if (this.hideEnchants) item = hideEnchants(item);

        return item;
    }
}

