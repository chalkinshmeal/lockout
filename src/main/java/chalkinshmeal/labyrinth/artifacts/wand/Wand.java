package chalkinshmeal.labyrinth.artifacts.wand;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * WandTool
 * This class handles any tool-related utilities
 */
public class Wand {
    private final UUID playerId;
    private Block pos1;
    private Block pos2;

    /** Constructor */
    public Wand(UUID playerId) {
        this.playerId = playerId;
    }

    /** Setters */
    public void setPos1(Block pos1, Player p) {
        if (pos1 == null) return;
        if (pos1.equals(this.pos1)) return;
        p.sendMessage("First wand position set to (" + pos1.getX() + ", " + pos1.getY() + ", " + pos1.getZ() + ")");
        this.pos1 = pos1;
    }
    public void setPos2(Block pos2, Player p) {
        if (pos2 == null) return;
        if (pos2.equals(this.pos2)) return;
        p.sendMessage("Second wand position set to (" + pos2.getX() + ", " + pos2.getY() + ", " + pos2.getZ() + ")");
        this.pos2 = pos2;
    }

    /** Getters */
    public Block getPos1() { return this.pos1; }
    public Block getPos2() { return this.pos2; }
}
