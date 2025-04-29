package boardgame.visual.scenes;

import boardgame.controller.SceneManager;
import boardgame.visual.elements.Menu.GameInitVisual;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class WinScreen {
    private final String winnerName;
    private final String winnerIconPath;

    public WinScreen(String winnerName, String winnerIconPath) {
        this.winnerName = winnerName;
        this.winnerIconPath = winnerIconPath;
    }

    public String getWinnerName() {
        return winnerName;
    }

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

        Label congratsLabel = new Label("🎉 Congratulations! 🎉");
        congratsLabel.setStyle("-fx-font-size: 36px; -fx-text-fill: #333; -fx-font-weight: bold;");

        Label winnerLabel = new Label(winnerName + " wins!");
        winnerLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #555;");

        ImageView winnerImage = new ImageView(new Image(winnerIconPath));
        winnerImage.setFitWidth(150);
        winnerImage.setFitHeight(150);

        Button mainMenuButton = new Button("Return to Main Menu");
        mainMenuButton.setOnAction(e -> {
            SceneManager.getInstance().changeScene(new GameInitVisual().getScene("Snakes & Ladders"));
        });

        root.getChildren().addAll(congratsLabel, winnerImage, winnerLabel, mainMenuButton);

        return new Scene(root, 600, 400);
    }

    
}
