package boardgame.visual.scenes;

import boardgame.controller.SceneManager;
import boardgame.controller.SnLGameController;
import boardgame.model.boardFiles.Player;
import boardgame.model.diceFiles.Dice;
import boardgame.model.effectFiles.SnL.MovementEffect;
import boardgame.visual.elements.SideColumn.DiceButtonVisual;
import boardgame.visual.elements.SideColumn.SideColumnVisual;
import boardgame.visual.gameLayers.SnLTokenLayer;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * Controller for handling in-game actions such as dice rolls, player movement,
 * and updating side UI elements during gameplay.
 * Connects game logic to the visual layer.
 *
 * @author Hector Mendana Morales
 */
public class SnLRollHandler implements RollHandler {

    private final SnLGameController gameController;
    private final SnLTokenLayer playerTokenLayer;
    private final SideColumnVisual sideColumn;
    private final Dice dice = new Dice(1);
    private final int boardSize;

    public SnLRollHandler(SnLGameController gameController, SnLTokenLayer playerTokenLayer, SideColumnVisual sideColumn) {
        this.gameController = gameController;
        this.playerTokenLayer = playerTokenLayer;
        this.sideColumn = sideColumn;
        this.boardSize = gameController.getBoard().getTiles().size();
    }

    /**
     * Animates and completes a player's move by a number of steps.
     * Updates token layer, invokes game logic, and re-enables the roll button.
     *
     * @param player the player to move
     * @param steps the number of tiles to move
     * @param buttonVisual the roll button to be re-enabled after move
     */
    @Override
    public void moveBy(Player player, int steps, DiceButtonVisual buttonVisual) {
        int startPosition = player.getPosition();
        int nextPosition = startPosition + steps;

        playerTokenLayer.moveTokenThroughPath(player, nextPosition);

        int additionalMsDelay = 0;
        //Additional delay for the effect to be shown
        if (gameController.getBoard().getTiles().get(nextPosition - 1).getEffect() instanceof MovementEffect) {
            additionalMsDelay = 300;
        }

        PauseTransition finalPause = new PauseTransition(Duration.millis((steps + 1) * 300 + additionalMsDelay));
        finalPause.setOnFinished(event -> {
            gameController.movePlayer(player, nextPosition);
            sideColumn.turnOnButton();
        });
        finalPause.play();
    }

    /**
     * Handles the dice roll event, initiates player movement and updates the display.
     *
     * @param buttonVisual the roll button that was pressed
     */
    @Override
    public void handleRollDice(DiceButtonVisual buttonVisual) {
        int diceRoll = dice.roll();
        sideColumn.displayRoll(diceRoll);

        Player currentPlayer = gameController.getCurrentPlayer();

        if (currentPlayer.getPosition() + diceRoll >= boardSize) {
            moveBy(gameController.getCurrentPlayer(), boardSize - currentPlayer.getPosition(), buttonVisual);

            PauseTransition gameEndAnimation = new PauseTransition(Duration.millis((boardSize - currentPlayer.getPosition() + 1) * 300 + 300));
            gameEndAnimation.setOnFinished(event -> {
                SceneManager.getInstance().changeScene(
                new WinScreen(
                    currentPlayer.getName(), currentPlayer.getIcon()
                    ).getScene());
            
            });
            gameEndAnimation.play();

        } else {
            moveBy(gameController.getCurrentPlayer(), diceRoll, buttonVisual);
            gameController.advanceTurn();
        }

    }

    /**
     * Instantly moves the token of a player to the given tile number.
     * Called during teleporting effects like ladders or snakes.
     *
     * @param player the player to move
     * @param tileNumber the destination tile
     */
    @Override
    public void moveToken(Player player, int tileNumber) {
        playerTokenLayer.moveToken(player, tileNumber);
    }
}