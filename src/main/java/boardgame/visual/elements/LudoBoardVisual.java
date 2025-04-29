package boardgame.visual.elements;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import boardgame.model.boardFiles.Ludo.LudoBoard;
import boardgame.model.boardFiles.Tile;
import boardgame.utils.ScreenDimension;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public final class LudoBoardVisual extends BoardVisual {

    private final double TILE_SIZE = (ScreenDimension.getScreenHeight() - 200) / 15;
    private final GridPane tileGrid = new GridPane(-1, -1);

    public LudoBoardVisual(LudoBoard board) {
        super(board);
        initializeBoard();
    }

    @Override
    public void initializeBoard() {
        List<Point> playableTiles = new ArrayList<>();
        List<Point> yellowHomeTiles = new ArrayList<>();
        List<Point> redHomeTiles = new ArrayList<>();
        List<Point> greenHomeTiles = new ArrayList<>();
        List<Point> blueHomeTiles = new ArrayList<>();
        List<Point> yellowStartTiles = new ArrayList<>();
        List<Point> redStartTiles = new ArrayList<>();
        List<Point> greenStartTiles = new ArrayList<>();
        List<Point> blueStartTiles = new ArrayList<>();

        // Playable path
        IntStream.rangeClosed(2, 6).forEach(i -> playableTiles.add(new Point(8, i)));
        IntStream.rangeClosed(9, 14).forEach(i -> playableTiles.add(new Point(i, 6)));
        IntStream.rangeClosed(7, 8).forEach(i -> playableTiles.add(new Point(14, i)));
        IntStream.iterate(13, i -> i >= 8, i -> i - 1).forEach(i -> playableTiles.add(new Point(i, 8)));
        IntStream.rangeClosed(9, 14).forEach(i -> playableTiles.add(new Point(8, i)));
        IntStream.iterate(7, i -> i >= 6, i -> i - 1).forEach(i -> playableTiles.add(new Point(i, 14)));
        IntStream.iterate(13, i -> i >= 8, i -> i - 1).forEach(i -> playableTiles.add(new Point(6, i)));
        IntStream.iterate(5, i -> i >= 0, i -> i - 1).forEach(i -> playableTiles.add(new Point(i, 8)));
        IntStream.iterate(7, i -> i >= 6, i -> i - 1).forEach(i -> playableTiles.add(new Point(0, i)));
        IntStream.rangeClosed(1, 6).forEach(i -> playableTiles.add(new Point(i, 6)));
        IntStream.iterate(5, i -> i >= 0, i -> i - 1).forEach(i -> playableTiles.add(new Point(6, i)));
        IntStream.rangeClosed(7, 8).forEach(i -> playableTiles.add(new Point(i, 0)));
        playableTiles.add(new Point(8, 1));

        // Home tiles
        IntStream.rangeClosed(1, 6).forEach(i -> yellowHomeTiles.add(new Point(7, i)));
        IntStream.iterate(13, i -> i >= 8, i -> i - 1).forEach(i -> redHomeTiles.add(new Point(i, 7)));
        IntStream.iterate(13, i -> i >= 8, i -> i - 1).forEach(i -> blueHomeTiles.add(new Point(7, i)));
        IntStream.rangeClosed(1, 6).forEach(i -> greenHomeTiles.add(new Point(i, 7)));

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
            Tile tile = tileLogic.get(i);
            TileVisual tileVisual = new TileVisual(tile, TILE_SIZE, TILE_SIZE);
            tileGrid.add(tileVisual, point.x, point.y);
        });

        this.getChildren().add(tileGrid);
    }
}
