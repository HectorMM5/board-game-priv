package boardgame.model.boardFiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import boardgame.model.effectFiles.Effect;

/**
 * Represents the game board consisting of a grid of {@link Tile} objects.
 * Provides access to tile layout, dimensions, and applied effects.
 * 
 * A default board has a width of 10 and height of 9 (i.e., 90 tiles).
 * Each tile is initialized with a unique index and may be associated with a game effect.
 * 
 *  
 */
public abstract class Board {
    public final Random randomGenerator;
    public final ArrayList<Tile> tiles;
    public final HashMap<Tile, Effect> effectMap;


    /**
     * Constructs a default board of size 10x9 (90 tiles).
     */
    public Board() {
        this.randomGenerator = new Random();
        this.tiles = new ArrayList<>();
        this.effectMap = new HashMap<>();
    }

    /**
     * Returns the list of all tiles on the board.
     *
     * @return the list of tiles
     */
    public ArrayList<Tile> getTiles() {
        return tiles;
    }

}
