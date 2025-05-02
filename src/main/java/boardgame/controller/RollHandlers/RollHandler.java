package boardgame.controller.RollHandlers;

import boardgame.model.boardFiles.Player;
import boardgame.visual.elements.SideColumn.DiceButtonVisual;

public interface RollHandler {
    
    public void moveBy(Player player, int steps, DiceButtonVisual buttonVisual);
    public void moveToken(Player player, int tileNumber);
    public void handleRollDice(DiceButtonVisual buttonVisual);
}
