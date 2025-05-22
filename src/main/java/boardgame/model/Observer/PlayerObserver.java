package boardgame.model.Observer;

import boardgame.model.Player;
import boardgame.utils.movementType;

/**
 * An interface for observers that need to be notified when a player makes a move on the game board.
 */
public interface PlayerObserver {

    /**
     * Notifies the observer that a player has moved to a new tile.
     *
     * @param player        the player who made the move.
     * @param newTileNumber the index of the new tile the player moved to.
     * @param movementType  the type of movement that occurred (e.g., PATH, INSTANT).
     */
    void registerPlayerMove(Player player, int newTileNumber, movementType movementType);

}