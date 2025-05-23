package boardgame.model.effectFiles;

import boardgame.controller.GameControllers.SnLGameController;
import boardgame.model.Player;
import boardgame.utils.movementType;
import javafx.scene.paint.Color;

/**
 * Effect that moves a player back to the starting position (tile index 1) when triggered.
 *
 *  
 */
public class BackToStartEffect extends MovementEffect {
    /**
     * Constructs a {@code BackToStartEffect}.
     *
     * @param baseTileIndex   the index of the tile where this effect originates.
     * @param targetTileIndex the index of the tile this effect leads to (though in this case, it's always 1).
     */
    public BackToStartEffect(int baseTileIndex, int targetTileIndex) {
        super(baseTileIndex, targetTileIndex);
    }

    /**
     * Executes the effect by moving the given player to the starting tile (index 1) instantly.
     *
     * @param player         the player on whom the effect is executed.
     * @param gameController the game controller for performing player movements.
     */
    @Override
    public void execute(Player player, SnLGameController gameController) {
        gameController.movePlayer(player, 1, movementType.INSTANT);
    }

    /**
     * Returns the color associated with this effect for visual representation.
     *
     * @return the color black.
     */
    @Override
    public Color getColor() {
        return Color.BLACK;
    }

}