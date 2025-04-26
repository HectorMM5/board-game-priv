package boardgame;

import boardgame.controller.SceneManager;
import boardgame.utils.ScreenDimension;
import boardgame.visual.scenes.StartScreenView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage){
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Board Game");

        SceneManager sceneManager = SceneManager.getInstance();

        primaryStage.setWidth(ScreenDimension.getScreenWidth());
        primaryStage.setHeight(ScreenDimension.getScreenHeight());

        sceneManager.setPrimaryStage(primaryStage);
        sceneManager.changeScene(StartScreenView.getScene());

    }

    public static void main(String[] args) {
        launch(args);
    }
}