package boardgame.utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javafx.scene.paint.Color;

/**
 * Utility class defining the layout and properties of the Ludo board tiles.
 * It provides the coordinates of the playable tiles and the starting positions for each color.
 */
public class LudoBoardTiles {

    private static final List<Point> playableTiles = new ArrayList<>();

    static {
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
    }

    /**
     * Returns the list of {@link Point} objects representing the coordinates of the playable tiles on the Ludo board.
     *
     * @return a list of playable tile coordinates.
     */
    public static List<Point> getPlayableTiles() {
        return playableTiles;
    }

    private static final Map<Color, Integer> colorStartPositions = Map.of(
            Color.YELLOW, 43,
            Color.RED, 1,
            Color.BLUE, 15,
            Color.GREEN, 29
    );

    /**
     * Returns a map associating each player color with its starting tile index on the Ludo board.
     *
     * @return a map of color to starting tile index.
     */
    public static Map<Color, Integer> getColorStartPositions() {
        return colorStartPositions;
    }

}