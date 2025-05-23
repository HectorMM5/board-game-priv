package boardgame.model.effectFiles;

import boardgame.controller.GameControllers.SnLGameController;
import boardgame.model.Player;
import javafx.scene.paint.Color;

/**
 * Represents a game effect that can be executed when a player interacts with a tile.
 * Effects may modify game state, such as moving the player or altering turn order.
 * 
 * All effect implementations must define their behavior via {@code execute},
 * and provide a visual color representation for display purposes.
 * 
 *  
 */
public interface Effect {

    /**
     * Executes the effect on the given player within the provided game context.
     *
     * @param player the player affected by the effect
     * @param gameController the game controller managing the game state
     */
    void execute(Player player, SnLGameController gameController);

    /**
     * Returns the color associated with this effect, typically for visual representation.
     *
     * @return the effect's color
     */
    Color getColor();
}
