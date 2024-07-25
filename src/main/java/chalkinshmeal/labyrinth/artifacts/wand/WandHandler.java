package chalkinshmeal.labyrinth.artifacts.wand;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WandHandler {
    private final Map<UUID, Wand> playerWands;

    /** Constructor */
    public WandHandler() {
        this.playerWands = new HashMap<>();
    }

    /** Getters */
    // Get wand. If playerID has no wand, create it
    public Wand getWand(UUID playerId) {
        // Create wand if playerId does not exist
        if (!this.playerWands.containsKey(playerId))
            this.createWand(playerId);

        // Return wand
        return this.playerWands.get(playerId);
    }

    /** Adders */
    public void createWand(UUID playerId) {
        this.playerWands.put(playerId, new Wand(playerId));
    }

}
