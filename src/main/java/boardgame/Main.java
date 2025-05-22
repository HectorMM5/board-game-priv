package boardgame;

import boardgame.controller.SceneManager;
import boardgame.utils.ScreenDimension;
import boardgame.visual.scenes.StartScreenView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main entry point for the Board Game application. Initializes the primary
 * stage and sets up the scene manager to display the initial start screen.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Board Game");

        SceneManager sceneManager = SceneManager.getInstance();

        primaryStage.setWidth(ScreenDimension.getScreenWidth());
        primaryStage.setHeight(ScreenDimension.getScreenHeight());

        sceneManager.setPrimaryStage(primaryStage);
        sceneManager.changeScene(StartScreenView.getScene());

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}