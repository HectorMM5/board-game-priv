package boardgame.controller.RollHandlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import boardgame.controller.GameControllers.LudoGameController;
import boardgame.controller.SceneManager;
import boardgame.model.Player;
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

    /**
     * Constructs a LudoRollHandler to manage rolling logic and animations.
     *
     * @param gameController the controller handling Ludo game logic
     * @param playerTokenLayer the token animation layer
     * @param sideColumn the UI column for status display
     */
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
     * Animates and completes a player's move by a number of steps.
     *
     * @param player the player to move
     * @param steps the number of tiles to move
     * @param buttonVisual the button to re-enable after animation
     */
    @Override
    public void moveBy(Player player, int steps, DiceButtonVisual buttonVisual) {
        int startPosition = player.getPosition();
        int nextPosition = startPosition + steps;
        Color color = gameController.getPlayerColor().get(player);
        int totalTilesMoved = tilesMoved.get(player);

        if (totalTilesMoved == 53) {
            int homePosition = gameController.getHomePosition().get(player);
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
     * Handles the dice roll, moves the player and checks for game end.
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

            PauseTransition gameEndAnimation = new PauseTransition(Duration.millis(toGoal * 300 + 300));
            gameEndAnimation.setOnFinished(event -> {
                playerTokenLayer.moveToGoal(currentPlayer);
                PauseTransition switchScreenPause = new PauseTransition(Duration.millis(600));
                switchScreenPause.setOnFinished(e -> {
                    SceneManager.getInstance().changeScene(
                        new WinScreen(currentPlayer.getName(), currentPlayer.getIcon()).getScene()
                    );
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
     * Moves player forward through home tiles by a number of steps.
     *
     * @param player the player to move
     * @param steps the number of tiles to move
     */
    public void applyHomeByMove(Player player, int steps) {
        gameController.movePlayerThroughHomeBy(player, steps);
    }

    /**
     * Moves player directly to a specific home tile.
     *
     * @param player the player to move
     * @param steps how far into home area the player should be placed
     */
    public void applyHomeMove(Player player, int steps) {
        gameController.movePlayerThroughHome(player, steps);
    }

    /**
     * Returns the internal map tracking how far each player has moved.
     *
     * @return a map of players and their movement progress
     */
    public Map<Player, Integer> getTilesMoved() {
        return tilesMoved;
    }
}
