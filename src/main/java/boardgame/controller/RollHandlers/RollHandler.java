package boardgame.controller.RollHandlers;

import boardgame.model.Player;
import boardgame.visual.elements.SideColumn.DiceButtonVisual;

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
     * @param buttonVisual the visual roll button to update after move
     */
    public void moveBy(Player player, int steps, DiceButtonVisual buttonVisual);

    /**
     * Handles the logic for when the player rolls the dice.
     *
     * @param buttonVisual the visual roll button that was pressed
     */
    public void handleRollDice(DiceButtonVisual buttonVisual);
}
