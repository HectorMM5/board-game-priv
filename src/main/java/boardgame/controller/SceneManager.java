package boardgame.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static SceneManager instance;
    private Stage primaryStage;

    private SceneManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the singleton instance of the {@code SceneManager}.
     * If no instance exists, a new one is created.
     *
     * @return the singleton instance of the {@code SceneManager}.
     */
    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    /**
     * Sets the primary stage of the application. This stage will be used for
     * displaying scenes.
     *
     * @param primaryStage the primary stage.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Returns the primary stage of the application.
     *
     * @return the primary stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Changes the current scene displayed on the primary stage.
     * It also applies the default stylesheet to the new scene.
     *
     * @param scene the new scene to display.
     */
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