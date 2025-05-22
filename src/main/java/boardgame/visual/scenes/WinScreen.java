package boardgame.visual.scenes;

import boardgame.controller.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Represents the screen displayed when a player wins the game.
 */
public class WinScreen {
    private final String winnerName;
    private final String winnerIconPath;

    /**
     * Constructs a new {@code WinScreen}.
     *
     * @param winnerName     the name of the winning player.
     * @param winnerIconPath the file path to the winner's icon.
     */
    public WinScreen(String winnerName, String winnerIconPath) {
        this.winnerName = winnerName;
        this.winnerIconPath = winnerIconPath;
    }

    /**
     * Gets the name of the winner.
     *
     * @return the winner's name.
     */
    public String getWinnerName() {
        return winnerName;
    }

    /**
     * Gets the file path to the winner's icon.
     *
     * @return the winner's icon file path.
     */
    public String getWinnerIconPath() {
        return winnerIconPath;
    }

    /**
     * Builds the winning scene displaying the winner's name and icon.
     *
     * @return the winner screen Scene
     */
    public Scene getScene() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);

        Label congratsLabel = new Label("Congratulations!");
        congratsLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: #333; -fx-font-weight: bold;");

        Label winnerLabel = new Label(winnerName + " wins!");
        winnerLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #555;");

        ImageView winnerImage = new ImageView(new Image(winnerIconPath));
        winnerImage.setFitWidth(150);
        winnerImage.setFitHeight(150);

        Button mainMenuButton = new Button("Return to Main Menu");
        mainMenuButton.setOnAction(e -> {
            SceneManager.getInstance().changeScene(StartScreenView.getScene());
        });
        mainMenuButton.getStyleClass().add("button-common");

        root.getChildren().addAll(congratsLabel, winnerImage, winnerLabel, mainMenuButton);

        return new Scene(root, 600, 400);
    }


}