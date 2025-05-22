package boardgame.visual.elements;

import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Represents a simple rectangular tile used as a filler in layouts, with a
 * specified color and dimensions.
 */
public class FillerTile extends StackPane {

    private Color color; // Default color

    /**
     * Constructs a {@code FillerTile} with the given width, height, and color.
     *
     * @param width  the width of the filler tile.
     * @param height the height of the filler tile.
     * @param color  the color of the filler tile.
     */
    public FillerTile(double width, double height, Color color) {
        this.color = color;
        this.setPrefSize(width, height);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);

        this.setBackground(Background.fill(color));

    }

    /**
     * Constructs a {@code FillerTile} with the given width and height, using
     * black as the default color.
     *
     * @param width  the width of the filler tile.
     * @param height the height of the filler tile.
     */
    public FillerTile(double width, double height) {
        this.color = Color.BLACK; // Default color
        this.setPrefSize(width, height);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);

        this.setBackground(Background.fill(color));

    }

    /**
     * Gets the current color of the filler tile.
     *
     * @return the color of the tile.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color of the filler tile and updates its background.
     *
     * @param color the new color for the tile.
     */
    public void setColor(Color color) {
        this.color = color;
        this.setBackground(Background.fill(color));
    }


}