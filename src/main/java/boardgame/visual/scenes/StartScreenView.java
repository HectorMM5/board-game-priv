package boardgame.visual.scenes;

import boardgame.controller.GameBuilding.GameInitController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartScreenView {

    private final Stage primaryStage;

    public StartScreenView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void init() {
        VBox menuWrapper = new VBox();
        menuWrapper.setSpacing(30);

        GridPane menuPane = new GridPane();
        menuWrapper.getChildren().add(menuPane);
        menuPane.setAlignment(Pos.CENTER);

        GameInitController gameInitController = new GameInitController(primaryStage);

        Button SnLButton = new Button("Snakes & Ladders");

        SnLButton.setOnAction(e -> {
            gameInitController.init("Snakes & Ladders");
        });

        Button LudoButton = new Button("Ludo");
        LudoButton.setOnAction(e -> {
            gameInitController.init("Ludo");

        });

        Button HorseRaceButton = new Button("Horse race");
        HorseRaceButton.setOnAction(e -> {
            gameInitController.init("Horse race");

        });

        menuPane.add(SnLButton, 0, 0);
        menuPane.add(LudoButton, 1, 0);
        menuPane.add(HorseRaceButton, 2, 0);

        Scene menu = new Scene(menuWrapper, 600, 600);

        primaryStage.setScene(menu);
        primaryStage.setMaximized(true);
    }

    public void start() {
        primaryStage.show();
    }
}
