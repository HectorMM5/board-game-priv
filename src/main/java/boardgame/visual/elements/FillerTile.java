package boardgame.visual.elements;

import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class FillerTile extends StackPane {

    private Color color; // Default color

    public FillerTile(double width, double height, Color color) {
        this.color = color;
        this.setPrefSize(width, height);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);

        this.setBackground(Background.fill(color));

    }

    public FillerTile(double width, double height) {
        this.color = Color.BLACK; // Default color
        this.setPrefSize(width, height);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);

        this.setBackground(Background.fill(color));

    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.setBackground(Background.fill(color));
    }




    
}
