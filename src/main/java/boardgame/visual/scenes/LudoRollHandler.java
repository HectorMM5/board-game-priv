package boardgame.visual.scenes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import boardgame.controller.LudoGameController;
import boardgame.controller.SceneManager;
import boardgame.model.boardFiles.Player;
import boardgame.model.diceFiles.Dice;
import boardgame.visual.elements.SideColumn.DiceButtonVisual;
import boardgame.visual.elements.SideColumn.SideColumnVisual;
import boardgame.visual.gameLayers.PlayerTokenLayer;
import javafx.animation.PauseTransition;
import javafx.scene.paint.Color;
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
        Color color = gameController.getPlayerColor().get(player);
        int totalTilesMoved = tilesMoved.get(player);

        if (totalTilesMoved == 53) {
            int homePosition = gameController.getHomePosition().get(player);

            playerTokenLayer.movePlayerThroughHomePath(player, color, homePosition + steps, gameController);

            PauseTransition pause = new PauseTransition(Duration.millis((steps + 1) * 300));
            pause.setOnFinished(e -> {
                gameController.movePlayerThroughHomeBy(player, steps);
                sideColumn.turnOnButton();
            });
            pause.play();

        } else if (totalTilesMoved + steps > 53) {
            int stepsUntilReachedHome = 53 - totalTilesMoved;
            playerTokenLayer.movePlayerThroughPath(player, startPosition + stepsUntilReachedHome);

            tilesMoved.replace(player, 53);

            PauseTransition pause = new PauseTransition(Duration.millis((stepsUntilReachedHome + 1) * 300));
            pause.setOnFinished(e -> {
                gameController.disablePlayerOnBoard(player);

                int remainingRoll = steps - stepsUntilReachedHome;

                playerTokenLayer.movePlayerThroughHomePath(player, color, remainingRoll, gameController);

                PauseTransition homeStepPause = new PauseTransition(Duration.millis((remainingRoll + 1) * 300));
                homeStepPause.setOnFinished(z -> {
                    gameController.movePlayerThroughHome(player, remainingRoll);
                    sideColumn.turnOnButton();
                });
                homeStepPause.play();

            });
            pause.play();

        } else {
            int adjustedNextPosition = nextPosition > 56 ? nextPosition - 56 : nextPosition;

            playerTokenLayer.movePlayerThroughPath(player, nextPosition);
            tilesMoved.replace(player, totalTilesMoved + steps);

            PauseTransition pause = new PauseTransition(Duration.millis((steps + 1) * 300));
            pause.setOnFinished(e -> {
                gameController.movePlayer(player, adjustedNextPosition);
                sideColumn.turnOnButton();
            });

            pause.play();
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
        int homePosition = gameController.getHomePosition().get(currentPlayer);

        System.out.println("HOME POSITION: " + homePosition);

        if (homePosition + diceRoll > 6) {
            int toGoal = 6 - homePosition;
            moveBy(gameController.getCurrentPlayer(), toGoal, buttonVisual);

            System.out.println("REACHED END");

            PauseTransition gameEndAnimation = new PauseTransition(Duration.millis(toGoal * 300 + 300));
            gameEndAnimation.setOnFinished(event -> {
                playerTokenLayer.moveToGoal(currentPlayer);

                PauseTransition switchScreenPause = new PauseTransition(Duration.millis(600));
                switchScreenPause.setOnFinished(e -> {
                SceneManager.getInstance().changeScene(
                        new WinScreen(
                                currentPlayer.getName(), currentPlayer.getIcon()
                        ).getScene());
                });

                switchScreenPause.play();

            });
            gameEndAnimation.play();

        } else {
            moveBy(gameController.getCurrentPlayer(), diceRoll, buttonVisual);
            gameController.advanceTurn();
        }

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
