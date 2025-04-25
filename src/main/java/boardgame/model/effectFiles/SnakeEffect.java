package boardgame.model.effectFiles;

import boardgame.controller.GameController;
import boardgame.model.boardFiles.Player;
import javafx.scene.paint.Color;

/**
 * Represents a snake effect in the game, which moves the player backward
 * from a base tile to a lower target tile when triggered.
 * 
 * This effect is visually represented using the color green.
 * 
 * Inherits movement logic from {@link MovementEffect}.
 * 
 * @author Hector Mendana Morales
 */
public class SnakeEffect extends MovementEffect {

    /**
     * Constructs a SnakeEffect that moves the player from the base tile to the target tile.
     *
     * @param baseTileIndex the tile where the snake begins
     * @param targetTileIndex the tile where the snake ends
     */
    public SnakeEffect(int baseTileIndex, int targetTileIndex) {
        super(baseTileIndex, targetTileIndex);
    }

    /**
     * Executes the snake effect by moving the player to the target tile.
     *
     * @param player the player triggering the effect
     * @param gameController the game controller managing the game state
     */
    @Override
    public void execute(Player player, GameController gameController) {
        gameController.movePlayer(player, targetTileIndex);
    }

    /**
     * Returns the color used to represent a snake effect.
     *
     * @return {@link Color#GREEN}
     */
    @Override
    public Color getColor() {
        return Color.GREEN;
    }
}
