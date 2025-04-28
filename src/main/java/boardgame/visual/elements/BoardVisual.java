package boardgame.visual.elements;

import java.util.ArrayList;

import boardgame.model.boardFiles.Board;
import boardgame.model.boardFiles.Tile;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;


/**
 * Visual representation of the game board using JavaFX's {@link GridPane}.
 * This class arranges {@link TileVisual} components based on the logic from
 * the underlying {@link SnLBoard} object and supports updating the board display.
 * 
 * The tiles are laid out in a zig-zag (snaking) pattern across the grid.
 * 
 * @author Hector Mendana Morales
 */
public abstract class BoardVisual extends StackPane {

    public final Board board;
    public final ArrayList<Tile> tileLogic;
    public final ArrayList<TileVisual> tileViews;
    

    /**
     * Constructs a new visual board based on the provided {@link SnLBoard} logic.
     * Initializes layout settings and populates the grid with visual tile elements.
     *
     * @param board the logical board to visualize
     */
    public BoardVisual(Board board) {
        this.board = board;
        this.tileLogic = board.getTiles();
        this.tileViews = new ArrayList<>();

    }

    /**
     * Initializes the board layout by placing all tiles into the grid.
     * Tiles are arranged in a zig-zag pattern based on the board's width.
     */
    public abstract void initializeBoard();

}
