package boardgame.model.effectFiles;

import boardgame.controller.GameController;
import boardgame.model.boardFiles.Player;
import javafx.scene.paint.Color;

/**
 * A placeholder implementation of the {@link Effect} interface.
 * This effect does nothing when executed, and is primarily used as a placeholder
 * for target tiles affected by other movement effects.
 * 
 * The visual color representation of this effect is gray.
 * 
 * @author Hector Mendana Morales
 */
public class PlaceholderEffect implements Effect {

    /**
     * Executes the placeholder effect, which does nothing.
     *
     * @param player the player affected by the effect (ignored)
     * @param gameController the game controller managing the game state (ignored)
     */
    @Override
    public void execute(Player player, GameController gameController) {
        // No action is performed
    }

    /**
     * Returns the color used to represent the placeholder effect.
     *
     * @return {@link Color#GRAY}
     */
    @Override
    public Color getColor() {
        return Color.GRAY;
    }
}
