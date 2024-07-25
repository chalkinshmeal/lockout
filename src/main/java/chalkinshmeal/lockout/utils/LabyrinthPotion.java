package chalkinshmeal.lockout.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import chalkinshmeal.lockout.data.ConfigHandler;

import java.util.ArrayList;
import java.util.List;

public class LabyrinthPotion {
    public static ItemStack setPotionEffect(ItemStack item, String potionTypeStr) {
        // If item is not a potion, return null
        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof PotionMeta)) return null;

        // If potion type is not valid, return null
        PotionType potionType = null;
        try {
            potionType = PotionType.valueOf(potionTypeStr.toUpperCase());
        }
        catch (Exception e) {
            return null;
        }

        PotionMeta potionMeta = (PotionMeta) itemMeta;
        PotionData newPotionData = new PotionData(potionType);

        // Set potionMeta type
        potionMeta.setBasePotionData(newPotionData);

        // Set item metadata
        item.setItemMeta(potionMeta);

        return item;
    }

    public static String getPotionEffectStr(ItemStack item) {
        // If item is not a potion, return null
        ItemMeta itemMeta = item.getItemMeta();
        if (!(itemMeta instanceof PotionMeta)) return null;

        PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
        PotionType potionType = potionMeta.getBasePotionData().getType();
        return potionType.name();
    }

    // Config
    public static List<LabyrinthEffect> getEffectsFromConfig(ConfigHandler config, String pathToPotionType) {
        List<LabyrinthEffect> potionEffects = new ArrayList<>();
        for (String effectName: config.getKeyListFromKey(pathToPotionType)) {
            String pathToEffect = pathToPotionType + "." + effectName;
            String pathToDuration = pathToEffect + ".duration";
            String pathToAmplifier = pathToEffect + ".amplifier";
            String pathToIsAmbient = pathToEffect + ".ambient";
            String pathToHasParticles = pathToEffect + ".has-particles";
            String pathToHasIcon = pathToEffect + ".has-icon";

            float duration = config.getFloat(pathToDuration, 0F);
            Integer amplifier = (Integer) config.getValue(pathToAmplifier);
            Boolean isAmbient = (Boolean) config.getValue(pathToIsAmbient);
            Boolean hasParticles = (Boolean) config.getValue(pathToHasParticles);
            Boolean hasIcon = (Boolean) config.getValue(pathToHasIcon);

            potionEffects.add(new LabyrinthEffect(effectName, duration, amplifier, isAmbient, hasParticles, hasIcon));
        }
        return potionEffects;
    }
}
