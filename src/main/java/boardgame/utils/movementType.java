package boardgame.utils;

/**
 * Enumeration defining the different types of movement a player can make on the board.
 */
public enum movementType {
    /**
     * Represents movement along the defined path of the game board, typically step-by-step.
     */
    PATH,
    /**
     * Represents an immediate change in position, without traversing intermediate tiles.
     */
    INSTANT
}