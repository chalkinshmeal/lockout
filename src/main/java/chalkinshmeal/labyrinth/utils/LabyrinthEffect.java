package chalkinshmeal.labyrinth.utils;

import chalkinshmeal.labyrinth.data.ConfigHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class LabyrinthEffect {
    private String name;
    private PotionEffect effect;

    public LabyrinthEffect(String name, float duration, Integer amplifier,
                     Boolean isAmbient, Boolean hasParticles, Boolean hasIcon) {
        // Default values
        if (duration == -1) duration = Float.MAX_VALUE;
        if (isAmbient == null) isAmbient = false;
        if (hasParticles == null) hasParticles = false;
        if (hasIcon == null) hasIcon = false;

        // Check inputs
        try {
            this.name = name;
            this.effect = new PotionEffect(PotionEffectType.getByName(name), (int) (duration*20), amplifier-1,
                                           isAmbient, hasParticles, hasIcon);
        }
        catch (Exception e) {
            System.out.println("Could not find effect name '" + name + "' with duration '" + duration + "' and amplifier '" + amplifier + "'");
        }
    }

    // Getters
    public PotionEffect getPotionEffect() { return this.effect; }

    public static List<LabyrinthEffect> getLabyrinthEffectsFromConfig(ConfigHandler config, String pathToEffects) {
        if (!config.doesKeyExist(pathToEffects)) { return new ArrayList<>(); }

        List<LabyrinthEffect> effects = new ArrayList<>();
        for (String effectName : config.getKeyListFromKey(pathToEffects)) {
            float duration = -1;
            String pathToEffect = pathToEffects + "." + effectName;
            String pathToDuration = pathToEffect + ".duration";
            String pathToAmplifier = pathToEffect + ".amplifier";
            String pathToIsAmbient = pathToEffect + ".ambient";
            String pathToHasParticles = pathToEffect + ".has-particles";
            String pathToHasIcon = pathToEffect + ".has-icon";

            if (config.doesKeyExist(pathToDuration)) duration = config.getFloat(pathToDuration, -1);
            Integer amplifier = (Integer) config.getValue(pathToAmplifier);
            Boolean isAmbient = (Boolean) config.getValue(pathToIsAmbient);
            Boolean hasParticles = (Boolean) config.getValue(pathToHasParticles);
            Boolean hasIcon = (Boolean) config.getValue(pathToHasIcon);

            effects.add(new LabyrinthEffect(effectName, duration, amplifier, isAmbient, hasParticles, hasIcon));
        }
        return effects;
    }
}
