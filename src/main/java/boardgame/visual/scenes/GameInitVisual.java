package boardgame.visual.scenes;

import java.util.List;

import boardgame.controller.GameInitController;
import boardgame.model.Player;
import boardgame.model.boardFiles.LudoBoard;
import boardgame.model.boardFiles.SnLBoard;
import boardgame.utils.GameType;
import boardgame.utils.JSON.BoardJSON;
import boardgame.utils.ScreenDimension;
import boardgame.visual.elements.BackButton;
import boardgame.visual.elements.LudoBoardVisual;
import boardgame.visual.elements.Menu.PlayerCreationRow;
import boardgame.visual.elements.Menu.PopUpAlert;
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

/**
 * Visual interface for the game initialization screen, allowing users to select
 * the game, board (if applicable), and create players.
 */
public class GameInitVisual {

    private int chosenBoard;
    private GameType chosenGame;

    private final VBox playerRowsContainer;
    private final HBox boardChoices = new HBox(25);
    private final StackPane boardChoiceHolder = new StackPane();
    private final HBox sceneWrapper = new HBox(25);

    private final Button addPlayerButton = new Button("Add Player");
    private final Button startGameButton = new Button("Start Game");
    private final BackButton backButton = new BackButton(false);

    /**
     * Constructs a new {@code GameInitVisual}.
     */
    public GameInitVisual() {
        this.chosenBoard = 0;
        this.playerRowsContainer = new VBox(10);
        playerRowsContainer.setStyle("-fx-padding: 20;");
    }

    /**
     * Creates and returns the scene for game initialization based on the chosen
     * game.
     *
     * @param chosenGame the type of game to initialize.
     * @return the game initialization scene.
     */
    public Scene getScene(GameType chosenGame) {
        this.chosenGame = chosenGame;
        addEmptyPlayerRow();

        Label titleLabel;
        Label subtitleLabel = new Label("Create your players!");

        switch (chosenGame) {
            case SnakesNLadders ->
                titleLabel = new Label("Snakes & Ladders");
            case Ludo ->
                titleLabel = new Label("Ludo");
            default ->
                throw new AssertionError("Unknown game: " + chosenGame);
        }

        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: #333; -fx-font-weight: bold;");

        subtitleLabel.setAlignment(Pos.CENTER);
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
        VBox.setMargin(subtitleLabel, new Insets(0, 0, 25, 0));

        addPlayerButton.setOnAction(e -> addEmptyPlayerRow());
        addPlayerButton.getStyleClass().add("button-common");

        startGameButton.setOnAction(e -> {

            if (playerRowsContainer.getChildren().size() < 1) {
                new PopUpAlert("At least 1 player is required.").show();
                return;
            }

            boolean allNamesValid = playerRowsContainer.getChildren().stream()
                    .filter(node -> node instanceof PlayerCreationRow)
                    .map(node -> ((PlayerCreationRow) node).getNameField().getText().trim())
                    .allMatch(name -> !name.isBlank());

            if (!allNamesValid) {
                new PopUpAlert("Player names cannot be empty.").show();
                return;
            }

            List<Player> players = playerRowsContainer.getChildren().stream()
                    .filter(node -> node instanceof PlayerCreationRow)
                    .map(node -> {
                        PlayerCreationRow row = (PlayerCreationRow) node;
                        return new Player(row.getSelectedIconName(), row.getNameField().getText());
                    })
                    .toList();

            GameInitController.handleGameStart(chosenGame, chosenBoard, players);
        });

        startGameButton.getStyleClass().add("button-common");

        // Board preset buttons for SnL only
        if (chosenGame.equals(GameType.SnakesNLadders)) {
            boardChoices.getChildren().clear();
            Button board1Button = new Button("Board 1");
            board1Button.setOnAction(e -> loadBoard(0));
            board1Button.getStyleClass().add("button-common");

            Button board2Button = new Button("Board 2");
            board2Button.setOnAction(e -> loadBoard(1));
            board2Button.getStyleClass().add("button-common");

            Button board3Button = new Button("Board 3");
            board3Button.setOnAction(e -> loadBoard(2));
            board3Button.getStyleClass().add("button-common");

            boardChoices.getChildren().addAll(board1Button, board2Button, board3Button);

        } else {
            boardChoices.getChildren().clear(); // Don't show board choices for Ludo
        }

        boardChoices.setAlignment(Pos.CENTER);

        VBox boardSide = new VBox();
        boardSide.setAlignment(Pos.CENTER);
        boardSide.getChildren().add(boardChoiceHolder);

        VBox sideColumn = new VBox(20);
        sideColumn.getChildren().addAll(backButton, titleLabel, subtitleLabel);

        if (chosenGame.equals(GameType.SnakesNLadders)) {
            sideColumn.getChildren().add(boardChoices);
        }

        sideColumn.getChildren().addAll(playerRowsContainer, addPlayerButton, startGameButton);
        sideColumn.setPadding(new Insets(20));
        sideColumn.setPrefWidth(ScreenDimension.getScreenWidth() * 0.3);
        sideColumn.setAlignment(Pos.TOP_CENTER);

        HBox.setHgrow(boardSide, Priority.NEVER);
        HBox.setHgrow(sideColumn, Priority.ALWAYS);

        sceneWrapper.setAlignment(Pos.CENTER_LEFT);
        sceneWrapper.getChildren().addAll(boardSide, sideColumn);

        loadBoard(0); // Load default board after chosenGame is set

        return new Scene(sceneWrapper);
    }

    /**
     * Adds an empty player creation row to the UI. Limits the number of players
     * based on the chosen game.
     */
    public void addEmptyPlayerRow() {
        switch (chosenGame) {
            case SnakesNLadders -> {
                if (playerRowsContainer.getChildren().size() == 4) {
                    addPlayerButton.setDisable(true);
                }
            }

            case Ludo -> {
                if (playerRowsContainer.getChildren().size() == 3) { // Ludo supports up to 4 players
                    addPlayerButton.setDisable(true);
                }
            }
        }

        PlayerCreationRow row = new PlayerCreationRow();
        row.setDeleteRowAction(() -> {
            playerRowsContainer.getChildren().remove(row);
            if (playerRowsContainer.getChildren().size() < (chosenGame == GameType.SnakesNLadders ? 5 : 4)) {
                addPlayerButton.setDisable(false);
            }
        });

        playerRowsContainer.getChildren().add(row);
    }

    /**
     * Loads the visual representation of the selected board.
     *
     * @param boardIndex the index of the board to load (for games with multiple
     * board presets).
     */
    private void loadBoard(int boardIndex) {
        this.chosenBoard = boardIndex;
        switch (chosenGame) {
            case SnakesNLadders ->
                handleSnL(boardIndex);
            case Ludo ->
                handleLudo();
        }
    }

    /**
     * Handles the loading of a Snakes and Ladders board based on the index.
     *
     * @param boardIndex the index of the Snakes and Ladders board to load.
     */
    private void handleSnL(int boardIndex) {
        SnLBoard board = BoardJSON.constructSnLBoardFromJSON(boardIndex);
        SnLBoardVisual boardVisual = new SnLBoardVisual(board);
        LadderLayer ladderLayer = new LadderLayer(boardVisual, board.getTilesWithLadders(), board.getTilesWithSnakes());

        boardChoiceHolder.getChildren().clear();
        boardChoiceHolder.getChildren().addAll(boardVisual, ladderLayer);
    }

    /**
     * Handles the loading of the Ludo board.
     */
    private void handleLudo() {
        LudoBoard ludoBoard = new LudoBoard();
        LudoBoardVisual ludoBoardVisual = new LudoBoardVisual(ludoBoard);

        boardChoiceHolder.getChildren().clear();
        boardChoiceHolder.getChildren().add(ludoBoardVisual);
    }
}
