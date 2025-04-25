package boardgame.model.boardFiles;

import java.util.ArrayList;

import boardgame.model.diceFiles.Dice;
import boardgame.model.effectFiles.Effect;

/**
 * Represents a rule configuration for a board game, containing
 * the set of effects allowed and the size of the dice used.
 * 
 * This class also provides access to a {@link Dice} object configured
 * with the specified number of sides.
 * 
 * @author Hector Mendana Morales
 */
public class Rules {
    private final ArrayList<Effect> effects;
    private final int diceSize;
    private final Dice dice;

    /**
     * Constructs a Rules object with the given list of effects and dice size.
     *
     * @param effects the list of effects allowed in the game
     * @param diceSize the number of sides for the dice
     */
    public Rules(ArrayList<Effect> effects, int diceSize) {
        this.effects = effects;
        this.diceSize = diceSize;
        this.dice = new Dice(diceSize);
    }

    /**
     * Returns the {@link Dice} instance configured with this rule's dice size.
     *
     * @return the dice object
     */
    public Dice getDice() {
        return dice;
    }

    /**
     * Returns the list of effects associated with this rule set.
     *
     * @return the list of effects
     */
    public ArrayList<Effect> getEffects() {
        return effects;
    }
}
