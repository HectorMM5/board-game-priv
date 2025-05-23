package boardgame.controller.RollHandlers;

import boardgame.controller.GameControllers.SnLGameController;
import boardgame.model.Player;
import boardgame.model.diceFiles.Dice;
import boardgame.utils.movementType;
import boardgame.visual.elements.SideColumn.SideColumnVisual;
import boardgame.visual.gameLayers.SnLTokenLayer;

/**
 * Controller for handling in-game actions such as dice rolls, player movement,
 * and updating side UI elements during gameplay. Connects game logic to the
 * visual layer.
 *
 *  
 */
public class SnLRollHandler implements RollHandler {

    private final SnLGameController gameController;
    private final SideColumnVisual sideColumn;
    private final SnLTokenLayer playerTokenLayer;
    private final Dice dice = new Dice(1);

    public SnLRollHandler(SnLGameController gameController, SnLTokenLayer playerTokenLayer, SideColumnVisual sideColumn) {
        this.gameController = gameController;
        this.sideColumn = sideColumn;
        this.playerTokenLayer = playerTokenLayer;
    }

    /**
     * Animates and completes a player's move by a number of steps. Updates
     * token layer, invokes game logic, and re-enables the roll button.
     *
     * @param player       the player to move
     * @param steps        the number of tiles to move
     * @param buttonVisual the roll button to be re-enabled after move
     */
    @Override
    public void moveBy(Player player, int steps) {
        int startPosition = player.getPosition();
        int nextPosition = startPosition + steps;

        gameController.movePlayer(player, nextPosition, movementType.PATH);

        if (nextPosition >= 90) {
            return;
        }

        playerTokenLayer.addToAnimationQueue(() -> {
            sideColumn.turnOnButton();
            playerTokenLayer.runNextAnimation();
        });
    }

    /**
     * Handles the dice roll event, initiates player movement and updates the
     * display.
     *
     * @param buttonVisual the roll button that was pressed
     */
    @Override
    public void handleRollDice() {
        int diceRoll = dice.roll();
        sideColumn.displayRoll(diceRoll);

        moveBy(gameController.getCurrentPlayer(), diceRoll);
        gameController.advanceTurn();
    }

}