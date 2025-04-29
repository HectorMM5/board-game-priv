package boardgame.visual.elements.Menu;

import java.util.List;

import boardgame.controller.GameInitController;
import boardgame.model.boardFiles.Player;
import boardgame.model.boardFiles.SnL.SnLBoard;
import boardgame.utils.BoardJSON;
import boardgame.visual.elements.SnL.LadderLayer;
import boardgame.visual.elements.SnL.SnLBoardVisual;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameInitVisual {

    // --- Main layout containers ---
    private final VBox playerRowsContainer;
    private final HBox boardChoices = new HBox();
    private final StackPane boardChoiceHolder = new StackPane();
    private final HBox sceneWrapper = new HBox(25);

    // --- Control buttons ---
    private final Button addPlayerButton = new Button("Add Player");
    private final Button startGameButton = new Button("Start Game");

    public GameInitVisual() {
        // Initialize player column
        this.playerRowsContainer = new VBox(10);
        playerRowsContainer.setStyle("-fx-padding: 20;");

        loadBoard(0);

    }

    public Scene getScene(String chosenGame) {
        // --- Setup initial UI elements ---

        addEmptyPlayerRow(); // Always show the first row by default

        Label titleLabel;
        Label subtitleLabel = new Label("Create your players!");

        // Game-specific title
        switch (chosenGame) {
            case "Snakes & Ladders" ->
                titleLabel = new Label("Snakes & Ladders");
            case "Ludo" ->
                titleLabel = new Label("Ludo");
            default ->
                throw new AssertionError();
        }

        // Title styling
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: #333; -fx-font-weight: bold;");
        subtitleLabel.setAlignment(Pos.CENTER);
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
        VBox.setMargin(subtitleLabel, new Insets(0, 0, 50, 0));

        // --- Setup buttons ---
        // Add player
        addPlayerButton.setOnAction(e -> addEmptyPlayerRow());

        // Start game
        startGameButton.setOnAction(e -> {
            // Check that all player rows have non-blank names
            boolean allNamesValid = playerRowsContainer.getChildren().stream()
                .filter(node -> node instanceof PlayerCreationRow)
                .map(node -> ((PlayerCreationRow) node).getNameField().getText())
                .allMatch(name -> !name.isBlank());
        
            if (!allNamesValid) {
                new PopUpAlert("Player names cannot be empty.").show();
                return;
            }
        
            // If all names are valid, build the player list
            List<Player> players = playerRowsContainer.getChildren().stream()
                .filter(node -> node instanceof PlayerCreationRow)
                .map(node -> {
                    PlayerCreationRow row = (PlayerCreationRow) node;
                    return new Player(row.getSelectedIconName(), row.getNameField().getText());
                })
                .toList();
        
            GameInitController.handleGameStart(chosenGame, 0, players);
        });

        // --- Board selection buttons ---
        Button board1Button = new Button("Board 1");
        board1Button.setOnAction(e -> loadBoard(0));

        Button board2Button = new Button("Board 2");
        board2Button.setOnAction(e -> loadBoard(1));

        Button board3Button = new Button("Board 3");
        board3Button.setOnAction(e -> loadBoard(2));

        boardChoices.getChildren().addAll(board1Button, board2Button, board3Button);
        boardChoices.setAlignment(Pos.CENTER);

        // --- Build left-hand side (board) ---
        VBox boardSide = new VBox();
        boardSide.setAlignment(Pos.CENTER);
        boardSide.getChildren().add(boardChoiceHolder);

        // --- Build right-hand side (controls) ---
        VBox sideColumn = new VBox(20);
        sideColumn.getChildren().addAll(titleLabel, subtitleLabel, boardChoices, playerRowsContainer, addPlayerButton, startGameButton);
        sideColumn.setPadding(new Insets(20));
        sideColumn.setPrefWidth(400);
        sideColumn.setAlignment(Pos.TOP_CENTER);

        // --- Layout rules ---
        HBox.setHgrow(boardSide, Priority.NEVER);
        HBox.setHgrow(sideColumn, Priority.ALWAYS);

        // --- Final scene assembly ---
        sceneWrapper.setAlignment(Pos.CENTER_LEFT);
        sceneWrapper.getChildren().addAll(boardSide, sideColumn);

        return new Scene(sceneWrapper);
    }

    /**
     * Adds a new empty player input row.
     */
    public void addEmptyPlayerRow() {
        if (playerRowsContainer.getChildren().size() == 3) {
            addPlayerButton.setDisable(true);
        }

        PlayerCreationRow row = new PlayerCreationRow();
        row.setDeleteRowAction((() -> {
            playerRowsContainer.getChildren().remove(row);
            if (playerRowsContainer.getChildren().size() < 3) {
                addPlayerButton.setDisable(false);
            }
        }));

        playerRowsContainer.getChildren().add(row);
    }

    /**
     * Loads a board into the left panel based on board variant index.
     */
    private void loadBoard(int boardIndex) {
        SnLBoard board = BoardJSON.constructSnLBoardFromJSON(boardIndex);
        SnLBoardVisual boardVisual = new SnLBoardVisual(board);
        LadderLayer ladderLayer = new LadderLayer(boardVisual, board.getTilesWithLadders(), board.getTilesWithSnakes());

        boardChoiceHolder.getChildren().clear();
        boardChoiceHolder.getChildren().addAll(boardVisual, ladderLayer);
    }
}