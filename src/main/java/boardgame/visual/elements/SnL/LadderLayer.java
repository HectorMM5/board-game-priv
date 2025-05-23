package boardgame.visual.elements.SnL;

import java.util.Iterator;
import java.util.List;

import boardgame.model.boardFiles.Tile;
import boardgame.model.effectFiles.LadderEffect;
import boardgame.model.effectFiles.MovementEffect;
import boardgame.model.effectFiles.SnakeEffect;
import boardgame.utils.ScreenDimension;
import boardgame.visual.elements.TileVisual;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;

/**
 * A visual layer placed on top of the board that renders ladders and snakes
 * based on their corresponding effects in the game.
 * 
 * This layer is purely decorative and uses transformations to draw rotated
 * {@link LadderVisual} and {@link SnakeVisual} elements between tile positions.
 * 
 *  
 */
public class LadderLayer extends Pane {

    private final SnLBoardVisual boardVisual;
    final double TILE_SIZE = (ScreenDimension.getScreenHeight() - 200) / 10; 
    final int GAP = 4;
    final double spacing = TILE_SIZE + GAP;
    
    private enum EffectType {
        LADDER, SNAKE
    }

    /**
     * Constructs the ladder layer using the board visual and lists of tiles that
     * contain ladder and snake effects.
     *
     * @param boardVisual the visual representation of the game board
     * @param tilesWithLadders tiles that contain {@link LadderEffect}s
     * @param tilesWithSnakes tiles that contain {@link SnakeEffect}s
     */
    public LadderLayer(SnLBoardVisual boardVisual, List<Tile> tilesWithLadders, List<Tile> tilesWithSnakes) {
        this.boardVisual = boardVisual;

        this.prefWidthProperty().bind(boardVisual.getTileGrid().widthProperty());
        this.prefHeightProperty().bind(boardVisual.getTileGrid().heightProperty());

        tilesWithLadders.stream()
            .map(tile -> (MovementEffect) tile.getEffect())
            .forEach(effect -> renderEffect(effect, EffectType.LADDER));

        tilesWithSnakes.stream()
            .map(tile -> (MovementEffect) tile.getEffect())
            .forEach(effect -> renderEffect(effect, EffectType.SNAKE));
    
    }

    /**
     * Generalized method to render either a ladder or a snake visual.
     *
     * @param effect the effect object (either LadderEffect or SnakeEffect)
     * @param type the type of effect (ladder or snake)
     */
    private void renderEffect(MovementEffect effect, EffectType type) {
        Integer baseX = null, baseY = null;
        Integer targetX = null, targetY = null;

        int baseIndex = effect.getBaseTileIndex();
        int targetIndex = effect.getTargetTileIndex();

        Iterator<Node> tileIterator = boardVisual.getTileGrid().getChildren().iterator();

        while (((baseX == null) || (targetX == null)) && tileIterator.hasNext()) {
            Node tileNode = tileIterator.next();

            if (tileNode instanceof TileVisual tileVisual) {
                int number = tileVisual.getTile().getNumber();
                if (number == baseIndex) {
                    baseX = GridPane.getColumnIndex(tileVisual);
                    baseY = GridPane.getRowIndex(tileVisual);
                } else if (number == targetIndex) {
                    targetX = GridPane.getColumnIndex(tileVisual);
                    targetY = GridPane.getRowIndex(tileVisual);
                }
            }
        }

        int dx = targetX - baseX;
        int dy = targetY - baseY;
        double hypotenuse = Math.sqrt((dx * dx) + (dy * dy));

        Group visual;
        if (type == EffectType.LADDER) {
            visual = new LadderVisual(hypotenuse * spacing);
        } else {
            visual = new SnakeVisual(hypotenuse * spacing);
        }

        visual.setLayoutX(baseX * spacing + 12.5);
        visual.setLayoutY(baseY * spacing + TILE_SIZE / 2);

        double angle = Math.toDegrees(Math.atan2(dx, dy));
        visual.getTransforms().add(new Rotate(-angle, 25, 0));

        this.getChildren().add(visual);
    }
}