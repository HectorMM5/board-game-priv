package boardgame.visual.scenes;

import boardgame.controller.GameController;
import boardgame.model.boardFiles.SnLBoard;
import boardgame.utils.GameSetup;
import boardgame.visual.elements.BoardVisual;
import boardgame.visual.elements.SideColumnVisual;
import boardgame.visual.gameLayers.PlayerTokenLayer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


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

    private final SnLBoard board;
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
        this.playerTokenLayer = new PlayerTokenLayer(boardVisual, gameSetup.getPlayers());
        this.ingameController = new IngameController(gameController, playerTokenLayer, sideColumn);
        
    }

    /**
     * Builds and displays the game scene, initializing all layers and visuals.
     *
     * @return the scene containing the in-game UI
     */
    public Scene getScene() {
    gameController.setIngame(this);

    // Main wrapper HBox
    HBox sceneWrapper = new HBox(25);

    // --- Left side: Board visuals ---
    StackPane boardPane = new StackPane();
    boardPane.getChildren().addAll(boardVisual, playerTokenLayer);
    boardPane.setAlignment(Pos.CENTER);

    playerTokenLayer.prefWidthProperty().bind(boardVisual.widthProperty());
    playerTokenLayer.prefHeightProperty().bind(boardVisual.heightProperty());
    playerTokenLayer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

    // NEW: Wrap boardPane in a VBox to center it properly
    VBox boardContainer = new VBox(boardPane);
    boardContainer.setAlignment(Pos.CENTER);
    HBox.setHgrow(boardContainer, Priority.ALWAYS); // Important to make it take up space

    // --- Right side: Side column ---
    sideColumn.setAlignment(Pos.CENTER);

    // Assemble
    sceneWrapper.getChildren().addAll(boardContainer, sideColumn);

    Scene scene = new Scene(sceneWrapper);

    gameController.start();

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
