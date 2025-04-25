package boardgame.visual.scenes;

import java.util.ArrayList;
import java.util.List;

import boardgame.controller.GameController;
import boardgame.model.boardFiles.Board;
import boardgame.model.boardFiles.Player;
import boardgame.model.boardFiles.Tile;
import boardgame.model.diceFiles.Dice;
import boardgame.model.effectFiles.LadderEffect;
import boardgame.model.effectFiles.SnakeEffect;
import boardgame.utils.GameSetup;
import boardgame.visual.elements.BoardVisual;
import boardgame.visual.elements.DiceButtonVisual;
import boardgame.visual.elements.SideColumnVisual;
import boardgame.visual.gameLayers.PlayerTokenLayer;
import boardgame.visual.gameLayers.SnakesNLadders.LadderLayer;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The main scene handler for the in-game screen.
 * Responsible for initializing all UI elements (board, side column, tokens),
 * and managing gameplay flow such as player movement and dice rolling.
 *
 * This class connects the game's logic (`GameController`) with the visual layers.
 * 
 * @author Hector Mendana Morales
 */
public class Ingame {

    private final Board board;
    private final BoardVisual boardVisual;
    private final List<Player> players;
    private final SideColumnVisual sideColumn;
    private final PlayerTokenLayer playerTokenLayer;
    private final Dice dice = new Dice(1);
    GameController gameController;

    /**
     * Constructs an in-game scene based on the given game setup.
     *
     * @param gameSetup contains references to board, players, and controller
     */
    public Ingame(GameSetup gameSetup) {
        this.gameController = gameSetup.getGameController();
        this.board = gameSetup.getBoard();
        this.boardVisual = new BoardVisual(board);
        this.sideColumn = new SideColumnVisual(gameController, gameSetup.getPlayers(), this);
        this.playerTokenLayer = new PlayerTokenLayer(gameSetup.getPlayers());
        this.players = gameSetup.getPlayers();
    }

    /**
     * Builds and displays the game scene, initializing all layers and visuals.
     *
     * @param primaryStage the stage to display the scene in
     */
    public void createGameScene(Stage primaryStage) {
        gameController.setIngame(this);

        StackPane centerPane = new StackPane();
        centerPane.getChildren().add(boardVisual);

        ArrayList<Tile> tilesWithLadders = new ArrayList<>();
        for (Tile tile : board.getTiles()) {
            if (tile.getEffect() instanceof LadderEffect) {
                tilesWithLadders.add(tile);
            }
            if (tile.getEffect() != null) {
                System.out.println("EFFECT IN: " + tile.getNumber());
            }
        }

        ArrayList<Tile> tilesWithSnakes = new ArrayList<>();
        for (Tile tile : board.getTiles()) {
            if (tile.getEffect() instanceof SnakeEffect) {
                tilesWithSnakes.add(tile);
            }
        }

        BorderPane root = new BorderPane();
        root.setLeft(sideColumn);

        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Board Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        LadderLayer ladders = new LadderLayer(boardVisual, tilesWithLadders, tilesWithSnakes);
        centerPane.getChildren().add(ladders);
        centerPane.getChildren().add(playerTokenLayer);
        root.setCenter(centerPane);

        gameController.start();
        boardVisual.updateEntireBoard();
    }

    /**
     * Animates and completes a player's move by a number of steps.
     * Updates token layer, invokes game logic, and re-enables the roll button.
     *
     * @param player the player to move
     * @param steps the number of tiles to move
     * @param buttonVisual the roll button to be re-enabled after move
     */
    public void moveBy(Player player, int steps, DiceButtonVisual buttonVisual) {
        int nextPosition = player.getPosition() + steps;

        playerTokenLayer.movePlayerThroughPath(player, nextPosition);
        PauseTransition finalPause = new PauseTransition(Duration.millis((nextPosition - player.getPosition() + 1) * 200));
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
    public void handleRollDice(DiceButtonVisual buttonVisual) {
        int diceRoll = dice.roll();
        System.out.println("Rolled: " + diceRoll);
        moveBy(gameController.getCurrentPlayer(), diceRoll, buttonVisual);
        sideColumn.displayRoll(diceRoll);
        gameController.advanceTurn();
    }

    /**
     * Instantly moves the token of a player to the given tile number.
     * Called during teleporting effects like ladders or snakes.
     *
     * @param player the player to move
     * @param tileNumber the destination tile
     */
    public void moveToken(Player player, int tileNumber) {
        playerTokenLayer.moveToken(player, tileNumber);
    }
}
