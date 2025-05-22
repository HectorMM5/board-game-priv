package boardgame.model.diceFiles;
import java.util.ArrayList;

/**
 * Represents a set of dice that can be rolled to produce a sum.
 * It manages a collection of {@link Die} objects.
 */
public class Dice {
    private static final ArrayList<Die> dice = new ArrayList<>();

    /**
     * Constructs a {@code Dice} object with a specified number of dice.
     *
     * @param numberOfDice the number of dice to include.
     */
    public Dice(int numberOfDice) {
        dice.clear();
        for (int i = 0; i < numberOfDice; i++) {
            dice.add(new Die());
        }
    }

    /**
     * Rolls all the dice in this set and returns the sum of their values.
     *
     * @return the total value of the dice roll.
     */
    public int roll() {
        int sum = 0;
        for (Die die : dice) {
            die.roll();
            sum += die.getValue();
        }
        return sum;
    }
}