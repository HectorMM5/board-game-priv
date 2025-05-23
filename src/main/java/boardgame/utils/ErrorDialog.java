package boardgame.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class ErrorDialog {

    //ChatGPT

    public static void showAndExit(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        // Get the Stage (window) of the alert
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        // Optionally set the stage to be on top
        stage.setAlwaysOnTop(true);

        // Wait for the user to click a button (e.g., OK)
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Code to close the JavaFX application
                System.exit(0); // This will forcefully terminate the JVM
            }
        });
    }

    public static void showInfo(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        // Get the Stage (window) of the alert
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        // Optionally set the stage to be on top
        stage.setAlwaysOnTop(true);

        alert.showAndWait(); // Just show and wait for acknowledgement
    }

    public static void main(String[] args) {
        // Example usage:
        // To show an error and exit:
        // ErrorDialog.showAndExit("Application Error", "A critical error occurred!", "The application will now exit.");

        // To just show information:
        // ErrorDialog.showInfo("Information", "Operation Successful", "The file was imported.");
    }
}