package boardgame.controller.RollHandlers;

import boardgame.controller.GameControllers.SnLGameController;
import boardgame.model.boardFiles.Player;
import boardgame.model.diceFiles.Dice;
import boardgame.model.effectFiles.SnL.MovementEffect;
import boardgame.utils.movementType;
import boardgame.visual.elements.SideColumn.DiceButtonVisual;
import boardgame.visual.elements.SideColumn.SideColumnVisual;
import boardgame.visual.gameLayers.SnLTokenLayer;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * Controller for handling in-game actions such as dice rolls, player movement,
 * and updating side UI elements during gameplay. Connects game logic to the
 * visual layer.
 *
 * @author Hector Mendana Morales
 */
public class SnLRollHandler implements RollHandler {

    private final SnLGameController gameController;
    private final SideColumnVisual sideColumn;
    private final Dice dice = new Dice(1);

    public SnLRollHandler(SnLGameController gameController, SnLTokenLayer playerTokenLayer, SideColumnVisual sideColumn) {
        this.gameController = gameController;
        this.sideColumn = sideColumn;
    }

    /**
     * Animates and completes a player's move by a number of steps. Updates
     * token layer, invokes game logic, and re-enables the roll button.
     *
     * @param player the player to move
     * @param steps the number of tiles to move
     * @param buttonVisual the roll button to be re-enabled after move
     */
    @Override
    public void moveBy(Player player, int steps, DiceButtonVisual buttonVisual) {
        int startPosition = player.getPosition();
        int nextPosition = startPosition + steps;

        gameController.movePlayer(player, nextPosition, movementType.PATH);

        if (nextPosition >= 90) {
            return;
        }

        int additionalMsDelay = 0;
        //Additional delay for the effect to be shown
        if (gameController.getBoard().getTiles().get(nextPosition - 1).getEffect() instanceof MovementEffect) {
            additionalMsDelay = 300;
        }

        PauseTransition buttonPause = new PauseTransition(Duration.millis((steps + 1) * 300 + additionalMsDelay));
        buttonPause.setOnFinished(event -> {
            sideColumn.turnOnButton();
        });
        buttonPause.play();
    }

    /**
     * Handles the dice roll event, initiates player movement and updates the
     * display.
     *
     * @param buttonVisual the roll button that was pressed
     */
    @Override
    public void handleRollDice(DiceButtonVisual buttonVisual) {
        int diceRoll = dice.roll();
        sideColumn.displayRoll(diceRoll);

        moveBy(gameController.getCurrentPlayer(), diceRoll, buttonVisual);
        gameController.advanceTurn();
    }

}
