package boardgame.visual.elements;

import java.util.Optional;

import boardgame.controller.SceneManager;
import boardgame.visual.scenes.StartScreenView;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

public class BackButton extends Button {

    /**
     * Creates a new BackButton with the default text "Back".
     * If {@code withPopup} is true, a confirmation dialog will appear before navigating.
     *
     * @param withPopup Whether to show a confirmation popup.
     */
    public BackButton(boolean withPopup) {
        super("Back");
        getStyleClass().add("button-common");

        this.setOnAction(e -> {
            if (withPopup) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Go back to Main Menu?");
                alert.setContentText("Do you really want to return to the main menu?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    SceneManager.getInstance().changeScene(StartScreenView.getScene());
                }
            } else {
                SceneManager.getInstance().changeScene(StartScreenView.getScene());
            }
        });
    }
}