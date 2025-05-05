package boardgame.visual.elements.Menu;

import java.io.InputStream;
import java.util.List;
import java.util.stream.IntStream;

import boardgame.utils.PlayerCSV;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlayerCreationRow extends HBox {

    private final TextField nameField;
    private final Button saveButton;
    private final MenuButton fetchButton;
    private final Button deleteRowButton;
    private Runnable deleteRowAction;
    private final PlayerCSV playerCSV;
    private StackPane iconWrapper;
    private ImageView iconDisplay;

    private String selectedIconName = "Red";

    public PlayerCreationRow() {
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.CENTER);
        this.playerCSV = PlayerCSV.instance();
        this.nameField = new TextField();
        this.saveButton = new Button("Save Player");
        this.deleteRowButton = new Button("X");
        this.fetchButton = new MenuButton("->");

        saveButton.getStyleClass().add("button-common");
        deleteRowButton.getStyleClass().add("button-common");
        fetchButton.getStyleClass().add("button-common");

        init();

        this.getChildren().addAll(fetchButton, iconWrapper, nameField, saveButton, deleteRowButton);

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
            try {
                playerCSV.registerNewPlayer(nameField.getText(), selectedIconName);

            } catch (IllegalArgumentException ex) {
                PopUpAlert alert = new PopUpAlert(ex.getMessage());
                alert.show();
            }

        });

        deleteRowButton.setOnAction(e -> {
            deleteRowAction.run();
        });

        List<String[]> playerNames = PlayerCSV.getCSVContent();
        IntStream.range(0, playerNames.size()).forEach(i -> {
            MenuItem userOption = new MenuItem(playerNames.get(i)[0]);
            userOption.setOnAction(e -> {
                nameField.setText(playerNames.get(i)[0]);
                changeDisplayIcon(playerNames.get(i)[1]);
            });

            fetchButton.getItems().add(userOption);

        });

    }

    private void changeDisplayIcon(String iconName) {
        InputStream is = getClass().getResourceAsStream("/PlayerIcons/" + iconName + ".png");
        if (is != null) {
            this.selectedIconName = iconName;
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
            popupStage.close();
        });

        Scene popupScene = new Scene(popup);
        popupStage.setScene(popupScene);
        popupStage.setTitle("Select Icon");
        popupStage.setResizable(false);
        popupStage.initOwner(this.getScene().getWindow()); 
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.centerOnScreen();
        popupStage.showAndWait();
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

    public void setDeleteRowAction(Runnable deleteRowAction) {
        this.deleteRowAction = deleteRowAction;
    }
}
