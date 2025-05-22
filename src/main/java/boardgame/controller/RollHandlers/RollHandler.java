package boardgame.controller.RollHandlers;

import boardgame.model.Player;

/**
 * Interface for controlling game-related actions triggered by dice rolls.
 * Implementing classes are responsible for managing player movement and turn flow.
 */
public interface RollHandler {

    /**
     * Moves the specified player a given number of steps across the board.
     *
     * @param player the player to move
     * @param steps the number of steps to move the player
     */
    public void moveBy(Player player, int steps);

    /**
     * Handles the logic for when the player rolls the dice.
     */
    public void handleRollDice();
}
