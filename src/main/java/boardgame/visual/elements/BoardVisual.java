package boardgame.visual.elements;

import java.util.ArrayList;

import boardgame.model.boardFiles.SnLBoard;
import boardgame.model.boardFiles.Tile;
import boardgame.utils.ScreenDimension;
import boardgame.visual.gameLayers.SnakesNLadders.LadderLayer;
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
public class BoardVisual extends StackPane {

    private final SnLBoard board;
    private final ArrayList<Tile> tileLogic;
    private final ArrayList<TileVisual> tileViews;
    private final double dimension = ScreenDimension.getScreenHeight() - 200; // Example dimension, adjust as needed
    private final LadderLayer ladderLayer;
    private final GridPane tileGrid = new GridPane();
    private final double TILE_SIZE = dimension / 10; 
    private final double spacing = TILE_SIZE + 4;
    

    /**
     * Constructs a new visual board based on the provided {@link SnLBoard} logic.
     * Initializes layout settings and populates the grid with visual tile elements.
     *
     * @param board the logical board to visualize
     */
    public BoardVisual(SnLBoard board) {
        this.board = board;
        this.tileLogic = board.getTiles();
        this.tileViews = new ArrayList<>();

        initializeBoard();
        this.ladderLayer = new LadderLayer(this, board.getTilesWithLadders(), board.getTilesWithSnakes()); 
        this.prefWidthProperty().bind(tileGrid.widthProperty());
        this.prefHeightProperty().bind(tileGrid.heightProperty());    
        this.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        this.getChildren().addAll(tileGrid, ladderLayer);
    }

    /**
     * Initializes the board layout by placing all tiles into the grid.
     * Tiles are arranged in a zig-zag pattern based on the board's width.
     */
    private void initializeBoard() {
        tileGrid.setHgap(4); // horizontal gap between tiles
        tileGrid.setVgap(4); // vertical gap between tiles
        tileGrid.setStyle("-fx-background-color: green;"); // background visible in gaps

        Boolean movesRight = false;

        for (int i = 0; i < board.getTileCount(); i++) {
            Tile tile = tileLogic.get(i);
            TileVisual tileVisual = new TileVisual(tile, TILE_SIZE, TILE_SIZE);
            tileViews.add(tileVisual);
        
            if ((i % board.getBoardWidth()) == 0) {
                movesRight = !movesRight;
            }
        
            int row = i / board.getBoardWidth();
            int col = movesRight
                ? i % board.getBoardWidth()
                : board.getBoardWidth() - ((i % board.getBoardWidth()) + 1);

            tileGrid.add(tileVisual, col, row);
        }
        
    }

    public GridPane getTileGrid() {
        return tileGrid;
    }

    public double getSpacing() {
        return spacing;
    }

}
