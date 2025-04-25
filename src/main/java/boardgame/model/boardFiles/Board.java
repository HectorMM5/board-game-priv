package boardgame.model.boardFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.IntStream;

import boardgame.model.effectFiles.Effect;

/**
 * Represents the game board consisting of a grid of {@link Tile} objects.
 * Provides access to tile layout, dimensions, and applied effects.
 * 
 * A default board has a width of 10 and height of 9 (i.e., 90 tiles).
 * Each tile is initialized with a unique index and may be associated with a game effect.
 * 
 * @author Hector Mendana Morales
 */
public class Board {
    public final Random randomGenerator;
    private final ArrayList<Tile> tiles;
    private final HashMap<Tile, Effect> effectMap;
    private final int boardWidth;
    private final int boardHeight;
    private final int tileCount;

    /**
     * Constructs a default board of size 10x9 (90 tiles).
     */
    public Board() {
        this.randomGenerator = new Random();
        this.tiles = new ArrayList<>();
        this.effectMap = new HashMap<>();
        this.boardWidth = 10;
        this.boardHeight = 9;
        this.tileCount = boardWidth * boardHeight;

        IntStream.rangeClosed(1, tileCount)
            .forEach(i -> tiles.add(new Tile(i)));
    }

    /**
     * Constructs a custom-sized board with the given width and height.
     *
     * @param boardWidth the number of tiles in each row
     * @param boardHeight the number of tiles in each column
     */
    public Board(int boardWidth, int boardHeight) {
        this.randomGenerator = new Random();
        this.tiles = new ArrayList<>();
        this.effectMap = new HashMap<>();
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tileCount = boardWidth * boardHeight;

        IntStream.rangeClosed(1, tileCount)
            .forEach(i -> tiles.add(new Tile(i)));
    }

    /**
     * Returns the list of all tiles on the board.
     *
     * @return the list of tiles
     */
    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    /**
     * Returns the tile located at the specified index in the tile list.
     *
     * @param index the index of the tile
     * @return the tile at the specified index
     */
    public Tile getTileInIndex(int index) {
        return tiles.get(index);
    }

    /**
     * Returns the map of tile-effect associations.
     *
     * @return a map where each key is a tile and the value is its effect
     */
    public HashMap<Tile, Effect> getEffectMap() {
        return effectMap;
    }

    /**
     * Returns the total number of tiles on the board.
     *
     * @return the tile count
     */
    public int getTileCount() {
        return tileCount;
    }

    /**
     * Returns the number of columns (tiles wide) on the board.
     *
     * @return the board width
     */
    public int getBoardWidth() {
        return boardWidth;
    }

    /**
     * Returns the number of rows (tiles high) on the board.
     *
     * @return the board height
     */
    public int getBoardHeight() {
        return boardHeight;
    }
}
