package boardgame.visual.scenes.Ingame;

import boardgame.controller.GameControllers.SnLGameController;
import boardgame.controller.RollHandlers.SnLRollHandler;
import boardgame.model.boardFiles.SnLBoard;
import boardgame.utils.GameFactory;
import boardgame.visual.elements.BackButton;
import boardgame.visual.elements.SideColumn.SideColumnVisual;
import boardgame.visual.elements.SnL.SnLBoardVisual;
import boardgame.visual.gameLayers.SnLTokenLayer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The main scene handler for the Snakes and Ladders in-game screen. Responsible
 * for initializing all UI elements (board, side column, tokens), and managing
 * gameplay flow such as player movement and dice rolling.
 * <p>
 * This class connects the game's logic (SnLGameController) with the visual layers.
 *
 * @author Hector Mendana Morales
 */
public class SnLIngame implements Ingame {

    public final SnLBoard board;
    public final SnLBoardVisual boardVisual;
    public final SnLRollHandler rollHandler;
    public final SnLGameController gameController;
    public final SideColumnVisual sideColumn;
    public final SnLTokenLayer playerTokenLayer;

    private final BackButton backButton = new BackButton(true);

    /**
     * Constructs an in-game scene for Snakes and Ladders based on the given game setup.
     *
     * @param gameSetup contains references to board, players, and controller
     */
    public SnLIngame(GameFactory gameSetup) {
        this.gameController = (SnLGameController) gameSetup.getGameController();
        this.board = (SnLBoard) gameSetup.getBoard();
        this.boardVisual = new SnLBoardVisual(board);
        this.sideColumn = new SideColumnVisual(gameController, gameSetup.getPlayers(), this);
        this.playerTokenLayer = new SnLTokenLayer(boardVisual, gameSetup.getPlayers());

        gameSetup.getPlayers().forEach(p -> p.addObserver(playerTokenLayer));

        this.rollHandler = new SnLRollHandler((SnLGameController) gameController, playerTokenLayer, sideColumn);

    }

    /**
     * Builds and displays the Snakes and Ladders game scene, initializing all layers and visuals.
     *
     * @return the scene containing the in-game UI
     */
    @Override
    public Scene getScene() {
        StackPane sceneStacker = new StackPane();
        
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

        // Align the back button to the top-left within the StackPane
        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        sceneStacker.getChildren().addAll(sceneWrapper, backButton);

        Scene scene = new Scene(sceneStacker);

        gameController.start();

        return scene;
    }

    /**
     * Returns the Snakes and Ladders roll handler associated with this scene.
     *
     * @return the Snakes and Ladders roll handler.
     */
    @Override
    public SnLRollHandler getRollHandler() {
        return rollHandler;
    }

}