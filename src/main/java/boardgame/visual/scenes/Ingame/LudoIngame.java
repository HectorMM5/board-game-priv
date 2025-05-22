package boardgame.visual.scenes.Ingame;

import boardgame.controller.GameControllers.LudoGameController;
import boardgame.controller.RollHandlers.LudoRollHandler;
import boardgame.model.boardFiles.LudoBoard;
import boardgame.utils.GameFactory;
import boardgame.visual.elements.BackButton;
import boardgame.visual.elements.LudoBoardVisual;
import boardgame.visual.elements.SideColumn.SideColumnVisual;
import boardgame.visual.gameLayers.LudoTokenLayer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The main scene handler for the Ludo in-game screen. Responsible for initializing
 * all UI elements (board, side column, tokens), and managing gameplay flow such
 * as player movement and dice rolling.
 * <p>
 * This class connects the game's logic (LudoGameController) with the visual layers.
 *
 * @author Hector Mendana Morales
 */
public class LudoIngame implements Ingame {

    public final LudoBoard board;
    public final LudoBoardVisual boardVisual;
    public final LudoRollHandler rollHandler;
    public final LudoGameController gameController;
    public final SideColumnVisual sideColumn;
    public final LudoTokenLayer playerTokenLayer;

    private final BackButton backButton = new BackButton(true);

    /**
     * Constructs an in-game scene for Ludo based on the given game setup.
     *
     * @param gameSetup contains references to board, players, and controller
     */
    public LudoIngame(GameFactory gameSetup) {
        this.gameController = (LudoGameController) gameSetup.getGameController();
        this.board = (LudoBoard) gameSetup.getBoard();
        this.boardVisual = new LudoBoardVisual(board);
        this.sideColumn = new SideColumnVisual(gameController, gameSetup.getPlayers(), this);
        this.playerTokenLayer = new LudoTokenLayer(boardVisual, gameSetup.getPlayers());

        gameSetup.getPlayers().forEach(p -> p.addObserver(playerTokenLayer));

        this.rollHandler = new LudoRollHandler((LudoGameController) gameController, playerTokenLayer, sideColumn);

    }

    /**
     * Builds and displays the Ludo game scene, initializing all layers and visuals.
     *
     * @return the scene containing the in-game UI
     */
    @Override
    public Scene getScene() {

        StackPane sceneStacker = new StackPane();

        gameController.setIngame(this);

        //ChatGPT

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
     * Returns the Ludo roll handler associated with this scene.
     *
     * @return the Ludo roll handler.
     */
    @Override
    public LudoRollHandler getRollHandler() {
        return rollHandler;
    }

}