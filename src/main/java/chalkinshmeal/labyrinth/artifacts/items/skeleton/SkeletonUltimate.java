package chalkinshmeal.labyrinth.artifacts.items.skeleton;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import static chalkinshmeal.labyrinth.utils.Utils.*;
import static org.bukkit.Material.BOW;

public class SkeletonUltimate extends LabyrinthItem {
    public SkeletonUltimate(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
    }
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!isRightClick(event)) return;
        if (!isPlayerHoldingItemInMainHand(player)) return;
        if (!doesPlayerHaveUltimate(player)) return;

        // Reset/Record state
        event.setCancelled(true);
        useHeldItem(player);
        setUsedUltimate(player);
        experienceCountdown(plugin, game, player, getDurationInSeconds());
        setUltimateDone(plugin, game, player, getDurationInSeconds());

        // Cosmetics
        strikeFakeLightning(player);
        broadcastGameMsg(ChatColor.AQUA + "The skeleton's bow has been powered up...");

        // Start gatling gun
        skeletonGatlingGunTask(game, player, (int) getDurationInSeconds(), (int) getCooldownInSeconds());
    }
    public void skeletonGatlingGunTask(Game game, Player player, int duration, int cooldown) {
        new BukkitRunnable() {
            float secsLeft = duration;
            @Override
            public void run() {
                secsLeft -= cooldown;
                if (!isUltimateActive(player)) return;
                if (!isPlayerHoldingItemInMainHand(player, BOW)) return;

                player.launchProjectile(Arrow.class);

                // End condition
                if (secsLeft <= 0) { this.cancel(); }
            }
        }.runTaskTimer(this.plugin, 20L*0L, 20L*cooldown);
    }

    //    // Give player knight items
    //    LabyrinthItem bow = new LabyrinthItem(Map.of(
    //        "position", 0,
    //        "material", BOW,
    //        "enchants", Map.of("ARROW_DAMAGE", 2, "ARROW_FIRE", 1),
    //        "displayName", "§bTrap Bow"));
    //    player.getInventory().setItem(0, bow.getItemStack());
    //    setHelmet(player, Material.CHAINMAIL_HELMET);
    //    setChestplate(player, Material.CHAINMAIL_CHESTPLATE);
    //    setLeggings(player, Material.CHAINMAIL_LEGGINGS);
    //    setBoots(player, Material.CHAINMAIL_BOOTS);

    // Skeleton Horse summon
    //public void onPlayerInteractEvent(PlayerInteractEvent event) {
    //    Player player = event.getPlayer();
    //    if (!isPlayerHoldingItemInMainHand(player)) return;
    //    if (!isRightClick(event)) return;

    //    // Reset/Record state
    //    event.setCancelled(true);
    //    useHeldItem(player);
    //    setUsedUltimate(player);

    //    // Cosmetics
    //    strikeFakeLightning(player);
    //    broadcastGameMsg(ChatColor.AQUA + "A skeleton trap has been summoned...");

    //    // Spawn horse and put player on it
    //    SkeletonHorse horse = (SkeletonHorse) player.getWorld().spawnEntity(player.getLocation(), EntityType.SKELETON_HORSE);
    //    horse.setJumpStrength(5);
    //    horse.setTamed(true);
    //    horse.setOwner(player);
    //    horse.setPersistent(false);
    //    horse.addPassenger(player);
    //    horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
    //    giveEffect(horse, SPEED, 100000, 2);
    //    giveEffect(horse, SLOWNESS_FALLING, 100000, 1);
    //    giveEffect(horse, ABSORPTION, 100000, 10);
    //    this.game.addPlayerSummons(player, List.of(horse));

    //    // Give player knight items
    //    LabyrinthItem bow = new LabyrinthItem(Map.of(
    //        "position", 0,
    //        "material", BOW,
    //        "enchants", Map.of("ARROW_DAMAGE", 2, "ARROW_FIRE", 1),
    //        "displayName", "§bTrap Bow"));
    //    player.getInventory().setItem(0, bow.getItemStack());
    //    setHelmet(player, Material.CHAINMAIL_HELMET);
    //    setChestplate(player, Material.CHAINMAIL_CHESTPLATE);
    //    setLeggings(player, Material.CHAINMAIL_LEGGINGS);
    //    setBoots(player, Material.CHAINMAIL_BOOTS);
    //}
}
