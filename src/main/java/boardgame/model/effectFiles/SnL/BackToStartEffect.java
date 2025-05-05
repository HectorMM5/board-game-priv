package boardgame.model.effectFiles.SnL;

import boardgame.controller.GameControllers.SnLGameController;
import boardgame.model.boardFiles.Player;
import boardgame.utils.movementType;
import javafx.scene.paint.Color;

/**
 * Effect that causes a player to skip their next turn when triggered.
 * 
 * @author Hector Mendana Morales
 */
public class BackToStartEffect extends MovementEffect {
    public BackToStartEffect(int baseTileIndex, int targetTileIndex) {
        super(baseTileIndex, targetTileIndex);
    }

    @Override
    public void execute(Player player, SnLGameController gameController) {
        gameController.movePlayer(player, 1, movementType.INSTANT);
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }
    
}
