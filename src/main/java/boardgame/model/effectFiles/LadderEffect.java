package boardgame.model.effectFiles;

import boardgame.controller.GameController;
import boardgame.model.boardFiles.Player;
import javafx.scene.paint.Color;

/**
 * Represents a ladder effect in the game, which moves the player forward
 * from a base tile to a higher target tile when triggered.
 * 
 * This effect is visually represented using the color blue.
 * 
 * Inherits movement logic from {@link MovementEffect}.
 * 
 * @author Hector Mendana Morales
 */
public class LadderEffect extends MovementEffect {

    /**
     * Constructs a LadderEffect that moves the player upwards from 
     * the base tile to the target tile.
     *
     * @param baseTileIndex the tile where the ladder begins
     * @param targetTileIndex the tile where the ladder ends
     */
    public LadderEffect(int baseTileIndex, int targetTileIndex) {
        super(baseTileIndex, targetTileIndex);
    }

    /**
     * Executes the ladder effect by moving the player to the target tile.
     *
     * @param player the player triggering the effect
     * @param gameController the game controller managing the game state
     */
    @Override
    public void execute(Player player, GameController gameController) {
        gameController.movePlayer(player, targetTileIndex);
    }

    /**
     * Returns the color used to represent a ladder effect.
     *
     * @return {@link Color#BLUE}
     */
    @Override
    public Color getColor() {
        return Color.BLUE;
    }
}
