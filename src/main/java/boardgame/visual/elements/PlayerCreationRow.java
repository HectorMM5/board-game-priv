package boardgame.visual.elements;

import java.io.InputStream;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PlayerCreationRow extends HBox {

    private final TextField nameField;
    private final Button saveButton;
    private StackPane iconWrapper;
    private ImageView iconDisplay;

    private String selectedIconName = "Red";

    public PlayerCreationRow() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.CENTER);
        nameField = new TextField();
        saveButton = new Button("Save Player");

        init();

        this.getChildren().addAll(iconWrapper, nameField, saveButton);

    }


    private void init() {
        // Top row: Icon button + name field + dummy button
        iconWrapper = new StackPane();
        iconWrapper.setPrefSize(50, 50);
        iconWrapper.setOnMouseClicked(e -> openIconPopup());

        InputStream is = getClass().getResourceAsStream("/PlayerIcons/" + selectedIconName + ".png");
        iconDisplay = new ImageView(new Image(is));
        iconDisplay.setFitWidth(50);
        iconDisplay.setFitHeight(50);

        iconWrapper.getChildren().add(iconDisplay);

        nameField.setPromptText("Enter player name...");
        nameField.setPrefWidth(200);

        saveButton.setOnAction(e -> {

        });

    }

    private void changeDisplayIcon(String iconName) {
        InputStream is = getClass().getResourceAsStream("/PlayerIcons/" + iconName + ".png");
        if (is != null) {
            iconDisplay.setImage(new Image(is));
        } else {
            System.out.println("Icon not found: " + iconName);
        }
    }

    private void openIconPopup() {
        Stage popupStage = new Stage();

        SelectIconPopUp popup = new SelectIconPopUp(selectedIcon -> {
            this.selectedIconName = selectedIcon;
            changeDisplayIcon(selectedIconName);
            refreshIconDisplay();
            popupStage.close();
        });

        Scene popupScene = new Scene(popup);
        popupStage.setScene(popupScene);
        popupStage.setTitle("Select Icon");
        popupStage.setResizable(false);
        popupStage.initOwner(this.getScene().getWindow()); // Set the owner of the popup to the main stage
        popupStage.centerOnScreen();
        popupStage.show();
    }

    private void refreshIconDisplay() {
        InputStream is = getClass().getResourceAsStream("/PlayerIcons/" + selectedIconName + ".png");
        iconDisplay = new ImageView(new Image(is));
        iconDisplay.setFitWidth(50);
        iconDisplay.setFitHeight(50);
    }

    public String getPlayerName() {
        return nameField.getText();
    }

    public String getSelectedIconName() {
        return "/PlayerIcons/" + selectedIconName + ".png";
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public TextField getNameField() {
        return nameField;
    }
}
