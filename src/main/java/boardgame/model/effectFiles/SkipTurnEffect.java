package boardgame.model.effectFiles;

import boardgame.controller.GameController;
import boardgame.model.boardFiles.Player;
import javafx.scene.paint.Color;

/**
 * Effect that causes a player to skip their next turn when triggered.
 * 
 * This effect is visually represented using the color red.
 * 
 * @author Hector Mendana Morales
 */
public class SkipTurnEffect implements Effect {

    /**
     * Executes the skip turn effect by marking the player to miss their next turn.
     *
     * @param player the player who will skip their next turn
     * @param gameController the game controller managing the game state
     */
    @Override
    public void execute(Player player, GameController gameController) {
        gameController.markPlayerToSkip(player);
    }

    /**
     * Returns the color used to represent this effect.
     *
     * @return {@link Color#RED}
     */
    @Override
    public Color getColor() {
        return Color.RED;
    }
}
