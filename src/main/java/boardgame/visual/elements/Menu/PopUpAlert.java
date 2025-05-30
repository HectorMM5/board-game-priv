package boardgame.visual.elements.Menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A reusable pop-up alert component to notify users about invalid actions.
 * Displays a given message string and a button to close the pop-up.
 * <p>
 * Usage example:
 * <pre>
 * new PopUpAlert("Please enter a player name!").show();
 * </pre>
 * Automatically resizes based on content.
 *
 *  
 */
public class PopUpAlert extends VBox {

    private final Stage popupStage;

    /**
     * Creates a new {@code PopUpAlert} with the specified message.
     *
     * @param message the text to display inside the pop-up.
     */
    public PopUpAlert(String message) {
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(20));
        this.setMinWidth(280);

        // Initialize stage first
        popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Notice");

        // Message
        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(260); // Ensures wrapping

        // Close button
        Button closeButton = new Button("OK");
        closeButton.getStyleClass().add("button-common");
        closeButton.setOnAction(e -> popupStage.close());

        // Add all elements
        this.getChildren().addAll(messageLabel, closeButton);

        // Scene setup
        Scene scene = new Scene(this);
        popupStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        popupStage.sizeToScene();
        popupStage.setResizable(false);
    }

    /**
     * Shows the pop-up alert and waits until it is closed by the user.
     */
    public void show() {
        popupStage.showAndWait();
    }
}