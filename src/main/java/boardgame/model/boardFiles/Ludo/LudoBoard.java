package boardgame.model.boardFiles.Ludo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import boardgame.model.boardFiles.Board;
import boardgame.model.boardFiles.Tile;

/**
 * Represents a Ludo game board (15x15 grid) with a predefined path layout.
 * Only specific tile indices are used as playable path tiles.
 * The layout corresponds to the traditional Ludo cross-style board.
 * 
 * @author Hector
 */
public class LudoBoard extends Board {

    private final List<Tile> redPath = new ArrayList<>();
    private final List<Tile> greenPath = new ArrayList<>();
    private final List<Tile> yellowPath = new ArrayList<>();
    private final List<Tile> bluePath = new ArrayList<>();

    public LudoBoard() {
        super(); // Initializes tiles and effectMap


        // Generate the full 15x15 grid with null/empty tiles
        IntStream.rangeClosed(1, 56).forEach(i -> {
            tiles.add(new Tile(i)); // Initialize each tile with a unique index
        });

        IntStream.rangeClosed(1, 7).forEach(i -> {
            redPath.add(new Tile(i)); // Initialize each tile with a unique index
            greenPath.add(new Tile(i)); // Initialize each tile with a unique index
            yellowPath.add(new Tile(i)); // Initialize each tile with a unique index
            bluePath.add(new Tile(i)); // Initialize each tile with a unique index
        });

    }

    public List<Tile> getRedPath() {
        return redPath;
    }

    public List<Tile> getGreenPath() {
        return greenPath;
    }

    public List<Tile> getYellowPath() {
        return yellowPath;
    }
      
    public List<Tile> getBluePath() {
        return bluePath;
    }

}
