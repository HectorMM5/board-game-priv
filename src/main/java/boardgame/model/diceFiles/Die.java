package boardgame.model.diceFiles;

import java.util.Random;

public class Die {
  public static final Random RANDOM = new Random();
  private int lastRolledValue;

  public void roll() {
    lastRolledValue = RANDOM.nextInt(1, 7);
    //return lastRolledValue;
  }

  public void roll(int max) {
    lastRolledValue = RANDOM.nextInt(1, max + 1);
    //return lastRolledValue;
  }

  public int getValue() {
    return lastRolledValue;
  }
}
