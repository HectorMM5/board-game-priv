package boardgame.visual.elements.Menu;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SelectIconPopUp extends VBox {

    private static final int IMAGES_PER_ROW = 3;
    private final GridPane iconGrid;
    private String iconSelected;
    private final Consumer<String> onIconSelected;

    public SelectIconPopUp(Consumer<String> onIconSelected) {
        this.onIconSelected = onIconSelected;
        this.iconGrid = new GridPane();
        init(); // Just builds the layout (no scene/stage)
    }

    public void showPopup() {
        Scene scene = new Scene(new SelectIconPopUp(onIconSelected)); // new instance for safety
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Select Icon");
        popupStage.setScene(scene);
        popupStage.setResizable(false);
        popupStage.show();
    }

    private void init() {
        iconGrid.setHgap(10);
        iconGrid.setVgap(10);

        String[] imageFiles = { "Red", "Lime", "White", "Orange", "Purple", "Yellow"};

        List<StackPane> icons = List.of(imageFiles).stream().map(i -> {
            InputStream is = getClass().getResourceAsStream("/PlayerIcons/" + i + ".png");
            if (is != null) {
                StackPane imageWrapper = new StackPane();
                imageWrapper.setPrefSize(105, 105);
                imageWrapper.setAlignment(Pos.CENTER);

                Image image = new Image(is);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);

                imageView.setOnMouseClicked(e -> {
                    cleanChoice();
                    iconSelected = i;
                    imageWrapper.setStyle("-fx-border-color: blue; -fx-border-width: 2;");
                });

                imageWrapper.getChildren().add(imageView);
                return imageWrapper;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        IntStream.range(0, icons.size()).forEach(i -> {
            int row = i / IMAGES_PER_ROW;
            int col = i % IMAGES_PER_ROW;
            iconGrid.add(icons.get(i), col, row);
        });

        Button saveButton = new Button("Save choice");
        saveButton.setOnAction(e -> {
            if (iconSelected != null) {
                onIconSelected.accept(iconSelected);
                ((Stage) this.getScene().getWindow()).close();
            }
        });

        saveButton.setAlignment(Pos.CENTER);
        saveButton.getStyleClass().add("button-common");

        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(iconGrid, saveButton);
    }

    public void cleanChoice() {
        iconGrid.getChildren().forEach(node -> {
            if (node instanceof StackPane imageWrapper) {
                imageWrapper.setStyle("-fx-border-color: transparent; -fx-border-width: 0;");
            }
        });
    }

    public String getIconChoice() {
        return iconSelected;
    }
}
