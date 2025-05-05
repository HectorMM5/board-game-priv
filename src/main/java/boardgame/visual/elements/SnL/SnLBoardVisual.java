package boardgame.visual.elements.SnL;

import boardgame.model.boardFiles.SnL.SnLBoard;
import boardgame.model.boardFiles.Tile;
import boardgame.utils.ScreenDimension;
import boardgame.visual.elements.BoardVisual;
import boardgame.visual.elements.TileVisual;
import javafx.scene.layout.GridPane;

/**
 * Visual representation of the game board using JavaFX's {@link GridPane}.
 * This class arranges {@link TileVisual} components based on the logic from
 * the underlying {@link SnLBoard} object and supports updating the board display.
 * 
 * The tiles are laid out in a zig-zag (snaking) pattern across the grid.
 * 
 * @author Hector Mendana Morales
 */
public final class SnLBoardVisual extends BoardVisual {

    private final double dimension = ScreenDimension.getScreenHeight() - 200; // Example dimension, adjust as needed
    private final LadderLayer ladderLayer;
    private final double TILE_SIZE = dimension / 10; 
    private final double spacing = TILE_SIZE + 4;
    

    /**
     * Constructs a new visual board based on the provided {@link SnLBoard} logic.
     * Initializes layout settings and populates the grid with visual tile elements.
     *
     * @param board the logical board to visualize
     */
    public SnLBoardVisual(SnLBoard board) {
        super(board);

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
    @Override
    public void initializeBoard() {
        tileGrid.setHgap(4); // horizontal gap between tiles
        tileGrid.setVgap(4); // vertical gap between tiles
        tileGrid.setStyle("-fx-background-color: black;"); // background visible in gaps

        Boolean movesRight = false;

        for (int i = 0; i < 90; i++) {
            Tile tile = tileLogic.get(i);
            TileVisual tileVisual = new TileVisual(tile, TILE_SIZE, TILE_SIZE);
        
            if ((i % 10) == 0) {
                movesRight = !movesRight;
            }
        
            int row = 8 - (i / 10);
            int col = movesRight
                ? i % 10
                : 10 - ((i % 10) + 1);

            tileGrid.add(tileVisual, col, row);
        }
        
    }

    @Override
    public GridPane getTileGrid() {
        return tileGrid;
    }

    @Override
    public double getSpacing() {
        return spacing;
    }

}
