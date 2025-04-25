package boardgame.visual.gameLayers.SnakesNLadders;

import java.util.Iterator;
import java.util.List;

import boardgame.model.boardFiles.Tile;
import boardgame.model.effectFiles.LadderEffect;
import boardgame.model.effectFiles.SnakeEffect;
import boardgame.visual.elements.BoardVisual;
import boardgame.visual.elements.TileVisual;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.transform.Rotate;

/**
 * A visual layer placed on top of the board that renders ladders and snakes
 * based on their corresponding effects in the game.
 * 
 * This layer is purely decorative and uses transformations to draw rotated
 * {@link LadderVisual} and {@link SnakeVisual} elements between tile positions.
 * 
 * @author Hector Mendana Morales
 */
public class LadderLayer extends Pane {

    private final BoardVisual boardVisual;

    /**
     * Constructs the ladder layer using the board visual and lists of tiles that
     * contain ladder and snake effects.
     *
     * @param boardVisual the visual representation of the game board
     * @param tilesWithLadders tiles that contain {@link LadderEffect}s
     * @param tilesWithSnakes tiles that contain {@link SnakeEffect}s
     */
    public LadderLayer(BoardVisual boardVisual, List<Tile> tilesWithLadders, List<Tile> tilesWithSnakes) {
        this.boardVisual = boardVisual;

        this.setPrefSize(536, 482);
        this.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        for (Tile tile : tilesWithLadders) {
            renderLadder((LadderEffect) tile.getEffect());
        }

        for (Tile tile : tilesWithSnakes) {
            renderSnake((SnakeEffect) tile.getEffect());
        }
    }

    /**
     * Renders a ladder graphic from the base tile to the target tile.
     *
     * @param ladder the ladder effect containing start and end tile indices
     */
    private void renderLadder(LadderEffect ladder) {
        LadderVisual ladderVisual;
        Integer baseX = null, baseY = null;
        Integer targetX = null, targetY = null;

        Iterator<Node> tileIterator = boardVisual.getChildren().iterator();

        final int TILE_SIZE = 50;
        final int GAP = 4;
        final int spacing = TILE_SIZE + GAP;

        while (((baseX == null) || (targetX == null)) && tileIterator.hasNext()) {
            Node tileNode = tileIterator.next();

            if (tileNode instanceof TileVisual tileVisual) {
                if (tileVisual.getTile().getNumber() == ladder.getBaseTileIndex()) {
                    baseX = BoardVisual.getColumnIndex(tileVisual);
                    baseY = BoardVisual.getRowIndex(tileVisual);
                } else if (tileVisual.getTile().getNumber() == ladder.getTargetTileIndex()) {
                    targetX = BoardVisual.getColumnIndex(tileVisual);
                    targetY = BoardVisual.getRowIndex(tileVisual);
                }
            }
        }

        int dx = targetX - baseX;
        int dy = targetY - baseY;
        double hypotenuse = Math.sqrt((dx * dx) + (dy * dy));

        ladderVisual = new LadderVisual(hypotenuse * spacing - TILE_SIZE / 2);
        ladderVisual.setLayoutX(baseX * spacing);
        ladderVisual.setLayoutY(baseY * spacing + TILE_SIZE / 2.0);

        double angle = Math.toDegrees(Math.atan2(dx, dy));
        ladderVisual.getTransforms().add(new Rotate(-angle, 25, 0));

        this.getChildren().add(ladderVisual);
    }

    /**
     * Renders a snake graphic from the base tile to the target tile.
     *
     * @param snake the snake effect containing start and end tile indices
     */
    private void renderSnake(SnakeEffect snake) {
        SnakeVisual snakeVisual;
        Integer baseX = null, baseY = null;
        Integer targetX = null, targetY = null;

        Iterator<Node> tileIterator = boardVisual.getChildren().iterator();

        final int TILE_SIZE = 50;
        final int GAP = 4;
        final int spacing = TILE_SIZE + GAP;

        while (((baseX == null) || (targetX == null)) && tileIterator.hasNext()) {
            Node tileNode = tileIterator.next();

            if (tileNode instanceof TileVisual tileVisual) {
                if (tileVisual.getTile().getNumber() == snake.getBaseTileIndex()) {
                    baseX = BoardVisual.getColumnIndex(tileVisual);
                    baseY = BoardVisual.getRowIndex(tileVisual);
                } else if (tileVisual.getTile().getNumber() == snake.getTargetTileIndex()) {
                    targetX = BoardVisual.getColumnIndex(tileVisual);
                    targetY = BoardVisual.getRowIndex(tileVisual);
                }
            }
        }

        int dx = targetX - baseX;
        int dy = targetY - baseY;
        double hypotenuse = Math.sqrt((dx * dx) + (dy * dy));

        snakeVisual = new SnakeVisual(hypotenuse * spacing - TILE_SIZE / 2);
        snakeVisual.setLayoutX(baseX * spacing);
        snakeVisual.setLayoutY(baseY * spacing + TILE_SIZE / 2.0);

        double angle = Math.toDegrees(Math.atan2(dx, dy));
        snakeVisual.getTransforms().add(new Rotate(-angle, 25, 0));

        this.getChildren().add(snakeVisual);
    }
}
