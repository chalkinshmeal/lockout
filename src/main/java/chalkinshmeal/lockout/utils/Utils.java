package chalkinshmeal.lockout.utils;

import static org.bukkit.Material.AIR;
import static org.bukkit.enchantments.Enchantment.BANE_OF_ARTHROPODS;
import static org.bukkit.enchantments.Enchantment.BLAST_PROTECTION;
import static org.bukkit.enchantments.Enchantment.FEATHER_FALLING;
import static org.bukkit.enchantments.Enchantment.FIRE_ASPECT;
import static org.bukkit.enchantments.Enchantment.FIRE_PROTECTION;
import static org.bukkit.enchantments.Enchantment.KNOCKBACK;
import static org.bukkit.enchantments.Enchantment.PROJECTILE_PROTECTION;
import static org.bukkit.enchantments.Enchantment.PROTECTION;
import static org.bukkit.enchantments.Enchantment.RESPIRATION;
import static org.bukkit.enchantments.Enchantment.SHARPNESS;
import static org.bukkit.enchantments.Enchantment.SMITE;
import static org.bukkit.enchantments.Enchantment.SOUL_SPEED;
import static org.bukkit.enchantments.Enchantment.SWEEPING_EDGE;
import static org.bukkit.enchantments.Enchantment.SWIFT_SNEAK;
import static org.bukkit.enchantments.Enchantment.THORNS;
import static org.bukkit.entity.EntityType.POTION;
import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;
import static org.bukkit.event.entity.EntityPotionEffectEvent.Cause.AREA_EFFECT_CLOUD;
import static org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.CUSTOM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Trident;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

import net.kyori.adventure.text.Component;

public class Utils {
    //---------------------------------------------------------------------------------------------
    // General
    //---------------------------------------------------------------------------------------------
    public static <T> List<T> getRandomItems(List<T> list, int n) {
        if (n == -1) {
            return list;
        }
        if (n > list.size()) {
            return list;
        }

        List<T> copy = new ArrayList<>(list);
        Collections.shuffle(copy, new Random(System.currentTimeMillis())); // Shuffle the list
        return copy.subList(0, n); // Get the first N items
    }
    public static int roundToNearestMultiple(int num, int multiple) { return ((int) num/multiple) * multiple; }
    public static int getRandNum(int lo, int hi) {
        Random random = new Random(System.currentTimeMillis());
        if (hi < lo) {
            int temp = lo; lo = hi; hi = temp;
        }
        return random.nextInt((hi-lo)+1) + lo;
    }
    // Returns the highest multiple (Ex: num=25, mult=9, returns 27)
    public static int getHighestMultiple(int num, int mult) {
        return ((num + mult - 1) / mult) * mult;
    }
    public static Color getColorFromStr(String colorStr) {
        if (colorStr.equals("AQUA")) { return Color.AQUA; }
        if (colorStr.equals("BLACK")) { return Color.BLACK; }
        if (colorStr.equals("BLUE")) { return Color.BLUE; }
        if (colorStr.equals("FUCHSIA")) { return Color.FUCHSIA; }
        if (colorStr.equals("GRAY")) { return Color.GRAY; }
        if (colorStr.equals("GREEN")) { return Color.GREEN; }
        if (colorStr.equals("LIME")) { return Color.LIME; }
        if (colorStr.equals("MAROON")) { return Color.MAROON; }
        if (colorStr.equals("NAVY")) { return Color.NAVY; }
        if (colorStr.equals("OLIVE")) { return Color.OLIVE; }
        if (colorStr.equals("ORANGE")) { return Color.ORANGE; }
        if (colorStr.equals("PURPLE")) { return Color.PURPLE; }
        if (colorStr.equals("RED")) { return Color.RED; }
        if (colorStr.equals("SILVER")) { return Color.SILVER; }
        if (colorStr.equals("TEAL")) { return Color.TEAL; }
        if (colorStr.equals("WHITE")) { return Color.WHITE; }
        if (colorStr.equals("YELLOW")) { return Color.YELLOW; }
        return null;
    }
    public static TrimPattern getTrimPatternFromStr(String trimPatternStr) {
        if (trimPatternStr.equals("COAST")) { return TrimPattern.COAST; }
        if (trimPatternStr.equals("DUNE")) { return TrimPattern.DUNE; }
        if (trimPatternStr.equals("EYE")) { return TrimPattern.EYE; }
        if (trimPatternStr.equals("HOST")) { return TrimPattern.HOST; }
        if (trimPatternStr.equals("RAISER")) { return TrimPattern.RAISER; }
        if (trimPatternStr.equals("RIB")) { return TrimPattern.RIB; }
        if (trimPatternStr.equals("SENTRY")) { return TrimPattern.SENTRY; }
        if (trimPatternStr.equals("SHAPER")) { return TrimPattern.SHAPER; }
        if (trimPatternStr.equals("SILENCE")) { return TrimPattern.SILENCE; }
        if (trimPatternStr.equals("SNOUT")) { return TrimPattern.SNOUT; }
        if (trimPatternStr.equals("SPIRE")) { return TrimPattern.SPIRE; }
        if (trimPatternStr.equals("TIDE")) { return TrimPattern.TIDE; }
        if (trimPatternStr.equals("VEX")) { return TrimPattern.VEX; }
        if (trimPatternStr.equals("WARD")) { return TrimPattern.WARD; }
        if (trimPatternStr.equals("WAYFINDER")) { return TrimPattern.WAYFINDER; }
        if (trimPatternStr.equals("WILD")) { return TrimPattern.WILD; }

        throw new RuntimeException("Utils::getTrimPatternFromStr: Invalid TrimPattern string passed: '" + trimPatternStr + "'");
    }
    public static TrimMaterial getTrimMaterialFromStr(String trimMaterialStr) {
        if (trimMaterialStr.equals("AMETHYST")) { return TrimMaterial.AMETHYST; }
        if (trimMaterialStr.equals("COPPER")) { return TrimMaterial.COPPER; }
        if (trimMaterialStr.equals("DIAMOND")) { return TrimMaterial.DIAMOND; }
        if (trimMaterialStr.equals("EMERALD")) { return TrimMaterial.EMERALD; }
        if (trimMaterialStr.equals("GOLD")) { return TrimMaterial.GOLD; }
        if (trimMaterialStr.equals("IRON")) { return TrimMaterial.IRON; }
        if (trimMaterialStr.equals("LAPIS")) { return TrimMaterial.LAPIS; }
        if (trimMaterialStr.equals("NETHERITE")) { return TrimMaterial.NETHERITE; }
        if (trimMaterialStr.equals("QUARTZ")) { return TrimMaterial.QUARTZ; }
        if (trimMaterialStr.equals("REDSTONE")) { return TrimMaterial.REDSTONE; }

        throw new RuntimeException("Utils::getTrimMaterialFromStr: Invalid TrimMaterial string passed: '" + trimMaterialStr + "'");
    }
    public static boolean isRightClick(Action action) { return action.equals(RIGHT_CLICK_BLOCK) || action.equals(RIGHT_CLICK_AIR); }
    public static boolean isMainHand(PlayerInteractEntityEvent event) { return event.getHand().equals(EquipmentSlot.HAND); }
    public static boolean isMainHand(PlayerInteractEvent event) { return event.getHand().equals(EquipmentSlot.HAND); }

    // World Information
    public static boolean isAirBetweenPoints(Location loc1, Location loc2) {
        int xmin = (int) Math.min(loc1.getX(), loc2.getX());
        int xmax = (int) Math.max(loc1.getX(), loc2.getX());
        int ymin = (int) Math.min(loc1.getY(), loc2.getY());
        int ymax = (int) Math.max(loc1.getY(), loc2.getY());
        int zmin = (int) Math.min(loc1.getZ(), loc2.getZ());
        int zmax = (int) Math.max(loc1.getZ(), loc2.getZ());
        Location loc3 = null;

        for (int x = xmin; x != xmax; x++) {
            for (int y = ymin; y != ymax; y++) {
                for (int z = zmin; z != zmax; z++) {
                    loc3 = new Location(loc1.getWorld(), x, y, z);
                    if (loc3.getBlock().getType() != AIR) { return false; }
                }
            }
        }
        return true;
    }
    public static List<Location> getWallsBetweenPoints(Location loc1, Location loc2) {
        int xmin = (int) Math.min(loc1.getX(), loc2.getX()); int xmax = (int) Math.max(loc1.getX(), loc2.getX());
        int ymin = (int) Math.min(loc1.getY(), loc2.getY()); int ymax = (int) Math.max(loc1.getY(), loc2.getY());
        int zmin = (int) Math.min(loc1.getZ(), loc2.getZ()); int zmax = (int) Math.max(loc1.getZ(), loc2.getZ());
        List<Location> locs = new ArrayList<>();

        for (int x = xmin; x != xmax+1; x++) {
            for (int y = ymin; y != ymax+1; y++) {
                for (int z = zmin; z != zmax+1; z++) {
                    if (x == xmin || x == xmax || y == ymin || y == ymax || z == zmin || z == zmax) {
                        locs.add(new Location(loc1.getWorld(), x, y, z));
                    }
                }
            }
        }
        return locs;
    }
    public static List<Block> getNonAirBlocksWithinRadius(Location loc, int radius) {
        List<Block> blocks = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    Block b = loc.getWorld().getBlockAt(new Location(loc.getWorld(), loc.getX()+dx, loc.getY()+dy, loc.getZ()+dz));
                    if (!b.getType().isAir())
                        blocks.add(b);
                }
            }
        }
        return blocks;
    }

    public static Location getNearestAirLocation(Block block, boolean just_up) { return getNearestAirLocation(block.getLocation(), just_up); }
    public static Location getNearestAirLocation(Location hitLocation, boolean just_up) {
        if (just_up) {
            return hitLocation.add(0, 1, 0);
        }
        // Spawn TNT in the nearest air location
        Block up = hitLocation.getWorld().getBlockAt(hitLocation.add(0, 1, 0));
        Block down = hitLocation.getWorld().getBlockAt(hitLocation.add(0, -1, 0));
        Block left = hitLocation.getWorld().getBlockAt(hitLocation.add(1, 0, 0));
        Block right = hitLocation.getWorld().getBlockAt(hitLocation.add(-1, 0, 0));
        Block front = hitLocation.getWorld().getBlockAt(hitLocation.add(0, 0, 1));
        Block back = hitLocation.getWorld().getBlockAt(hitLocation.add(0, 0, -1));
        if (up.getBlockData().getMaterial().equals(AIR)) { hitLocation.add(0, 1, 0); }
        else if (down.getBlockData().getMaterial().equals(AIR)) { hitLocation.add(0, -1, 0); }
        else if (left.getBlockData().getMaterial().equals(AIR)) { hitLocation.add(1, 0, 0); }
        else if (right.getBlockData().getMaterial().equals(AIR)) { hitLocation.add(-1, 0, 0); }
        else if (front.getBlockData().getMaterial().equals(AIR)) { hitLocation.add(0, 0, 1); }
        else if (back.getBlockData().getMaterial().equals(AIR)) { hitLocation.add(0, 0, -1); }
        else { ; }

        return hitLocation;
    }
    public static List<Projectile> getNearbyProjectiles(Entity entity, double r) {
        List<Entity> entities = entity.getNearbyEntities(r, r, r);
        List<Projectile> projectiles = new ArrayList<>();
        for (Entity e : entities) { if (e instanceof Projectile) projectiles.add((Projectile) e); }
        return projectiles;
    }

    // Players
    public static List<Player> getPlayersFromUUIDs(JavaPlugin plugin, List<UUID> uuids) {
        List<Player> players = new ArrayList<>();
        for (UUID uuid : uuids) { players.add(plugin.getServer().getPlayer(uuid)); }
        return players;
    }

    // Area Effect Clouds
    public static boolean isDamagerACloud(EntityDamageByEntityEvent event) {
        return event.getDamager().getType().equals(EntityType.AREA_EFFECT_CLOUD);
    }
    public static boolean isDamagerACloud(EntityPotionEffectEvent event) {
        return event.getCause().equals(AREA_EFFECT_CLOUD);
    }
    public static boolean doesCloudHaveEffects(Entity entity, List<PotionEffectType> potionEffectTypes) {
        if (!(entity instanceof AreaEffectCloud)) return false;
        AreaEffectCloud cloud = (AreaEffectCloud) entity;

        for (PotionEffectType type : potionEffectTypes) {
            if (!(cloud.hasCustomEffect(type))) return false;
        }
        return true;
    }

    // Health
    public static void addHealth(LivingEntity e, int health) {
        addHealth(e, health, CUSTOM);
    }
    @SuppressWarnings("deprecation")
    public static void addHealth(LivingEntity e, int health, EntityRegainHealthEvent.RegainReason reason) {
        //EntityRegainHealthEvent event = new EntityRegainHealthEvent(e, health, reason);
        //Bukkit.getPluginManager().callEvent(event);
        e.setHealth(Math.min(e.getHealth() + health, e.getMaxHealth()));
    }
    public static void subtractHealth(LivingEntity e, int health) { e.damage(health); }
    public static void subtractHealth(LivingEntity e, int health, boolean hurt) {
        if (hurt)
            e.damage(health);
        else
            e.setHealth(Math.max(e.getHealth() - health, 0));
    }
    public static void addAir(LivingEntity e, int air) {
        int remainingAir = Math.min(roundToNearestMultiple(e.getRemainingAir() + air, 30), e.getMaximumAir()-30);
        e.setRemainingAir(remainingAir);
    }
    public static void subtractAir(LivingEntity e, int air) {
        int remainingAir = Math.max(roundToNearestMultiple(e.getRemainingAir() - air , 30), -30);
        e.setRemainingAir(remainingAir);
    }
    public static void addBubbles(LivingEntity e, int bubbles) { addAir(e, bubbles*30); }
    public static void subtractBubbles(LivingEntity e, int bubbles) { subtractAir(e, bubbles*30); }
    public static int getBubbles(LivingEntity e) { return (int) e.getRemainingAir() / 30; }
    public static void addFreeze(LivingEntity e, float freeze_secs) { e.setFreezeTicks(Math.min(e.getFreezeTicks() + (int) freeze_secs*20, e.getMaxFreezeTicks())); }
    public static void subtractFreeze(LivingEntity e, float freeze_secs) { e.setFreezeTicks(Math.max(e.getFreezeTicks() - (int) freeze_secs*20, 0)); }

    // Player State
    public static boolean isPlayerHolding(Entity e, Material m) {
        if (!(e instanceof Player)) return false;
        return ((Player) e).getInventory().getItemInMainHand().getType().equals(m);
    }
    public static boolean isPlayerHolding(Entity e, Material m, Enchantment enchant) {
        if (!(e instanceof Player)) return false;
        return isPlayerHolding(e, m) && ((Player) e).getInventory().getItemInMainHand().getEnchantments().containsKey(enchant);
    }
    public static void useHeldItem(Player p) { p.getInventory().getItemInMainHand().subtract(1); p.updateInventory(); }
    public static void setItem(Player p, Material m, int slot) {
        p.getInventory().setItem(slot, new ItemStack(m));
        p.updateInventory();
    }
    public static ItemStack removeAndGetRandItem(Player p) {
        return removeAndGetRandItem((InventoryHolder) p);
    }
    public static ItemStack removeAndGetRandItem(LivingEntity p) {
        ItemStack item = p.getEquipment().getItemInMainHand();
        p.getEquipment().setItemInMainHand(new ItemStack(AIR));
        return item;
    }
    public static ItemStack removeAndGetRandItem(InventoryHolder p) {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack i : p.getInventory().getStorageContents()) {
            if (i != null)
                items.add(i);
        }
        if (items.size() == 0) return null;

        int randItemIndex = getRandNum(0, items.size()-1);
        ItemStack item = items.get(randItemIndex);
        p.getInventory().remove(item);
        return item;
    }
    @SuppressWarnings("deprecation")
    public static boolean isItemInInventory(Entity e, Material m, String name) {
        if (!(e instanceof Player)) return false;
        Player player = (Player) e;

        List<ItemStack> items = new ArrayList<>();
        for (ItemStack i : player.getInventory().getStorageContents()) {
            if (i != null)
                items.add(i);
        }
        for (ItemStack item : items) {
            if (item.getType().equals(m) && item.getItemMeta().getDisplayName().contains(name)) {
                return true;
            }
        }
        return false;
    }
    public static void resetCooldowns(Player p) {
        for (ItemStack item : p.getInventory().getStorageContents()) {
            if (item != null)
                p.setCooldown(item.getType(), 0);
        }
    }

    // Inventory
    public static void giveItem(LivingEntity e, ItemStack item) {
        InventoryHolder inventoryHolder = (InventoryHolder) e;
        inventoryHolder.getInventory().addItem(item);
    }
    public static ItemStack getItem(Entity e, Material m) {
        // NOTE: Returns first item in inventory that matches material m
        if (!(e instanceof Player)) return null;
        Player player = (Player) e;
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item != null && !item.getType().equals(AIR)) {
                boolean matchesMaterial = item.getType().equals(m);
                if (matchesMaterial) return item;
            }
        }
        return null;

    }

    // Sounds
    public static void playSound(Player p, Sound s) { p.getWorld().playSound(p, s, 1, 1); }
    public static void playSound(Player p, Sound s, float volume) { p.getWorld().playSound(p, s, volume, 1); }
    public static void playSound(Location l, Sound s) { l.getWorld().playSound(l, s, 1, 1); }
    public static void playSound(Location l, Sound s, float volume) { l.getWorld().playSound(l, s, volume, 1); }

    // Armor
    public static void setHelmet(LivingEntity e, Material m) { if (e.getEquipment().getHelmet() != null) e.getEquipment().setHelmet(new ItemStack(m)); }
    public static void setChestplate(LivingEntity e, Material m) { if (e.getEquipment().getChestplate() != null) e.getEquipment().setChestplate(new ItemStack(m)); }
    public static void setLeggings(LivingEntity e, Material m) { if (e.getEquipment().getLeggings() != null) e.getEquipment().setLeggings(new ItemStack(m)); }
    public static void setBoots(LivingEntity e, Material m) { if (e.getEquipment().getBoots() != null) e.getEquipment().setBoots(new ItemStack(m)); }
    public static void setHelmet(LivingEntity e, ItemStack i) { e.getEquipment().setHelmet(i); }
    public static void setChestplate(LivingEntity e, ItemStack i) { e.getEquipment().setChestplate(i); }
    public static void setLeggings(LivingEntity e, ItemStack i) { e.getEquipment().setLeggings(i); }
    public static void setBoots(LivingEntity e, ItemStack i) { e.getEquipment().setBoots(i); }
    public static ItemStack getHelmet(LivingEntity e) { return e.getEquipment().getHelmet(); }
    public static ItemStack getChestplate(LivingEntity e) { return e.getEquipment().getChestplate(); }
    public static ItemStack getLeggings(LivingEntity e) { return e.getEquipment().getLeggings(); }
    public static ItemStack getBoots(LivingEntity e) { return e.getEquipment().getBoots(); }
    public static void addEnchantToHelmet(Player p, Enchantment enchant, int lvl) { p.getInventory().getHelmet().addUnsafeEnchantment(enchant, lvl); }
    public static void addEnchantToChestplate(Player p, Enchantment enchant, int lvl) { p.getInventory().getChestplate().addUnsafeEnchantment(enchant, lvl); }
    public static void addEnchantToLeggings(Player p, Enchantment enchant, int lvl) { p.getInventory().getLeggings().addUnsafeEnchantment(enchant, lvl); }
    public static void addEnchantToBoots(Player p, Enchantment enchant, int lvl) { p.getInventory().getBoots().addUnsafeEnchantment(enchant, lvl); }
    public static void addEnchantToItem(Player p, int slot, Enchantment enchant, int lvl) { p.getInventory().getItem(slot).addUnsafeEnchantment(enchant, lvl); }
    public static void addEnchantToItem(ItemStack item, Enchantment enchant, int lvl) { item.addUnsafeEnchantment(enchant, lvl); }
    public static boolean isWearingArmor(LivingEntity e) {
        return !(e.getEquipment().getHelmet().getType().equals(AIR) &&
                 e.getEquipment().getChestplate().getType().equals(AIR) &&
                 e.getEquipment().getLeggings().getType().equals(AIR) &&
                 e.getEquipment().getBoots().getType().equals(AIR));
    }
    public static void addTrim(ItemStack item, TrimPattern trimPattern, TrimMaterial trimMaterial) {
        if (trimPattern == null || trimMaterial == null) return;
        if (!(item.getItemMeta() instanceof ArmorMeta)) return;
        ArmorMeta am = (ArmorMeta) item.getItemMeta();
        ArmorTrim trim = new ArmorTrim(trimMaterial, trimPattern);
        am.setTrim(trim);
        item.setItemMeta(am);
    }

    // Potion
    public static void giveEffect(Entity entity, PotionEffectType effect, float secs, int lvl) {
        if (!(entity instanceof LivingEntity)) return;
        ((LivingEntity) entity).addPotionEffect(new PotionEffect(effect, (int) (secs*20), lvl-1));
    }
    public static void removeEffect(Entity entity, PotionEffectType effect) {
        if (!(entity instanceof LivingEntity)) return;
        ((LivingEntity) entity).removePotionEffect(effect);
    }
    public static String getRandGoodEffect() {
        List<String> potionEffects = new ArrayList<>(List.of(
        "ABSORPTION", "RESISTANCE", "DOLPHINS_GRACE", "FAST_DIGGING",
            "FIRE_RESISTANCE", "HEAL", "HEALTH_BOOST", "HERO_OF_THE_VILLAGE",
            "SHARPNESS", "INVISIBILITY", "JUMP_BOOST",
            "LUCK", "NIGHT_VISION", "REGENERATION", "SATURATION", "SPEED",
            "WATER_BREATHING"
        ));
        return potionEffects.get(getRandNum(0, potionEffects.size()-1));
    }
    public static String getRandBadEffect() {
        List<String> potionEffects = new ArrayList<>(List.of(
            "BAD_OMEN", "BLINDNESS", "CONFUSION", "DARKNESS", "INSTANT_DAMAGE",
            "HUNGER", "POISON", "SLOWNESS", "SLOWNESS_DIGGING", "UNLUCK", "WEAKNESS", "WITHER", "LEVITATION"
        ));
        return potionEffects.get(getRandNum(0, potionEffects.size()-1));
    }
    public static Enchantment getRandGoodEnchantment(String type) {
        List<Enchantment> enchantments = null;
        if (type == "armor") {
            enchantments = new ArrayList<>(List.of(
                PROTECTION, PROJECTILE_PROTECTION, FIRE_PROTECTION, FEATHER_FALLING, BLAST_PROTECTION,
                RESPIRATION, THORNS, SWIFT_SNEAK, SOUL_SPEED
            ));
        }
        else if (type == "weapon") {
            enchantments = new ArrayList<>(List.of(
                SHARPNESS, BANE_OF_ARTHROPODS, SMITE, FIRE_ASPECT,
                KNOCKBACK, SWEEPING_EDGE
            ));
        }
        return enchantments.get(getRandNum(0, enchantments.size()-1));
    }
    public static ItemStack getPotion(PotionEffectType type, int duration, int amplifier, boolean isSplash, Color color) {
        // Create potion
        Material material = isSplash ? Material.SPLASH_POTION : Material.POTION;
        ItemStack potion = new ItemStack(material);

        // Create effect
        PotionEffect potionEffect = new PotionEffect(type, duration, amplifier);

        // Add effect + color
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        potionMeta.addCustomEffect(potionEffect, false);
        potionMeta.setColor(color);
        potion.setItemMeta(potionMeta);

        return potion;
    }

    public static void spawnPotion(Location loc, PotionEffectType type, int duration, int amplifier, Color color) {
        ItemStack potion = getPotion(type, duration, amplifier, true, color);
        ThrownPotion thrownPotion = (ThrownPotion) loc.getWorld().spawnEntity(loc, POTION);
        thrownPotion.setItem(potion);
    }

    // Vectors
    public static Vector getVectorFromPlayerToEntity(Player player, Entity entity) {
        Vector playerEyeLoc = player.getEyeLocation().toVector();
        return entity.getLocation().toVector().subtract(playerEyeLoc).normalize();
    }
    public static Vector getVectorFromEntityToEntity(Entity e1, Entity e2) {
        return e2.getLocation().toVector().subtract(e1.getLocation().toVector()).normalize();
    }
    public static Vector getVectorFromProjectileToEntity(Projectile p1, Entity e2) {
        Vector projLoc = p1.getLocation().toVector();
        Vector entityLoc = e2.getLocation().toVector();
        Vector difference = entityLoc.subtract(projLoc);
        return difference.normalize();
    }
    public static Vector getVectorFromLocationToLocation(Location loc1, Location loc2) {
        Vector v1 = loc1.toVector();
        Vector v2 = loc2.toVector();
        Vector difference = v2.subtract(v1);
        return difference.normalize();
    }
    public static Vector getVectorFromEntityToProjectile(Entity e, Projectile p) {
        Vector projVel = p.getVelocity();
        Vector entityLoc = e.getLocation().toVector();
        Vector difference = projVel.subtract(entityLoc);
        return difference.normalize();
    }
    public static boolean isPlayerLookingAtEntity(Player player, Entity entity) {
        Vector playerLookDir = player.getEyeLocation().getDirection();
        Vector playerEyeLoc = player.getEyeLocation().toVector();
        Vector playerEntityVec = entity.getLocation().toVector().subtract(playerEyeLoc);
        float angle = playerLookDir.angle(playerEntityVec);
        return (angle <= 0.2f);
    }
    public static boolean isPlayerLookingAtPlayer(Player p1, Player p2) {
        if (p1.equals(p2)) return false;
        Vector playerLookDir = p1.getEyeLocation().getDirection();
        Vector playerEyeLoc = p1.getEyeLocation().toVector();
        Vector playerEntityVec = p2.getLocation().toVector().subtract(playerEyeLoc);
        float angle = playerLookDir.angle(playerEntityVec);
        return (angle <= 0.2f);
    }
    public static Location getRelativeLocation(Player player, float dLateral, float dForward, float dVertical) {
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();

        float x = (float) (player.getLocation().getX() + (-1*dLateral * Math.cos(Math.toRadians(yaw))) + (dForward*Math.cos(Math.toRadians(yaw+90))));
        float y = (float) player.getLocation().getY() + dVertical;
        float z = (float) (player.getLocation().getZ() + (-1*dLateral * Math.sin(Math.toRadians(yaw))) + (dForward*Math.sin(Math.toRadians(yaw+90))));
        return new Location(player.getWorld(), x, y, z, yaw, pitch);
    }
    public static double getAngleFromProjectileVelocityToEntity(Projectile p, Entity e) {
        Vector projToEntity = getVectorFromProjectileToEntity(p, e);
        return Math.toDegrees(p.getVelocity().normalize().angle(projToEntity));
    }
    public static Entity getEntityPlayerIsLookingAt(Player player, int range) {
        return getEntityPlayerIsLookingAt(player, range, true);
    }
    public static Entity getEntityPlayerIsLookingAt(Player player, int range, boolean needLineOfSight) {
        Vector playerLookDir = player.getEyeLocation().getDirection();
        Vector playerEyeLoc = player.getEyeLocation().toVector();
        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (needLineOfSight) {
                if (!isAirBetweenPoints(entity.getLocation(), player.getEyeLocation())) continue;
            }
            Vector playerEntityVec = entity.getLocation().toVector().subtract(playerEyeLoc);
            float angle = playerLookDir.angle(playerEntityVec);
            float angle2 = playerLookDir.angle(playerEntityVec.add(new Vector(0, 1, 0)));
            if (angle <= 0.2f || angle2 <= 0.2f) {
                return entity;
            }
        }
        return null;
    }
    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.pow(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2), 0.5);
    }

    // Points (Locations)
    public static Location getMidpoint(Location loc1, Location loc2) {
        return new Location(
            loc1.getWorld(),
            Math.min(loc1.getX(), loc2.getX()) + Math.abs((loc1.getX() - loc2.getX()) / 2),
            Math.min(loc1.getY(), loc2.getY()) + Math.abs((loc1.getY() - loc2.getY()) / 2),
            Math.min(loc1.getZ(), loc2.getY()) + Math.abs((loc1.getZ() - loc2.getZ()) / 2));
    }

    // Lightning
    public static void strikeFakeLightning(Entity e) { strikeFakeLightning(e.getLocation()); }
    public static void strikeFakeLightning(Location loc) { loc.getWorld().strikeLightningEffect(loc); }
    public static void strikeLightning(Entity e) { strikeLightning(e.getLocation()); }
    public static void strikeLightning(Location loc) {
        loc.getWorld().strikeLightning(loc);
    }

    // Events
    public static boolean isAPlayer(Entity entity) { return (entity instanceof Player); }
    public static boolean isLiving(Entity entity) { return (entity instanceof LivingEntity); }
    public static boolean isEntityAHumanEntity(Entity entity) { return (entity instanceof HumanEntity); }
    public static boolean isEntityATrident(Entity entity) { return (entity instanceof Trident); }
    public static boolean isShooterAPlayer(ProjectileSource src) { return (src instanceof Player); }
    public static boolean isProjectileAnItem(Projectile p, Material m) {
        if (!(p instanceof ThrowableProjectile)) return false;
        return ((ThrowableProjectile) p).getItem().getType().equals(m);
    }
    public static boolean isProjectileAWitherSkull(Projectile src) { return (src instanceof WitherSkull); }
    public static boolean isProjectileSourceAPlayer(ProjectileSource src) { return (src instanceof Player); }

    // Armor stands
    public static ArmorStand getNewArmorStand(Location location, boolean visible, boolean mini) {
        ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);

        as.setBasePlate(false);
        as.setArms(true);
        as.setVisible(visible);
        as.setInvulnerable(true);
        as.setCanPickupItems(false);
        as.setGravity(false);
        as.setSmall(mini);
        as.setMarker(true);

        return as;
    }

    //---------------------------------------------------------------------------------------------
    // Items 
    //---------------------------------------------------------------------------------------------
    public static ItemStack addLore(ItemStack item, Component lore) {
        ItemMeta meta = item.getItemMeta();
        List<Component> loreList = meta.lore();
        if (loreList == null) loreList = new ArrayList<>();

        loreList.add(lore);
        meta.lore(loreList);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack hideEnchants(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setDisplayName(ItemStack item, Component displayName) {
        ItemMeta meta = item.getItemMeta();
        meta.displayName(displayName);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack setMaterial(ItemStack item, Material material) {
        ItemStack newItem = new ItemStack(material);
        newItem.setItemMeta(item.getItemMeta());
        return newItem;
    }

    // Blocks
    public static void placeBlock(BlockPlaceEvent event, Material material) {
        event.getBlockReplacedState().setType(material);
        event.getBlockReplacedState().setBlockData(event.getBlockPlaced().getBlockData());
    }
    public static boolean isMaterial(Block b, Material m) {
        return b.getType().equals(m);
    }

    //---------------------------------------------------------------------------------------------
    // Biomes 
    //---------------------------------------------------------------------------------------------
    public static String getReadableBiomeName(Biome biome) {
        return biome.name().replace("_", " ").toLowerCase();
    }

    //---------------------------------------------------------------------------------------------
    // Entities 
    //---------------------------------------------------------------------------------------------
    public static String getReadableEntityTypeName(EntityType entityType) {
        return entityType.name().replace("_", " ").toLowerCase();
    }

    //---------------------------------------------------------------------------------------------
    // Player
    //---------------------------------------------------------------------------------------------
    public static boolean hasMaterial(Player player, Material material, int amount) {
        int totalAmount = 0;
        if (amount == 0) return true;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == material) {
                totalAmount += item.getAmount();
                if (totalAmount >= amount) return true;
            }
        }

        return false;
    }

    //---------------------------------------------------------------------------------------------
    // Strings 
    //---------------------------------------------------------------------------------------------
    public static String intToRomanNumerals(int num) {
        if (num > 10) return Integer.toString(num);

        int[] values = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        String[] symbols = {"X", "IX", "VIII", "VII", "VI", "V", "IV", "III", "II", "I"};
        StringBuilder roman = new StringBuilder();

        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                roman.append(symbols[i]);
                num -= values[i];
            }
        }

        return roman.toString();
    }

    public static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.toLowerCase().split(" ");
        StringBuilder titleCase = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                titleCase.append(Character.toUpperCase(word.charAt(0)))
                         .append(word.substring(1))
                         .append(" ");
            }
        }

        return titleCase.toString().trim();
    }

    //---------------------------------------------------------------------------------------------
    // TextComponent
    //---------------------------------------------------------------------------------------------
    public static String stripColor(String string) {
        String COLOR_CODE_REGEX = "§[0-9a-fk-or]";
        return string.replaceAll(COLOR_CODE_REGEX, "");
    }

    //---------------------------------------------------------------------------------------------
    // Materials 
    //---------------------------------------------------------------------------------------------
    public static String getReadableMaterialName(Material material) {
        return material.name().replace("_", " ").toLowerCase();
    }
}
