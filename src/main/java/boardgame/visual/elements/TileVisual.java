package boardgame.visual.elements;

import boardgame.model.boardFiles.Tile;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * <p>
 * A visual representation of a {@code Tile} in the board game. This component
 * is a {@code StackPane} that layers classic visual elements, such as:
 * </p>
 * <ul>
 * <li>A background color</li>
 * <li>The tile number</li>
 * </ul>
 *
 * <p>
 * In cases where there are players in the tile, the following elements also
 * apply:
 * </p>
 * <ul>
 * <li>A player icon (if one player is present)</li>
 * <li>An overlay and player counter (if multiple players are present)</li>
 * </ul>
 *
 * <p>
 * The visual is updated using {@link #updateVisual()} based on the tile state.
 * </p>
 *
 *  
 */
public class TileVisual extends StackPane {

    private final Tile tile;
    private final Rectangle background;
    private final Label viewNumber;

    /**
     * Constructs a new {@code TileVisual} based on paramet.
     *
     * @param tile the {@code Tile} object this visual represents
     */
    public TileVisual(Tile tile, double xDimension, double yDimension) {
        this.tile = tile;
        
        this.background = new Rectangle(xDimension, yDimension);
        background.setFill(Color.rgb(255, 255, 255));
        
        viewNumber = new Label(Integer.toString(tile.getNumber()));

        if (tile.getEffect() != null) {
            this.background.setFill(tile.getEffect().getColor());
            viewNumber.setTextFill(Color.WHITE);            
        }

        this.getChildren().addAll(background, viewNumber);

    }

    /**
     * Returns the {@code Tile} model associated with this visual.
     *
     * @return the {@code Tile} this visual represents
     */
    public Tile getTile() {
        return tile;
    }

}
