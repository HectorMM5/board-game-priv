package boardgame.visual.elements;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class SelectIconPopUp extends VBox {

    private static final int IMAGES_PER_ROW = 3;
    private GridPane iconGrid;
    private String iconSelected;
    private Consumer<String> onIconSelected;

    public SelectIconPopUp(Consumer<String> onIconSelected) {
        this.onIconSelected = onIconSelected;
        this.iconGrid = new GridPane();
        init();
    }

    private void init() {
        iconGrid.setHgap(10);
        iconGrid.setVgap(10);

        String[] imageFiles = {
            "listhaug",
            "solbriller",
            "Red",
            "mythra",
            "daddyposter",
            "bingus"
        };

        List<String> imageNames = List.of(imageFiles);

        List<StackPane> icons = imageNames.stream().map(i -> {
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

            iconGrid.add(icons.get(i), row, col);
        });

        Button saveButton = new Button("Save choice");
        saveButton.setOnAction(e -> {
            if (iconSelected != null) {
                onIconSelected.accept(iconSelected);
            }
        });
        saveButton.setAlignment(Pos.CENTER);

        this.getChildren().addAll(iconGrid, saveButton);

    }

    public void cleanChoice() {
        iconGrid.getChildren().stream().forEach(node -> {
            if (node instanceof StackPane) {
                StackPane imageWrapper = (StackPane) node;
                imageWrapper.setStyle("-fx-border-color: transparent; -fx-border-width: 0;");
            }
        });
    }

    public String getIconChoice() {
        return iconSelected;
    }

}
