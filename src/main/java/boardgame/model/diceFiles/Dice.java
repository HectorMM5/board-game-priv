package boardgame.model.diceFiles;
import java.util.ArrayList;

public class Dice {
    private static final ArrayList<Die> dice = new ArrayList<>();

    public Dice(int numberOfDice) {
        for (int i = 0; i < numberOfDice; i++) {
            dice.add(new Die());
        }
    }

    public int roll() {
        int sum = 0;
        for (Die die : dice) {
            die.roll();
            sum += die.getValue();
        }
        return sum;
    }
}