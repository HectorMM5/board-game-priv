package boardgame.model.effectFiles;

import boardgame.controller.GameController;
import boardgame.model.boardFiles.Player;

/**
 * An abstract base class for all effects that involve moving a player
 * from one tile to another. This class stores both the base and target tile indices,
 * and provides a default implementation of the {@link #execute} method
 * to move the player.
 * 
 * Subclasses must define how the effect is visually represented through {@code getColor()}.
 * 
 * @author Hector Mendana Morales
 */
public abstract class MovementEffect implements Effect {
    public int baseTileIndex;
    public int targetTileIndex;

    /**
     * Constructs a movement effect between a base tile and a target tile.
     *
     * @param baseTileIndex the index of the starting tile
     * @param targetTileIndex the index of the destination tile
     */
    public MovementEffect(int baseTileIndex, int targetTileIndex) {
        this.baseTileIndex = baseTileIndex;
        this.targetTileIndex = targetTileIndex;
    }

    /**
     * Executes the effect by moving the player to the target tile index.
     *
     * @param player the player to move
     * @param gameController the game controller managing the game state
     */
    @Override
    public void execute(Player player, GameController gameController) {
        gameController.movePlayer(player, targetTileIndex);
    }

    /**
     * Returns the base tile index of the effect.
     *
     * @return the starting tile index
     */
    public int getBaseTileIndex() {
        return baseTileIndex;
    }

    /**
     * Returns the target tile index to which the player should be moved.
     *
     * @return the destination tile index
     */
    public int getTargetTileIndex() {
        return targetTileIndex;
    }
}
