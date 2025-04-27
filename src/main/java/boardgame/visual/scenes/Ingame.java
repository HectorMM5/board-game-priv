package boardgame.visual.scenes;

import boardgame.controller.GameController;
import boardgame.model.boardFiles.Board;
import boardgame.utils.GameSetup;
import boardgame.visual.elements.BoardVisual;
import boardgame.visual.elements.SideColumnVisual;
import boardgame.visual.gameLayers.PlayerTokenLayer;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * The main scene handler for the in-game screen.
 * Responsible for initializing all UI elements (board, side column, tokens),
 * and managing gameplay flow such as player movement and dice rolling.
 *
 * This class connects the game's logic (GameController) with the visual layers.
 * Now uses IngameController to separate gameplay logic from scene setup.
 *
 * @author Hector Mendana Morales
 */
public class Ingame {

    private final Board board;
    private final BoardVisual boardVisual;
    private final SideColumnVisual sideColumn;
    private final PlayerTokenLayer playerTokenLayer;
    private final IngameController ingameController;
    private final GameController gameController;

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
        this.ingameController = new IngameController(gameController, playerTokenLayer, sideColumn);
    }

    /**
     * Builds and displays the game scene, initializing all layers and visuals.
     *
     * @return the scene containing the in-game UI
     */
    public Scene getScene() {
        gameController.setIngame(this);

        StackPane centerPane = new StackPane();
        centerPane.getChildren().add(boardVisual);

        BorderPane root = new BorderPane();
        root.setLeft(sideColumn);

        Scene scene = new Scene(root, 600, 600);

        centerPane.getChildren().add(playerTokenLayer);
        root.setCenter(centerPane);

        gameController.start();
        boardVisual.updateEntireBoard();

        return scene;
    }

    /**
     * Returns the ingame controller responsible for gameplay actions.
     *
     * @return the IngameController instance
     */
    public IngameController getIngameController() {
        return ingameController;
    }
}
