package boardgame.visual.elements;

import java.util.ArrayList;

import boardgame.model.boardFiles.Board;
import boardgame.model.boardFiles.Tile;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

/**
 * Visual representation of the game board using JavaFX's {@link GridPane}.
 * This class arranges {@link TileVisual} components based on the logic from
 * the underlying {@link Board} object and supports updating the board display.
 * 
 * The tiles are laid out in a zig-zag (snaking) pattern across the grid.
 * 
 * @author Hector Mendana Morales
 */
public class BoardVisual extends GridPane {

    private final Board board;
    private final ArrayList<Tile> tileLogic;
    private final ArrayList<TileVisual> tileViews;

    /**
     * Constructs a new visual board based on the provided {@link Board} logic.
     * Initializes layout settings and populates the grid with visual tile elements.
     *
     * @param board the logical board to visualize
     */
    public BoardVisual(Board board) {
        this.board = board;
        this.tileLogic = board.getTiles();
        this.tileViews = new ArrayList<>();

        this.setPrefSize(536, 482);
        this.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        initializeBoard();
    }

    /**
     * Initializes the board layout by placing all tiles into the grid.
     * Tiles are arranged in a zig-zag pattern based on the board's width.
     */
    private void initializeBoard() {
        this.setHgap(4); // horizontal gap between tiles
        this.setVgap(4); // vertical gap between tiles
        this.setStyle("-fx-background-color: lightblue;"); // background visible in gaps

        Boolean movesRight = false;

        for (int i = 0; i < board.getTileCount(); i++) {
            Tile tile = tileLogic.get(i);
            TileVisual tileVisual = new TileVisual(tile);
            tileViews.add(tileVisual);
        
            if ((i % board.getBoardWidth()) == 0) {
                movesRight = !movesRight;
            }
        
            int row = i / board.getBoardWidth();
            int col = movesRight
                ? i % board.getBoardWidth()
                : board.getBoardWidth() - ((i % board.getBoardWidth()) + 1);

            this.add(tileVisual, col, row);
        }
    }

    /**
     * Updates the entire board's visuals by triggering a refresh
     * on each individual {@link TileVisual}.
     */
    public void updateEntireBoard() {
        for (TileVisual tv : tileViews) {
            tv.updateVisual();
        }
    }
}
