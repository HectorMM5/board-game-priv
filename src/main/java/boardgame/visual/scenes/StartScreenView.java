package boardgame.visual.scenes;

import boardgame.controller.SnakesAndLaddersInitController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class StartScreenView {
    private final Stage primaryStage;

    public StartScreenView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void init() {

        primaryStage.setTitle("primaryStage");

        VBox menuWrapper = new VBox();
        GridPane menuPane = new GridPane();
        menuWrapper.getChildren().add(menuPane);
        menuPane.setAlignment(Pos.CENTER);
        menuWrapper.setMargin(menuPane, new Insets(50,50,50,50));

        Button b1 = new Button("Snakes & ladders");
        Button b2 = new Button("Goose game???");
        Button b3 = new Button("Ludo!");

        b1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                new SnakesAndLaddersInitController(primaryStage).start();
            }
        });


        Scene menu = new Scene(menuWrapper, 600, 600);
        menuPane.getChildren().addAll(b1, b2, b3);
        GridPane.setColumnIndex(b1,0);
        GridPane.setColumnIndex(b2,1);
        GridPane.setColumnIndex(b3,2);

        primaryStage.setScene(menu);
    }

    public void start() {
        primaryStage.show();
    }
}
