package boardgame.model.boardFiles;

import java.util.List;
import java.util.stream.IntStream;

import boardgame.model.effectFiles.LadderEffect;
import boardgame.model.effectFiles.SnakeEffect;

/**
 * Represents the game board consisting of a grid of {@link Tile} objects.
 * Provides access to tile layout, dimensions, and applied effects.
 * <p>
 * A default board has a width of 10 and height of 9 (i.e., 90 tiles).
 * Each tile is initialized with a unique index and may be associated with a game effect.
 *
 * @author Hector Mendana Morales
 */
public class SnLBoard extends Board {
    private final int boardWidth;
    private final int boardHeight;
    private final int tileCount;

    /**
     * Constructs a default board of size 10x9 (90 tiles).
     */
    public SnLBoard() {
        this.boardWidth = 10;
        this.boardHeight = 9;
        this.tileCount = boardWidth * boardHeight;

        IntStream.rangeClosed(1, tileCount)
                .forEach(i -> tiles.add(new Tile(i)));
    }

    /**
     * Constructs a custom-sized board with the given width and height.
     *
     * @param boardWidth  the number of tiles in each row
     * @param boardHeight the number of tiles in each column
     */
    public SnLBoard(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.tileCount = boardWidth * boardHeight;

        IntStream.rangeClosed(1, tileCount)
                .forEach(i -> tiles.add(new Tile(i)));
    }

    /**
     * Returns a list of tiles that have a {@link LadderEffect} associated with them.
     *
     * @return a list of tiles with ladders.
     */
    public List<Tile> getTilesWithLadders() {
        return tiles.stream()
                .filter(tile -> tile.getEffect() instanceof LadderEffect)
                .toList();
    }

    /**
     * Returns a list of tiles that have a {@link SnakeEffect} associated with them.
     *
     * @return a list of tiles with snakes.
     */
    public List<Tile> getTilesWithSnakes() {
        return tiles.stream()
                .filter(tile -> tile.getEffect() instanceof SnakeEffect)
                .toList();
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