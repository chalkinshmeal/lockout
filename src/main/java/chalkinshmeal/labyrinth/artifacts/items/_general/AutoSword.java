package chalkinshmeal.labyrinth.artifacts.items._general;

import chalkinshmeal.labyrinth.artifacts.game.Game;
import chalkinshmeal.labyrinth.artifacts.items.LabyrinthItem;
import chalkinshmeal.labyrinth.data.ConfigHandler;
import chalkinshmeal.labyrinth.utils.Particles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.List;

import static chalkinshmeal.labyrinth.utils.Utils.*;

public class AutoSword extends LabyrinthItem {
    private final int swordCount = 6;
    public List<Boolean> isDeflecting;
    public List<ArmorStand> swords;
    public AutoSword(JavaPlugin plugin, ConfigHandler config, String className, String position, Game game) {
        super(plugin, config, className, position, game);
        this.swords = new ArrayList<>();
        this.isDeflecting = new ArrayList<>();
        for (int i = 0; i < swordCount; i++) { this.isDeflecting.add(false); }
    }

    public void onStart(Player player) {
        //for (int i = 0; i < swordCount; i++) {
        //    ArmorStand as = getNewArmorStand(this.getSwordLocation(player, i), false, false);
        //    ItemStack sword = new ItemStack(GOLDEN_SWORD);
        //    sword.addEnchantment(SHARPNESS, 1);
        //    as.setItem(EquipmentSlot.HAND, sword);
        //    this.game.addPlayerSummons(player, List.of(as));
        //    swords.add(as);
        //}

        //startDeflectTask(this, player, 0.2F, 0.05F);
    }

    // Sword movement
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        //Player player = event.getPlayer();
        //if (!isPlayerHoldingItemInInventory(player)) return;

        //for (int i = 0; i < swordCount; i++) {
        //    if (!this.isDeflecting.get(i))
        //        swords.get(i).teleport(this.getSwordLocation(player, i));
        //}
    }

    public Location getSwordLocation(Player player, int id) {
        switch (id) {
            case 0: return getRelativeLocation(player, 0.3F, -0.2F, 0.4F);
            case 1: return getRelativeLocation(player, -1F,-0.2F, 0.4F);
            case 2: return getRelativeLocation(player, 0.5F, -0.2F, 0F);
            case 3: return getRelativeLocation(player, -1.2F,-0.2F, 0F);
            case 4: return getRelativeLocation(player, 0.7F, -0.2F, -0.4F);
            case 5: return getRelativeLocation(player, -1.4F,-0.2F, -0.4F);
            default: return null;
        }
    }

    public int getFreeSwordIndex() {
        List<Integer> freeIndices = new ArrayList<>();
        for (int i = 0; i < swordCount; i++) {
            if (!this.isDeflecting.get(i)) {
                freeIndices.add(i);
            }
        }
        if (freeIndices.isEmpty()) return -1;
        int randSwordIndex = freeIndices.get(getRandNum(0, freeIndices.size()-1));
        return randSwordIndex;
    }

    public void startDeflectTask(AutoSword autoSword, Player player, float cooldown, float deflectRate) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Get nearby projectiles
                List<Projectile> projectiles = getNearbyProjectiles(player, 1.5);

                // Deflect
                for (Projectile projectile : projectiles) {
                    if (getAngleFromProjectileVelocityToEntity(projectile, player) > 30) continue;
                    if (projectile.isOnGround()) return;
                    int freeSwordIndex = getFreeSwordIndex();
                    if (freeSwordIndex == -1) continue;
                    ArmorStand sword = autoSword.swords.get(freeSwordIndex);
                    autoSword.isDeflecting.set(freeSwordIndex, true);

                    // Deflect
                    projectile.setVelocity(projectile.getVelocity().multiply(-1));
                    if (projectile instanceof Fireball)
                        ((Fireball) projectile).setDirection(((Fireball) projectile).getDirection().multiply(-1));

                    // Deflect animation
                    sword.setRightArmPose(new EulerAngle(Math.toRadians(325), Math.toRadians(0), Math.toRadians(90)));
                    //Location newSwordLocation = sword.getLocation().add(getVectorFromEntityToProjectile(player ,projectile).multiply(1.2));
                    Location newSwordLocation = projectile.getLocation();
                    sword.teleport(newSwordLocation);

                    // Cosmetics
                    playSound(player, Sound.BLOCK_CHAIN_HIT, 2);
                    playSound(player, Sound.BLOCK_CHAIN_STEP, 2);
                    Particles.spawnParticle(newSwordLocation, Particle.FLAME, 10);
                    Particles.spawnParticle(newSwordLocation, Particle.SWEEP_ATTACK, 1);

                    // Undeflect after cooldown
                    doDeflectAnimation(autoSword, freeSwordIndex, cooldown, player);
                }

                // End condition
                if (!autoSword.game.isPlayerInPlay(player)) { this.cancel(); }
            }
        }.runTaskTimer(this.plugin, (long) (20L*0), (long) (20L*deflectRate));
    }

    public void doDeflectAnimation(AutoSword autoSword, int freeSwordIndex, float cooldown, Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // End condition
                autoSword.isDeflecting.set(freeSwordIndex, false);
                autoSword.swords.get(freeSwordIndex).setRightArmPose(new EulerAngle(0, 0, 0));
                autoSword.swords.get(freeSwordIndex).teleport(autoSword.getSwordLocation(player, freeSwordIndex));
                this.cancel();
            }
        }.runTaskTimer(this.plugin, (long) (20L*cooldown), (long) (20L*0));
    }


    //public void onPlayerInteractEvent(PlayerInteractEvent event) {
    //    Player player = event.getPlayer();
    //    if (!isPlayerHoldingItemInMainHand(player)) return;
    //    if (!isRightClick(event)) return;

    //    // Reset state
    //    event.setCancelled(true);

    //    // Spawn armorstand
    //    ArmorStand as = getNewArmorStand(player.getLocation(), true, false);
    //    this.game.addPlayerSummons(player, List.of(as));
    //}
}
