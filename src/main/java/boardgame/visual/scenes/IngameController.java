package boardgame.visual.scenes;

import boardgame.model.boardFiles.Player;
import boardgame.visual.elements.SideColumn.DiceButtonVisual;

public interface IngameController {
    
    public void moveBy(Player player, int steps, DiceButtonVisual buttonVisual);
    public void moveToken(Player player, int tileNumber);
    public void handleRollDice(DiceButtonVisual buttonVisual);
}
