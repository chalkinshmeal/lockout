package chalkinshmeal.labyrinth.artifacts.items;

import io.papermc.paper.event.entity.EntityMoveEvent;
import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items._general.*;
import chalkinshmeal.labyrinth.artifacts.items.allay.*;
import chalkinshmeal.labyrinth.artifacts.items.axolotl.AxolotlFins;
import chalkinshmeal.labyrinth.artifacts.items.axolotl.AxolotlSlick;
import chalkinshmeal.labyrinth.artifacts.items.axolotl.AxolotlSteal;
import chalkinshmeal.labyrinth.artifacts.items.axolotl.AxolotlUltimate;
import chalkinshmeal.labyrinth.artifacts.items.bat.*;
import chalkinshmeal.labyrinth.artifacts.items.bee.BeeHoneyImmunity;
import chalkinshmeal.labyrinth.artifacts.items.bee.BeeHoneycomb;
import chalkinshmeal.labyrinth.artifacts.items.bee.BeeUltimate;
import chalkinshmeal.labyrinth.artifacts.items.blaze.BlazeBow;
import chalkinshmeal.labyrinth.artifacts.items.blaze.BlazeUltimate;
import chalkinshmeal.labyrinth.artifacts.items.cactus.CactusGreenDye;
import chalkinshmeal.labyrinth.artifacts.items.cactus.CactusPunishment;
import chalkinshmeal.labyrinth.artifacts.items.cactus.CactusUltimate;
import chalkinshmeal.labyrinth.artifacts.items.chicken.ChickenEgg;
import chalkinshmeal.labyrinth.artifacts.items.chicken.ChickenUltimate;
import chalkinshmeal.labyrinth.artifacts.items.chorus_flower.ChorusFlowerFruit;
import chalkinshmeal.labyrinth.artifacts.items.chorus_flower.ChorusFlowerGlobalTeleporter;
import chalkinshmeal.labyrinth.artifacts.items.chorus_flower.ChorusFlowerPersonalTeleporter;
import chalkinshmeal.labyrinth.artifacts.items.creeper.CreeperTnt;
import chalkinshmeal.labyrinth.artifacts.items.creeper.CreeperUltimate;
import chalkinshmeal.labyrinth.artifacts.items.drowned.DrownedRiptide;
import chalkinshmeal.labyrinth.artifacts.items.drowned.DrownedTrident;
import chalkinshmeal.labyrinth.artifacts.items.drowned.DrownedUltimate;
import chalkinshmeal.labyrinth.artifacts.items.dweller.DwellerBone;
import chalkinshmeal.labyrinth.artifacts.items.dweller.DwellerFish;
import chalkinshmeal.labyrinth.artifacts.items.dweller.DwellerUltimate;
import chalkinshmeal.labyrinth.artifacts.items.enderdragon.*;
import chalkinshmeal.labyrinth.artifacts.items.enderman.EndermanEnderpearl;
import chalkinshmeal.labyrinth.artifacts.items.enderman.EndermanEye;
import chalkinshmeal.labyrinth.artifacts.items.enderman.EndermanUltimate;
import chalkinshmeal.labyrinth.artifacts.items.fox.FoxMaster;
import chalkinshmeal.labyrinth.artifacts.items.fox.FoxTamer;
import chalkinshmeal.labyrinth.artifacts.items.horse.HorseLasso;
import chalkinshmeal.labyrinth.artifacts.items.horse.HorseUltimate;
import chalkinshmeal.labyrinth.artifacts.items.iron_golem.IronGolemAnvil;
import chalkinshmeal.labyrinth.artifacts.items.iron_golem.IronGolemFist;
import chalkinshmeal.labyrinth.artifacts.items.obsidian.*;
import chalkinshmeal.labyrinth.artifacts.items.ocelot.OcelotEdibleFish;
import chalkinshmeal.labyrinth.artifacts.items.ocelot.OcelotSlow;
import chalkinshmeal.labyrinth.artifacts.items.ocelot.OcelotUltimate;
import chalkinshmeal.labyrinth.artifacts.items.pig.PigBeetroot;
import chalkinshmeal.labyrinth.artifacts.items.pig.PigCarrotOnAStick;
import chalkinshmeal.labyrinth.artifacts.items.pig.PigHealth;
import chalkinshmeal.labyrinth.artifacts.items.pig.PigUltimate;
import chalkinshmeal.labyrinth.artifacts.items.piglin.PiglinBarter;
import chalkinshmeal.labyrinth.artifacts.items.piglin.PiglinGildedBlackstone;
import chalkinshmeal.labyrinth.artifacts.items.piglin.PiglinTrap;
import chalkinshmeal.labyrinth.artifacts.items.rabbit.*;
import chalkinshmeal.labyrinth.artifacts.items.sheep.SheepGreenWool;
import chalkinshmeal.labyrinth.artifacts.items.sheep.SheepShears;
import chalkinshmeal.labyrinth.artifacts.items.sheep.SheepUltimate;
import chalkinshmeal.labyrinth.artifacts.items.sheep.SheepWool;
import chalkinshmeal.labyrinth.artifacts.items.skeleton.SkeletonBony;
import chalkinshmeal.labyrinth.artifacts.items.skeleton.SkeletonUltimate;
import chalkinshmeal.labyrinth.artifacts.items.slime.*;
import chalkinshmeal.labyrinth.artifacts.items.snow_golem.*;
import chalkinshmeal.labyrinth.artifacts.items.spider.SpiderString;
import chalkinshmeal.labyrinth.artifacts.items.spider.SpiderTripwireHook;
import chalkinshmeal.labyrinth.artifacts.items.spider.SpiderUltimate;
import chalkinshmeal.labyrinth.artifacts.items.spider.SpiderWeb;
import chalkinshmeal.labyrinth.artifacts.items.squid.SquidInkSac;
import chalkinshmeal.labyrinth.artifacts.items.squid.SquidUltimate;
import chalkinshmeal.labyrinth.artifacts.items.villager.VillagerEmerald;
import chalkinshmeal.labyrinth.artifacts.items.villager.VillagerPotato;
import chalkinshmeal.labyrinth.artifacts.items.villager.VillagerUltimate;
import chalkinshmeal.labyrinth.artifacts.items.warden.WardenShriek;
import chalkinshmeal.labyrinth.artifacts.items.warden.WardenUltimate;
import chalkinshmeal.labyrinth.artifacts.items.witch.WitchBroomstick;
import chalkinshmeal.labyrinth.artifacts.items.witch.WitchWand;
import chalkinshmeal.labyrinth.artifacts.items.wither.WitherBow;
import chalkinshmeal.labyrinth.artifacts.items.wither.WitherUltimate;
import chalkinshmeal.labyrinth.artifacts.items.wolf.WolfBeef;
import chalkinshmeal.labyrinth.artifacts.items.wolf.WolfUltimate;
import chalkinshmeal.labyrinth.artifacts.items.zombie.ZombieBite;
import chalkinshmeal.labyrinth.artifacts.items.zombie.ZombieSpawner;
import chalkinshmeal.labyrinth.artifacts.items.zombie.ZombieUltimate;
import chalkinshmeal.labyrinth.artifacts.items.zombie_pigman.ZombiePigmanBling;
import chalkinshmeal.labyrinth.artifacts.items.zombie_pigman.ZombiePigmanUltimate;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.Material;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static chalkinshmeal.labyrinth.utils.Utils.getRandNum;
import static org.bukkit.Material.LIGHT_BLUE_DYE;


public class LabyrinthItemHandler {
    private final JavaPlugin plugin;
    private final ConfigHandler config;
    private final Game game;
    private Map<Integer, LabyrinthItem> labyrinthItems;
    private List<LabyrinthItem> normalItems;

    public LabyrinthItemHandler(JavaPlugin plugin, Game game) {
        this.plugin = plugin;
        this.config = new ConfigHandler(plugin, "plugins/labyrinth/classes.yml");
        this.game = game;
        this.labyrinthItems = this.getLabyrinthItemsFromConfig("classes.");
        this.normalItems = this.getNormalItemsFromConfig("classes.");
    }
    // -------------------------------------------------------------------------------------------
    // Manipulators - purely for LabyrinthClassHandler + LabyrinthClass!
    // -------------------------------------------------------------------------------------------
    public int getTotalNumItems() { return this.labyrinthItems.size() + this.normalItems.size(); }
    public ItemStack getItem(String className, String displayName, String itemPosStr) {
        for (LabyrinthItem item : this.labyrinthItems.values()) {
            //if (item.getClassName().equals(className) && item.getDisplayName().equals(displayName)) { return item.getItemStack(); }
            if (item.getDisplayName().equals(displayName)) { return item.getItemStack(); }
        }
        for (LabyrinthItem item : this.normalItems) {
            if (item.getClassName().equals(className) && item.getPosition().equals(itemPosStr)) { return item.getItemStack(); }
        }
        return null;
    }
    public LabyrinthItem getItem(int specialID) { return this.labyrinthItems.get(specialID); }
    public ItemStack getRandItem(boolean includeUltimates, boolean isSingleItem) {
        if (!isSingleItem) {
            return getRandItem(includeUltimates);
        }
        else {
            ItemStack item = null;
            while (item == null || item.getAmount() != 1) {
                item = getRandItem(includeUltimates);
            }
            return item;
        }
    }
    public ItemStack getRandItem(boolean includeUltimates) {
        List<LabyrinthItem> items = new ArrayList<>();
        for (LabyrinthItem item : this.labyrinthItems.values()) {
            if (!includeUltimates && item.isUltimate()) continue;
            if (item.getPosition().equals("helmet") ||
                item.getPosition().equals("chestplate") ||
                item.getPosition().equals("leggings") ||
                item.getPosition().equals("boots")) continue;
            // TODO: Actually add ability to get lifesapper normally
            if (item.getItemStack().getType().equals(LIGHT_BLUE_DYE)) continue;

            items.add(item);
        }
        for (LabyrinthItem item : this.normalItems) {
            if (item.getPosition().equals("helmet") ||
                item.getPosition().equals("chestplate") ||
                item.getPosition().equals("leggings") ||
                item.getPosition().equals("boots")) continue;
            if (item.getItemStack().getType().equals(Material.ARROW)) continue;
            if (item.getItemStack().getType().equals(Material.TIPPED_ARROW)) continue;
            items.add(item);
        }

        // Select random item
        int index = getRandNum(0, items.size()-1);
        return items.get(index).getItemStack();
    }

    // -------------------------------------------------------------------------------------------
    // Iterators for all classes' event listeners - purely for listeners!
    // -------------------------------------------------------------------------------------------
    public void onSpawn(Player player) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onSpawn(player); }
    public void onBlockBreakEvent(BlockBreakEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onBlockBreakEvent(event); }
    public void onBlockExplodeEvent(BlockExplodeEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onBlockExplodeEvent(event); }
    public void onBlockIgniteEvent(BlockIgniteEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onBlockIgniteEvent(event); }
    public void onBlockPlaceEvent(BlockPlaceEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onBlockPlaceEvent(event); }
    public void onBlockRedstoneEvent(BlockRedstoneEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onBlockRedstoneEvent(event); }
    public void onChunkLoadEvent(ChunkLoadEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onChunkLoadEvent(event); }
    public void onEntityAirChangeEvent(EntityAirChangeEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityAirChangeEvent(event); }
    public void onEntityBreedEvent(EntityBreedEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityBreedEvent(event); }
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityChangeBlockEvent(event); }
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityDamageByEntityEvent(event); }
    public void onEntityDamageEvent(EntityDamageEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityDamageEvent(event); }
    public void onEntityDeathEvent(EntityDeathEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityDeathEvent(event); }
    public void onEntityDropItemEvent(EntityDropItemEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityDropItemEvent(event); }
    public void onEntityExplodeEvent(EntityExplodeEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityExplodeEvent(event); }
    public void onEntityInteractEvent(EntityInteractEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityInteractEvent(event); }
    public void onEntityMoveEvent(EntityMoveEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityMoveEvent(event); }
    public void onEntityPickupItemEvent(EntityPickupItemEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityPickupItemEvent(event); }
    public void onEntityPotionEffectEvent(EntityPotionEffectEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityPotionEffectEvent(event); }
    public void onEntityRegainHealthEvent(EntityRegainHealthEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityRegainHealthEvent(event); }
    public void onEntityShootBowEvent(EntityShootBowEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityShootBowEvent(event); }
    public void onEntitySpawnEvent(EntitySpawnEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntitySpawnEvent(event); }
    public void onEntityTargetEvent(EntityTargetEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityTargetEvent(event); }
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onEntityToggleGlideEvent(event); }
    public void onInventoryClickEvent(InventoryClickEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onInventoryClickEvent(event); }
    public void onInventoryDragEvent(InventoryDragEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onInventoryDragEvent(event); }
    public void onInventoryOpenEvent(InventoryOpenEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onInventoryOpenEvent(event); }
    public void onItemSpawnEvent(ItemSpawnEvent event) throws ReflectiveOperationException { for (LabyrinthItem item : this.labyrinthItems.values()) item.onItemSpawnEvent(event); }
    public void onLightningStrikeEvent(LightningStrikeEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onLightningStrikeEvent(event); }
    public void onPlayerDeathEvent(PlayerDeathEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onPlayerDeathEvent(event); }
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onPlayerDropItemEvent(event); }
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onPlayerInteractEntityEvent(event); }
    public void onPlayerInteractEvent(PlayerInteractEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onPlayerInteractEvent(event); }
    public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onPlayerItemConsumeEvent(event); }
    public void onPlayerItemDamageEvent(PlayerItemDamageEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onPlayerItemDamageEvent(event); }
    public void onPlayerItemHeldEvent(PlayerItemHeldEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onPlayerItemHeldEvent(event); }
    public void onPlayerLeashEntityEvent(PlayerLeashEntityEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onPlayerLeashEntityEvent(event); }
    public void onPlayerMoveEvent(PlayerMoveEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onPlayerMoveEvent(event); }
    public void onPlayerTeleportEvent(PlayerTeleportEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onPlayerTeleportEvent(event); }
    public void onPlayerToggleFlightEvent(PlayerToggleFlightEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onPlayerToggleFlightEvent(event); }
    public void onProjectileHitEvent(ProjectileHitEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onProjectileHitEvent(event); }
    public void onSlimeSplitEvent(SlimeSplitEvent event) { for (LabyrinthItem item : this.labyrinthItems.values()) item.onSlimeSplitEvent(event); }

    // -------------------------------------------------------------------------------------------
    // Config
    // -------------------------------------------------------------------------------------------
    public Map<Integer, LabyrinthItem> getLabyrinthItemsFromConfig(String pathToClasses) {
        Map<Integer, LabyrinthItem> labyrinthItems = new HashMap<>();
        if (!config.doesKeyExist(pathToClasses)) return labyrinthItems;

        for (String className : config.getKeyListFromKey(pathToClasses)) {
            String pathToItems = pathToClasses + "." + className + ".items.";
            for (String invPosStr : config.getKeyListFromKey(pathToItems)) {
                int itemID = config.getInt(pathToItems + "." + invPosStr + ".specialID", -1);
                switch (itemID) {
                    case 0: labyrinthItems.put(itemID, new BlazeBow(plugin, config, className, invPosStr, game)); break;
                    case 1: labyrinthItems.put(itemID, new BlazeUltimate(plugin, config, className, invPosStr, game)); break;
                    case 2: labyrinthItems.put(itemID, new SpiderWeb(plugin, config, className, invPosStr, game)); break;
                    case 3: labyrinthItems.put(itemID, new SpiderUltimate(plugin, config, className, invPosStr, game)); break;
                    case 4: labyrinthItems.put(itemID, new SquidInkSac(plugin, config, className, invPosStr, game)); break;
                    case 5: labyrinthItems.put(itemID, new SquidUltimate(plugin, config, className, invPosStr, game)); break;
                    case 6: labyrinthItems.put(itemID, new AxolotlFins(plugin, config, className, invPosStr, game)); break;
                    case 7: labyrinthItems.put(itemID, new AxolotlSteal(plugin, config, className, invPosStr, game)); break;
                    case 8: labyrinthItems.put(itemID, new AxolotlSlick(plugin, config, className, invPosStr, game)); break;
                    case 9: labyrinthItems.put(itemID, new AxolotlUltimate(plugin, config, className, invPosStr, game)); break;
                    case 10: labyrinthItems.put(itemID, new BeeHoneycomb(plugin, config, className, invPosStr, game)); break;
                    case 11: labyrinthItems.put(itemID, new BeeHoneyImmunity(plugin, config, className, invPosStr, game)); break;
                    case 12: labyrinthItems.put(itemID, new BeeUltimate(plugin, config, className, invPosStr, game)); break;
                    case 13: labyrinthItems.put(itemID, new CactusUltimate(plugin, config, className, invPosStr, game)); break;
                    case 14: labyrinthItems.put(itemID, new ChorusFlowerFruit(plugin, config, className, invPosStr, game)); break;
                    case 15: labyrinthItems.put(itemID, new ChorusFlowerPersonalTeleporter(plugin, config, className, invPosStr, game)); break;
                    case 16: labyrinthItems.put(itemID, new ChorusFlowerGlobalTeleporter(plugin, config, className, invPosStr, game)); break;
                    case 17: labyrinthItems.put(itemID, new ChickenEgg(plugin, config, className, invPosStr, game)); break;
                    case 18: labyrinthItems.put(itemID, new ChickenUltimate(plugin, config, className, invPosStr, game)); break;
                    case 19: labyrinthItems.put(itemID, new CreeperTnt(plugin, config, className, invPosStr, game)); break;
                    case 20: labyrinthItems.put(itemID, new CreeperUltimate(plugin, config, className, invPosStr, game)); break;
                    case 21: labyrinthItems.put(itemID, new DrownedRiptide(plugin, config, className, invPosStr, game)); break;
                    case 22: labyrinthItems.put(itemID, new DrownedTrident(plugin, config, className, invPosStr, game)); break;
                    case 23: labyrinthItems.put(itemID, new DrownedUltimate(plugin, config, className, invPosStr, game)); break;
                    case 24: labyrinthItems.put(itemID, new DwellerBone(plugin, config, className, invPosStr, game)); break;
                    case 25: labyrinthItems.put(itemID, new DwellerFish(plugin, config, className, invPosStr, game)); break;
                    case 26: labyrinthItems.put(itemID, new DwellerUltimate(plugin, config, className, invPosStr, game)); break;
                    case 27: labyrinthItems.put(itemID, new EnderdragonBreath(plugin, config, className, invPosStr, game)); break;
                    case 28: labyrinthItems.put(itemID, new EnderdragonFlight(plugin, config, className, invPosStr, game)); break;
                    case 29: labyrinthItems.put(itemID, new EnderdragonImmunity(plugin, config, className, invPosStr, game)); break;
                    case 30: labyrinthItems.put(itemID, new EnderdragonHealth(plugin, config, className, invPosStr, game)); break;
                    case 31: labyrinthItems.put(itemID, new EnderdragonJump(plugin, config, className, invPosStr, game)); break;
                    case 32: labyrinthItems.put(itemID, new EnderdragonUltimate(plugin, config, className, invPosStr, game)); break;
                    case 33: labyrinthItems.put(itemID, new EndermanEye(plugin, config, className, invPosStr, game)); break;
                    case 34: labyrinthItems.put(itemID, new EndermanUltimate(plugin, config, className, invPosStr, game)); break;
                    case 35: labyrinthItems.put(itemID, new HorseLasso(plugin, config, className, invPosStr, game)); break;
                    case 36: labyrinthItems.put(itemID, new SpeedI(plugin, config, className, invPosStr, game)); break;
                    case 37: labyrinthItems.put(itemID, new HorseUltimate(plugin, config, className, invPosStr, game)); break;
                    case 38: labyrinthItems.put(itemID, new IronGolemFist(plugin, config, className, invPosStr, game)); break;
                    case 39: labyrinthItems.put(itemID, new IronGolemAnvil(plugin, config, className, invPosStr, game)); break;
                    case 40: labyrinthItems.put(itemID, new ObsidianHarden(plugin, config, className, invPosStr, game)); break;
                    case 41: labyrinthItems.put(itemID, new ObsidianCryingObsidian(plugin, config, className, invPosStr, game)); break;
                    case 42: labyrinthItems.put(itemID, new ObsidianBeacon(plugin, config, className, invPosStr, game)); break;
                    case 43: labyrinthItems.put(itemID, new KnockbackResistance(plugin, config, className, invPosStr, game)); break;
                    case 44: labyrinthItems.put(itemID, new ObsidianHealth(plugin, config, className, invPosStr, game)); break;
                    case 45: labyrinthItems.put(itemID, new ObsidianUltimate(plugin, config, className, invPosStr, game)); break;
                    case 46: labyrinthItems.put(itemID, new OcelotSlow(plugin, config, className, invPosStr, game)); break;
                    case 47: labyrinthItems.put(itemID, new OcelotEdibleFish(plugin, config, className, invPosStr, game)); break;
                    case 48: labyrinthItems.put(itemID, new OcelotUltimate(plugin, config, className, invPosStr, game)); break;
                    case 49: labyrinthItems.put(itemID, new PigCarrotOnAStick(plugin, config, className, invPosStr, game)); break;
                    case 50: labyrinthItems.put(itemID, new PigBeetroot(plugin, config, className, invPosStr, game)); break;
                    case 51: labyrinthItems.put(itemID, new PigHealth(plugin, config, className, invPosStr, game)); break;
                    case 52: labyrinthItems.put(itemID, new PigUltimate(plugin, config, className, invPosStr, game)); break;
                    case 53: labyrinthItems.put(itemID, new SheepWool(plugin, config, className, invPosStr, game)); break;
                    case 54: labyrinthItems.put(itemID, new SheepShears(plugin, config, className, invPosStr, game)); break;
                    case 55: labyrinthItems.put(itemID, new SheepUltimate(plugin, config, className, invPosStr, game)); break;
                    case 56: labyrinthItems.put(itemID, new SheepGreenWool(plugin, config, className, invPosStr, game)); break;
                    case 57: labyrinthItems.put(itemID, new SkeletonUltimate(plugin, config, className, invPosStr, game)); break;
                    case 58: labyrinthItems.put(itemID, new SnowGolemStick(plugin, config, className, invPosStr, game)); break;
                    case 59: labyrinthItems.put(itemID, new SnowGolemSnowballs(plugin, config, className, invPosStr, game)); break;
                    case 60: labyrinthItems.put(itemID, new SnowGolemPumpkin(plugin, config, className, invPosStr, game)); break;
                    case 61: labyrinthItems.put(itemID, new SnowGolemSnowyFeet(plugin, config, className, invPosStr, game)); break;
                    case 62: labyrinthItems.put(itemID, new SnowGolemUltimate(plugin, config, className, invPosStr, game)); break;
                    case 63: labyrinthItems.put(itemID, new VillagerEmerald(plugin, config, className, invPosStr, game)); break;
                    case 64: labyrinthItems.put(itemID, new VillagerPotato(plugin, config, className, invPosStr, game)); break;
                    case 65: labyrinthItems.put(itemID, new VillagerUltimate(plugin, config, className, invPosStr, game)); break;
                    case 66: labyrinthItems.put(itemID, new WardenShriek(plugin, config, className, invPosStr, game)); break;
                    case 67: labyrinthItems.put(itemID, new WardenUltimate(plugin, config, className, invPosStr, game)); break;
                    case 68: labyrinthItems.put(itemID, new WitherBow(plugin, config, className, invPosStr, game)); break;
                    case 69: labyrinthItems.put(itemID, new WitherUltimate(plugin, config, className, invPosStr, game)); break;
                    case 70: labyrinthItems.put(itemID, new WolfBeef(plugin, config, className, invPosStr, game)); break;
                    case 71: labyrinthItems.put(itemID, new WolfUltimate(plugin, config, className, invPosStr, game)); break;
                    case 72: labyrinthItems.put(itemID, new ZombieBite(plugin, config, className, invPosStr, game)); break;
                    case 73: labyrinthItems.put(itemID, new ZombieSpawner(plugin, config, className, invPosStr, game)); break;
                    case 74: labyrinthItems.put(itemID, new ZombieUltimate(plugin, config, className, invPosStr, game)); break;
                    case 75: labyrinthItems.put(itemID, new ZombiePigmanBling(plugin, config, className, invPosStr, game)); break;
                    case 76: labyrinthItems.put(itemID, new ZombiePigmanUltimate(plugin, config, className, invPosStr, game)); break;
                    case 77: labyrinthItems.put(itemID, new SkeletonBony(plugin, config, className, invPosStr, game)); break;
                    case 78: labyrinthItems.put(itemID, new SlimeSpawner(plugin, config, className, invPosStr, game)); break;
                    case 79: labyrinthItems.put(itemID, new SlimeGooImmunity(plugin, config, className, invPosStr, game)); break;
                    case 80: labyrinthItems.put(itemID, new SlimeSticky(plugin, config, className, invPosStr, game)); break;
                    case 81: labyrinthItems.put(itemID, new SlimeBall(plugin, config, className, invPosStr, game)); break;
                    case 82: labyrinthItems.put(itemID, new SlimeUltimate(plugin, config, className, invPosStr, game)); break;
                    case 83: labyrinthItems.put(itemID, new WitchWand(plugin, config, className, invPosStr, game)); break;
                    case 84: labyrinthItems.put(itemID, new WitchBroomstick(plugin, config, className, invPosStr, game)); break;
                    case 85: labyrinthItems.put(itemID, new LightningImmunity(plugin, config, className, invPosStr, game)); break;
                    case 86: labyrinthItems.put(itemID, new EndermanEnderpearl(plugin, config, className, invPosStr, game)); break;
                    case 87: labyrinthItems.put(itemID, new FireResistance(plugin, config, className, invPosStr, game)); break;
                    case 88: labyrinthItems.put(itemID, new WitherProtection(plugin, config, className, invPosStr, game)); break;
                    case 89: labyrinthItems.put(itemID, new RandomItem(plugin, config, className, invPosStr, game)); break;
                    //case 90: labyrinthItems.put(itemID, new AutoSword(plugin, config, className, invPosStr, game)); break;
                    //case 91: labyrinthItems.put(itemID, new SummonGold(plugin, config, className, invPosStr, game)); break;
                    //case 92: labyrinthItems.put(itemID, new LightningCircle(plugin, config, className, invPosStr, game)); break;
                    case 93: labyrinthItems.put(itemID, new RabbitFoot(plugin, config, className, invPosStr, game)); break;
                    case 94: labyrinthItems.put(itemID, new RabbitKillerBunnySpawner(plugin, config, className, invPosStr, game)); break;
                    case 95: labyrinthItems.put(itemID, new RabbitLuckyDiamond(plugin, config, className, invPosStr, game)); break;
                    case 96: labyrinthItems.put(itemID, new RabbitSpawner(plugin, config, className, invPosStr, game)); break;
                    case 97: labyrinthItems.put(itemID, new RabbitUltimate(plugin, config, className, invPosStr, game)); break;
                    case 98: labyrinthItems.put(itemID, new CactusPunishment(plugin, config, className, invPosStr, game)); break;
                    case 99: labyrinthItems.put(itemID, new CactusGreenDye(plugin, config, className, invPosStr, game)); break;
                    case 100: labyrinthItems.put(itemID, new AllayLifeSapper(plugin, config, className, invPosStr, game)); break;
                    case 101: labyrinthItems.put(itemID, new AllaySwarm(plugin, config, className, invPosStr, game)); break;
                    case 102: labyrinthItems.put(itemID, new AllayFragile(plugin, config, className, invPosStr, game)); break;
                    case 103: labyrinthItems.put(itemID, new AllayJukebox(plugin, config, className, invPosStr, game)); break;
                    case 104: labyrinthItems.put(itemID, new AllayUltimate(plugin, config, className, invPosStr, game)); break;
                    case 105: labyrinthItems.put(itemID, new AllaySoulLantern(plugin, config, className, invPosStr, game)); break;
                    //case 106: labyrinthItems.put(itemID, new WitherDeflect(plugin, config, className, invPosStr, game)); break;
                    case 107: labyrinthItems.put(itemID, new FoxTamer(plugin, config, className, invPosStr, game)); break;
                    case 108: labyrinthItems.put(itemID, new LabyrinthItem(plugin, config, className, invPosStr, game)); break; // Berries
                    case 109: labyrinthItems.put(itemID, new FoxMaster(plugin, config, className, invPosStr, game)); break; // Berries
                    case 110: labyrinthItems.put(itemID, new BatFragile(plugin, config, className, invPosStr, game)); break; // Berries
                    case 111: labyrinthItems.put(itemID, new BatFangs(plugin, config, className, invPosStr, game)); break; // Berries
                    case 112: labyrinthItems.put(itemID, new BatShriek(plugin, config, className, invPosStr, game)); break; // Berries
                    case 113: labyrinthItems.put(itemID, new BatLaser(plugin, config, className, invPosStr, game)); break; // Berries
                    case 114: labyrinthItems.put(itemID, new BatUltimate(plugin, config, className, invPosStr, game)); break; // Berries
                    case 115: labyrinthItems.put(itemID, new PiglinGildedBlackstone(plugin, config, className, invPosStr, game)); break; // Berries
                    case 116: labyrinthItems.put(itemID, new PiglinTrap(plugin, config, className, invPosStr, game)); break; // Berries
                    case 117: labyrinthItems.put(itemID, new SpiderString(plugin, config, className, invPosStr, game)); break; // Berries
                    case 118: labyrinthItems.put(itemID, new SpiderTripwireHook(plugin, config, className, invPosStr, game)); break; // Berries
                    case 119: labyrinthItems.put(itemID, new PiglinBarter(plugin, config, className, invPosStr, game)); break; // Berries
                }
            }
        }
        return labyrinthItems;
    }
    public List<LabyrinthItem> getNormalItemsFromConfig(String pathToClasses) {
        List<LabyrinthItem> items = new ArrayList<>();
        if (!config.doesKeyExist(pathToClasses)) return items;

        for (String className : config.getKeyListFromKey(pathToClasses)) {
            String pathToItems = pathToClasses + "." + className + ".items.";
            for (String invPosStr : config.getKeyListFromKey(pathToItems)) {
                int itemID = config.getInt(pathToItems + "." + invPosStr + ".specialID", -1);
                if (itemID != -1) continue;
                items.add(new LabyrinthItem(plugin, config, className, invPosStr, game));
            }
        }
        return items;
    }
}
