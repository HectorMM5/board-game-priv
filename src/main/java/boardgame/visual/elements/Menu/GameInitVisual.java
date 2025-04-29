package boardgame.visual.elements.Menu;

import java.util.List;

import boardgame.controller.GameInitController;
import boardgame.model.boardFiles.Ludo.LudoBoard;
import boardgame.model.boardFiles.Player;
import boardgame.model.boardFiles.SnL.SnLBoard;
import boardgame.utils.BoardJSON;
import boardgame.visual.elements.LudoBoardVisual;
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

    private int chosenBoard;
    private String chosenGame;

    private final VBox playerRowsContainer;
    private final HBox boardChoices = new HBox();
    private final StackPane boardChoiceHolder = new StackPane();
    private final HBox sceneWrapper = new HBox(25);

    private final Button addPlayerButton = new Button("Add Player");
    private final Button startGameButton = new Button("Start Game");

    public GameInitVisual() {
        this.chosenBoard = 0;
        this.playerRowsContainer = new VBox(10);
        playerRowsContainer.setStyle("-fx-padding: 20;");
    }

    public Scene getScene(String chosenGame) {
        this.chosenGame = chosenGame;
        addEmptyPlayerRow();

        Label titleLabel;
        Label subtitleLabel = new Label("Create your players!");

        switch (chosenGame) {
            case "Snakes & Ladders" -> titleLabel = new Label("Snakes & Ladders");
            case "Ludo" -> titleLabel = new Label("Ludo");
            default -> throw new AssertionError("Unknown game: " + chosenGame);
        }

        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: #333; -fx-font-weight: bold;");
        subtitleLabel.setAlignment(Pos.CENTER);
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
        VBox.setMargin(subtitleLabel, new Insets(0, 0, 50, 0));

        addPlayerButton.setOnAction(e -> addEmptyPlayerRow());

        startGameButton.setOnAction(e -> {
            boolean allNamesValid = playerRowsContainer.getChildren().stream()
                .filter(node -> node instanceof PlayerCreationRow)
                .map(node -> ((PlayerCreationRow) node).getNameField().getText())
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

        // Board preset buttons for SnL only
        if (chosenGame.equals("Snakes & Ladders")) {
            boardChoices.getChildren().clear();
            Button board1Button = new Button("Board 1");
            board1Button.setOnAction(e -> loadBoard(0));

            Button board2Button = new Button("Board 2");
            board2Button.setOnAction(e -> loadBoard(1));

            Button board3Button = new Button("Board 3");
            board3Button.setOnAction(e -> loadBoard(2));

            boardChoices.getChildren().addAll(board1Button, board2Button, board3Button);
            boardChoices.setAlignment(Pos.CENTER);
        } else {
            boardChoices.getChildren().clear(); // Don't show board choices for Ludo
        }

        VBox boardSide = new VBox();
        boardSide.setAlignment(Pos.CENTER);
        boardSide.getChildren().add(boardChoiceHolder);

        VBox sideColumn = new VBox(20);
        sideColumn.getChildren().addAll(titleLabel, subtitleLabel);

        if (chosenGame.equals("Snakes & Ladders")) {
            sideColumn.getChildren().add(boardChoices);
        }

        sideColumn.getChildren().addAll(playerRowsContainer, addPlayerButton, startGameButton);
        sideColumn.setPadding(new Insets(20));
        sideColumn.setPrefWidth(400);
        sideColumn.setAlignment(Pos.TOP_CENTER);

        HBox.setHgrow(boardSide, Priority.NEVER);
        HBox.setHgrow(sideColumn, Priority.ALWAYS);

        sceneWrapper.setAlignment(Pos.CENTER_LEFT);
        sceneWrapper.getChildren().addAll(boardSide, sideColumn);

        loadBoard(0); // Load default board after chosenGame is set

        return new Scene(sceneWrapper);
    }

    public void addEmptyPlayerRow() {
        if (playerRowsContainer.getChildren().size() == 3) {
            addPlayerButton.setDisable(true);
        }

        PlayerCreationRow row = new PlayerCreationRow();
        row.setDeleteRowAction(() -> {
            playerRowsContainer.getChildren().remove(row);
            if (playerRowsContainer.getChildren().size() < 3) {
                addPlayerButton.setDisable(false);
            }
        });

        playerRowsContainer.getChildren().add(row);
    }

    private void loadBoard(int boardIndex) {
        this.chosenBoard = boardIndex;
        switch (chosenGame) {
            case "Snakes & Ladders" -> handleSnL(boardIndex);
            case "Ludo" -> handleLudo();
        }
    }

    private void handleSnL(int boardIndex) {
        SnLBoard board = BoardJSON.constructSnLBoardFromJSON(boardIndex);
        SnLBoardVisual boardVisual = new SnLBoardVisual(board);
        LadderLayer ladderLayer = new LadderLayer(boardVisual, board.getTilesWithLadders(), board.getTilesWithSnakes());

        boardChoiceHolder.getChildren().clear();
        boardChoiceHolder.getChildren().addAll(boardVisual, ladderLayer);
    }

    private void handleLudo() {
        LudoBoardVisual ludoBoardVisual = new LudoBoardVisual(new LudoBoard());
        boardChoiceHolder.getChildren().clear();
        boardChoiceHolder.getChildren().add(ludoBoardVisual);
    }
}
