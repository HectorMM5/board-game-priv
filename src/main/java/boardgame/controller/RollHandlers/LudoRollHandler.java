package boardgame.controller.RollHandlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import boardgame.controller.GameControllers.LudoGameController;
import boardgame.controller.SceneManager;
import boardgame.model.boardFiles.Player;
import boardgame.model.diceFiles.Dice;
import boardgame.utils.movementType;
import boardgame.visual.elements.SideColumn.DiceButtonVisual;
import boardgame.visual.elements.SideColumn.SideColumnVisual;
import boardgame.visual.gameLayers.LudoTokenLayer;
import boardgame.visual.scenes.WinScreen;
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
    private final LudoTokenLayer playerTokenLayer;
    private final SideColumnVisual sideColumn;
    private final Dice dice = new Dice(1);

    private final Map<Player, Integer> tilesMoved = new HashMap<>();

    public LudoRollHandler(LudoGameController gameController, LudoTokenLayer playerTokenLayer, SideColumnVisual sideColumn) {
        this.gameController = gameController;
        this.playerTokenLayer = playerTokenLayer;
        this.sideColumn = sideColumn;

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

            // Step 1: Animate the token
            playerTokenLayer.addToAnimationQueue(() -> {
                playerTokenLayer.movePlayerThroughHomePath(player, color, homePosition + steps, gameController);

                PauseTransition pause = new PauseTransition(Duration.millis((steps * 300) + 100));
                pause.setOnFinished(e -> {
                    applyHomeByMove(player, steps);
                    sideColumn.turnOnButton();

                });
                pause.play();

            });

        } else if (totalTilesMoved + steps > 53) {
            int stepsUntilReachedHome = 53 - totalTilesMoved;
            tilesMoved.replace(player, 53);

            gameController.movePlayer(player, startPosition + stepsUntilReachedHome, movementType.PATH);

            int remainingRoll = steps - stepsUntilReachedHome;

            playerTokenLayer.addToAnimationQueue(() -> {
                playerTokenLayer.movePlayerThroughHomePath(player, color, remainingRoll, gameController);

                PauseTransition homeStepPause = new PauseTransition(Duration.millis((remainingRoll + 1) * 300));
                homeStepPause.setOnFinished(z -> {
                    applyHomeMove(player, remainingRoll);
                    sideColumn.turnOnButton();
                });
                homeStepPause.play();
            });

        } else {
            gameController.movePlayer(player, nextPosition, movementType.PATH);

            tilesMoved.replace(player, totalTilesMoved + steps);

            playerTokenLayer.addToAnimationQueue(() -> {
                PauseTransition pause = new PauseTransition(Duration.millis(100));
                pause.setOnFinished(e -> {
                    sideColumn.turnOnButton();
                    playerTokenLayer.runNextAnimation();

                });
                pause.play();
            });

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

    public void applyHomeByMove(Player player, int steps) {
        gameController.movePlayerThroughHomeBy(player, steps);
    }

    public void applyHomeMove(Player player, int steps) {
        gameController.movePlayerThroughHome(player, steps);
    }

    public Map<Player, Integer> getTilesMoved() {
        return tilesMoved;
    }

}
