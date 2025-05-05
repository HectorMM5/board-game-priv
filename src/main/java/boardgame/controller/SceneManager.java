package boardgame.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;

    private SceneManager() {
        // Private constructor to prevent instantiation
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void changeScene(Scene scene) {
        if (primaryStage != null) {
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            System.out.println("Primary stage is not set. Cannot change scene.");
        }
    }

}
