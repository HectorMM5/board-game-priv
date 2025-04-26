package boardgame;

import boardgame.controller.SceneManager;
import boardgame.visual.scenes.StartScreenView;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage){
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Board Game");

        SceneManager sceneManager = SceneManager.getInstance();

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        sceneManager.setPrimaryStage(primaryStage);
        sceneManager.changeScene(new StartScreenView(primaryStage).getScene());




    }

    public static void main(String[] args) {
        launch(args);
    }
}