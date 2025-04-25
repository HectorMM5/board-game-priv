package boardgame.model.boardFiles;

import java.util.ArrayList;
import java.util.List;

import boardgame.model.effectFiles.Effect;

/**
 * Represents a single tile on the game board. A tile can have an associated {@link Effect}
 * and may contain one or more {@link Player} instances.
 * Each tile has a unique number identifying its position.
 * 
 * @author Hector Mendana Morales
 */
public class Tile {
    
    private List<Player> players;
    private Effect effect;
    private final int number;

    /**
     * Constructs a tile with a specific tile number.
     *
     * @param number the tile's position or identifier on the board
     */
    public Tile(int number) {
        this.number = number;
        this.effect = null;
        this.players = new ArrayList<>();
    }

    /**
     * Returns the tile number.
     *
     * @return the tile's identifier
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the effect assigned to this tile, or {@code null} if none exists.
     *
     * @return the tile's effect
     */
    public Effect getEffect() {
        return effect;
    }

    /**
     * Returns a list of players currently on this tile.
     *
     * @return the list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Adds a player to this tile.
     *
     * @param recievedPlayer the player to add
     */
    public void addPlayer(Player recievedPlayer) {
        players.add(recievedPlayer);
    }

    /**
     * Removes the first player from the list of players on this tile.
     */
    public void popPlayer() {
        players.remove(0);
    }

    /**
     * Sets the effect for this tile.
     *
     * @param effect the effect to assign to the tile
     */
    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
