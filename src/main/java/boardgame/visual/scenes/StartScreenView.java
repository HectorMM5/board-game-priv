package boardgame.visual.scenes;

import boardgame.controller.SceneManager;
import boardgame.visual.elements.Menu.GameInitVisual;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class StartScreenView {

    public static Scene getScene() {
        VBox root = new VBox();
        root.setSpacing(30);

        GridPane menuPane = new GridPane();
        root.getChildren().add(menuPane);
        menuPane.setAlignment(Pos.CENTER);

        GameInitVisual gameInitVisual = new GameInitVisual();


        SceneManager sceneManager = SceneManager.getInstance();

        Button SnLButton = new Button("Snakes & Ladders");
        SnLButton.setOnAction(e -> {
            sceneManager.changeScene(gameInitVisual.getScene("Snakes & Ladders"));
        });

        Button LudoButton = new Button("Ludo");
        LudoButton.setOnAction(e -> {
            sceneManager.changeScene(gameInitVisual.getScene("Ludo"));

        });

        Button HorseRaceButton = new Button("Horse race");
        HorseRaceButton.setOnAction(e -> {
            sceneManager.changeScene(gameInitVisual.getScene("Horse race"));

        });

        menuPane.add(SnLButton, 0, 0);
        menuPane.add(LudoButton, 1, 0);
        menuPane.add(HorseRaceButton, 2, 0);

        return new Scene(root);

    }

}
