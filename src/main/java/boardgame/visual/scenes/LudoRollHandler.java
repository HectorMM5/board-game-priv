package boardgame.visual.scenes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import boardgame.controller.LudoGameController;
import boardgame.model.boardFiles.Player;
import boardgame.model.diceFiles.Dice;
import boardgame.visual.elements.SideColumn.DiceButtonVisual;
import boardgame.visual.elements.SideColumn.SideColumnVisual;
import boardgame.visual.gameLayers.PlayerTokenLayer;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * Controller for handling in-game actions such as dice rolls, player movement,
 * and updating side UI elements during gameplay. Connects game logic to the
 * visual layer.
 *
 * @author Hector Mendana Morales
 */
public class LudoRollHandler implements RollHandler {

    private final LudoGameController gameController;
    private final PlayerTokenLayer playerTokenLayer;
    private final SideColumnVisual sideColumn;
    private final Dice dice = new Dice(1);
    private final int boardSize;

    private final Map<Player, Integer> tilesMoved = new HashMap<>();

    public LudoRollHandler(LudoGameController gameController, PlayerTokenLayer playerTokenLayer, SideColumnVisual sideColumn) {
        this.gameController = gameController;
        this.playerTokenLayer = playerTokenLayer;
        this.sideColumn = sideColumn;
        this.boardSize = gameController.getBoard().getTiles().size();

        List<Player> players = gameController.getPlayers();

        IntStream.range(0, players.size()).forEach(i -> {
            tilesMoved.put(players.get(i), 0);
        });

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

        int totalTilesMoved = tilesMoved.get(player);
        if (totalTilesMoved + steps > 52) {
            //TODO implement houe movement
        } else {
            int adjustedPosition = nextPosition > 56 ? nextPosition - 56 : nextPosition;

            playerTokenLayer.movePlayerThroughPath(player, nextPosition);

            PauseTransition finalPause = new PauseTransition(Duration.millis((steps + 1) * 300));
            finalPause.setOnFinished(event -> {
                gameController.movePlayer(player, adjustedPosition);
                sideColumn.turnOnButton();
            });
            finalPause.play();
        }

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

        Player currentPlayer = gameController.getCurrentPlayer();

        //if (currentPlayer.getPosition() + diceRoll >= boardSize) {
        //    moveBy(gameController.getCurrentPlayer(), boardSize - currentPlayer.getPosition(), buttonVisual);
//
        //    PauseTransition gameEndAnimation = new PauseTransition(Duration.millis((boardSize - currentPlayer.getPosition() + 1) * 300 + 300));
        //    gameEndAnimation.setOnFinished(event -> {
        //        SceneManager.getInstance().changeScene(
        //                new WinScreen(
        //                        currentPlayer.getName(), currentPlayer.getIcon()
        //                ).getScene());
//
        //    });
        //    gameEndAnimation.play();
//
        //} else {
            moveBy(gameController.getCurrentPlayer(), diceRoll, buttonVisual);
            gameController.advanceTurn();
        //}

    }

    /**
     * Instantly moves the token of a player to the given tile number. Called
     * during teleporting effects like ladders or snakes.
     *
     * @param player the player to move
     * @param tileNumber the destination tile
     */
    @Override
    public void moveToken(Player player, int tileNumber) {
        playerTokenLayer.moveToken(player, tileNumber);
    }
}
