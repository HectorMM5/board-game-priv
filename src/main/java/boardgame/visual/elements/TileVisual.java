package boardgame.visual.elements;

import java.util.List;

import boardgame.model.boardFiles.Player;
import boardgame.model.boardFiles.Tile;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
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
 * @author Hector Mendana Morales
 */
public class TileVisual extends StackPane {

    private final Tile tile;
    private final Rectangle background;
    private final ImageView iconView;
    private final Pane cover;
    private final Label playersInTile;
    private final Label viewNumber;

    /**
     * Constructs a new {@code TileVisual} based on paramet.
     *
     * @param tile the {@code Tile} object this visual represents
     */
    public TileVisual(Tile tile) {
        this.tile = tile;
        this.background = new Rectangle(50, 50);
        background.setFill(Color.rgb(255, 255, 224));

        this.iconView = new ImageView();
        iconView.setFitWidth(40);
        iconView.setFitHeight(40);

        viewNumber = new Label(Integer.toString(tile.getNumber()));

        this.cover = new Pane();
        cover.setPrefSize(50, 50);
        cover.setBackground(Background.EMPTY);

        this.playersInTile = new Label();

        this.getChildren().addAll(background, viewNumber, iconView, cover, playersInTile);

    }

    /**
     * <p>
     * Updates the visual elements of this tile based on its current state:
     * <p>
     *
     * <ul>
     * <li>If the tile has an effect, the background becomes red.</li>
     * <li>If one player is on the tile, their icon is shown.</li>
     * <li>If multiple players are on the tile, a white overlay and a count are
     * shown.</li>
     *
     * <li>If no players are present, the tile number is shown.</li>
     * </ul>
     */
    public void updateVisual() {
        if (tile.getEffect() != null) {
            this.background.setFill(tile.getEffect().getColor());
            viewNumber.setTextFill(Color.WHITE);            
        }

        List<Player> players = tile.getPlayers();

        if (!players.isEmpty()) {
            background.setFill(Color.LIGHTYELLOW);

            if (players.size() == 1) {
                String iconPath = players.get(0).getIcon();
                Image playerIcon = new Image(getClass().getResourceAsStream(iconPath));
                iconView.setImage(playerIcon);
                playersInTile.setText("");
                cover.setBackground(Background.EMPTY);
            } else {
                cover.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                playersInTile.setText(Integer.toString(players.size()));

            }

        } else if (iconView.getImage() != null || playersInTile.getText().equals("")) {
            iconView.setImage(null);
            playersInTile.setText("");
            cover.setBackground(Background.EMPTY);

        }

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
