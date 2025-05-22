package boardgame.visual.scenes;

import boardgame.controller.SceneManager;
import boardgame.utils.GameType;
import boardgame.utils.PlayerCSV;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * The initial screen of the application, allowing users to select which game
 * they want to play.
 */
public class StartScreenView {

    /**
     * Creates and returns the scene for the start screen.
     *
     * @return the start screen scene.
     */
    public static Scene getScene() {
        VBox root = new VBox();
        root.setSpacing(30);
        root.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("BOARD-GAME");
        Label subtitleLabel = new Label("Select your game:");

        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setStyle("-fx-font-size: 80px; -fx-text-fill: #333; -fx-font-weight: bold;");
        subtitleLabel.setAlignment(Pos.CENTER);
        subtitleLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: #555;");
        VBox.setMargin(subtitleLabel, new Insets(25, 0, 25, 0));

        GridPane menuPane = new GridPane();
        menuPane.setHgap(50);
        root.getChildren().addAll(titleLabel, subtitleLabel, menuPane);
        menuPane.setAlignment(Pos.CENTER);

        GameInitVisual gameInitVisual = new GameInitVisual();
        SceneManager sceneManager = SceneManager.getInstance();

        Button SnLButton = new Button("Snakes & Ladders");
        SnLButton.setOnAction(e -> {
            sceneManager.changeScene(gameInitVisual.getScene(GameType.SnakesNLadders));
        });
        SnLButton.getStyleClass().add("button-common");

        Button LudoButton = new Button("Ludo");
        LudoButton.setOnAction(e -> {
            sceneManager.changeScene(gameInitVisual.getScene(GameType.Ludo));
        });
        LudoButton.getStyleClass().add("button-common");

        menuPane.add(SnLButton, 0, 0);
        menuPane.add(LudoButton, 1, 0);

        Button importCSVButton = new Button("Import CSV");
        importCSVButton.setOnAction(e -> {
            PlayerCSV.instance().handleImport(SceneManager.getPrimaryStage());
        });
        importCSVButton.getStyleClass().add("button-common");

        root.getChildren().add(importCSVButton);

        return new Scene(root);

    }

}