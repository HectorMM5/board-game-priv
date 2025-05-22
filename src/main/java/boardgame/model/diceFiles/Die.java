package boardgame.model.diceFiles;

import java.util.Random;

/**
 * Represents a single six-sided die that can be rolled to produce a random value.
 */
public class Die {
    public static final Random RANDOM = new Random();
    private int lastRolledValue;

    /**
     * Rolls the die, producing a random integer value between 1 and 6 (inclusive).
     */
    public void roll() {
        lastRolledValue = RANDOM.nextInt(1, 7);
        //return lastRolledValue;
    }

    /**
     * Rolls the die, producing a random integer value between 1 and a specified maximum (inclusive).
     *
     * @param max the maximum value the die can roll.
     */
    public void roll(int max) {
        lastRolledValue = RANDOM.nextInt(1, max + 1);
        //return lastRolledValue;
    }

    /**
     * Gets the value of the last roll of the die.
     *
     * @return the last rolled value.
     */
    public int getValue() {
        return lastRolledValue;
    }
}