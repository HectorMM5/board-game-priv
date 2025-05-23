package boardgame.visual.elements;

import java.util.ArrayList;

import boardgame.model.boardFiles.Board;
import boardgame.model.boardFiles.Tile;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;


/**
 * Abstract class providing a base for visual representations of game boards.
 * It manages the underlying logical board, its tiles, and a JavaFX GridPane
 * for arranging the visual tiles. Subclasses are responsible for implementing
 * the specific layout of the tiles.
 *
 *  
 */
public abstract class BoardVisual extends StackPane {

    public final Board board;
    public final ArrayList<Tile> tileLogic;
    public final GridPane tileGrid = new GridPane();


    /**
     * Constructs a new visual board based on the provided {@link Board} logic.
     *
     * @param board the logical board to visualize.
     */
    public BoardVisual(Board board) {
        this.board = board;
        this.tileLogic = board.getTiles();
    }

    /**
     * Initializes the board layout by placing all tiles into the grid.
     * The specific arrangement of tiles is determined by the implementing subclass.
     */
    public abstract void initializeBoard();

    /**
     * Returns the JavaFX GridPane that contains the visual tiles.
     *
     * @return the tile grid.
     */
    public abstract GridPane getTileGrid();

    /**
     * Returns the spacing between the visual tiles in the grid.
     *
     * @return the spacing.
     */
    public abstract double getSpacing();

}