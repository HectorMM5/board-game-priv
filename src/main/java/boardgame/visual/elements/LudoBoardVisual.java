package boardgame.visual.elements;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import boardgame.model.boardFiles.LudoBoard;
import boardgame.model.boardFiles.Tile;
import boardgame.utils.LudoBoardTiles;
import boardgame.utils.ScreenDimension;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Visual representation of the Ludo game board using JavaFX's {@link GridPane}.
 * This class arranges {@link TileVisual} and {@link FillerTile} components
 * to create the Ludo board layout based on the underlying {@link LudoBoard} logic.
 */
public final class LudoBoardVisual extends BoardVisual {

    private final double TILE_SIZE = (ScreenDimension.getScreenHeight() - 200) / 15;
    private final double spacing = TILE_SIZE;

    private final List<Point> yellowHomeTiles = new ArrayList<>();
    private final List<Point> redHomeTiles = new ArrayList<>();
    private final List<Point> greenHomeTiles = new ArrayList<>();
    private final List<Point> blueHomeTiles = new ArrayList<>();

    /**
     * Constructs a new visual Ludo board based on the provided {@link LudoBoard} logic.
     * Initializes layout settings and populates the grid with visual tile elements.
     *
     * @param board the logical Ludo board to visualize.
     */
    public LudoBoardVisual(LudoBoard board) {
        super(board);
        initializeBoard();

        tileGrid.setVgap(-1);
        tileGrid.setHgap(-1);
        this.prefWidthProperty().bind(tileGrid.widthProperty());
        this.prefHeightProperty().bind(tileGrid.heightProperty());
        this.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        this.getChildren().add(tileGrid);
    }

    /**
     * Initializes the Ludo board layout by placing playable tiles, home paths,
     * starting areas, and filler tiles into the grid.
     */
    @Override
    public void initializeBoard() {
        List<Point> playableTiles = LudoBoardTiles.getPlayableTiles();

        List<Point> yellowStartTiles = new ArrayList<>();
        List<Point> redStartTiles = new ArrayList<>();
        List<Point> greenStartTiles = new ArrayList<>();
        List<Point> blueStartTiles = new ArrayList<>();


        // Home tiles
        IntStream.rangeClosed(1, 6).forEach(i -> redHomeTiles.add(new Point(7, i)));
        IntStream.iterate(13, i -> i >= 8, i -> i - 1).forEach(i -> blueHomeTiles.add(new Point(i, 7)));
        IntStream.iterate(13, i -> i >= 8, i -> i - 1).forEach(i -> greenHomeTiles.add(new Point(7, i)));
        IntStream.rangeClosed(1, 6).forEach(i -> yellowHomeTiles.add(new Point(i, 7)));

        // Start boxes (5x5 hollow)
        IntStream.rangeClosed(0, 4).forEach(x -> {
            yellowStartTiles.add(new Point(x, 0));
            yellowStartTiles.add(new Point(x, 4));
        });
        IntStream.rangeClosed(1, 3).forEach(y -> {
            yellowStartTiles.add(new Point(0, y));
            yellowStartTiles.add(new Point(4, y));
        });

        IntStream.rangeClosed(10, 14).forEach(x -> {
            redStartTiles.add(new Point(x, 0));
            redStartTiles.add(new Point(x, 4));
        });
        IntStream.rangeClosed(1, 3).forEach(y -> {
            redStartTiles.add(new Point(10, y));
            redStartTiles.add(new Point(14, y));
        });

        IntStream.rangeClosed(0, 4).forEach(x -> {
            greenStartTiles.add(new Point(x, 10));
            greenStartTiles.add(new Point(x, 14));
        });
        IntStream.rangeClosed(11, 13).forEach(y -> {
            greenStartTiles.add(new Point(0, y));
            greenStartTiles.add(new Point(4, y));
        });

        IntStream.rangeClosed(10, 14).forEach(x -> {
            blueStartTiles.add(new Point(x, 10));
            blueStartTiles.add(new Point(x, 14));
        });
        IntStream.rangeClosed(11, 13).forEach(y -> {
            blueStartTiles.add(new Point(10, y));
            blueStartTiles.add(new Point(14, y));
        });

        // Color mapping
        Map<Point, Color> homeTileColors = new HashMap<>();
        yellowHomeTiles.forEach(p -> homeTileColors.put(p, Color.YELLOW));
        redHomeTiles.forEach(p -> homeTileColors.put(p, Color.RED));
        greenHomeTiles.forEach(p -> homeTileColors.put(p, Color.GREEN));
        blueHomeTiles.forEach(p -> homeTileColors.put(p, Color.BLUE));

        Map<Point, Color> startTileColors = new HashMap<>();
        yellowStartTiles.forEach(p -> startTileColors.put(p, Color.YELLOW));
        redStartTiles.forEach(p -> startTileColors.put(p, Color.RED));
        greenStartTiles.forEach(p -> startTileColors.put(p, Color.GREEN));
        blueStartTiles.forEach(p -> startTileColors.put(p, Color.BLUE));

        // Render non-playable tiles
        IntStream.range(0, 225).forEach(i -> {
            int row = i / 15;
            int col = i % 15;
            Point point = new Point(col, row);

            if (homeTileColors.containsKey(point)) {
                tileGrid.add(new FillerTile(TILE_SIZE, TILE_SIZE, homeTileColors.get(point)), col, row);
            } else if (startTileColors.containsKey(point)) {
                tileGrid.add(new FillerTile(TILE_SIZE, TILE_SIZE, startTileColors.get(point)), col, row);
            } else if (!playableTiles.contains(point)) {
                tileGrid.add(new FillerTile(TILE_SIZE, TILE_SIZE), col, row);
            }
        });

        // Add playable tile visuals
        IntStream.rangeClosed(1, playableTiles.size()).forEach(i -> {
            Point point = playableTiles.get(i - 1);
            Tile tile = tileLogic.get(i - 1);
            TileVisual tileVisual = new TileVisual(tile, TILE_SIZE, TILE_SIZE);
            tileGrid.add(tileVisual, point.x, point.y);
        });
    }

    /**
     * Returns the JavaFX GridPane used to lay out the tiles visually.
     *
     * @return the tile grid.
     */
    @Override
    public GridPane getTileGrid() {
        return tileGrid;
    }

    /**
     * Returns the spacing between the visual tiles.
     *
     * @return the spacing.
     */
    @Override
    public double getSpacing() {
        return spacing;
    }

    /**
     * Returns the list of coordinates for the yellow player's home path tiles.
     *
     * @return the list of yellow home tile coordinates.
     */
    public List<Point> getYellowHomeTiles() {
        return yellowHomeTiles;
    }

    /**
     * Returns the list of coordinates for the red player's home path tiles.
     *
     * @return the list of red home tile coordinates.
     */
    public List<Point> getRedHomeTiles() {
        return redHomeTiles;
    }

    /**
     * Returns the list of coordinates for the blue player's home path tiles.
     *
     * @return the list of blue home tile coordinates.
     */
    public List<Point> getBlueHomeTiles() {
        return blueHomeTiles;
    }

    /**
     * Returns the list of coordinates for the green player's home path tiles.
     *
     * @return the list of green home tile coordinates.
     */
    public List<Point> getGreenHomeTiles() {
        return greenHomeTiles;
    }

}