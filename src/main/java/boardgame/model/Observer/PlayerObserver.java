package boardgame.model.Observer;

import boardgame.model.boardFiles.Player;
import boardgame.utils.movementType;

public interface PlayerObserver {

    void registerPlayerMove(Player player, int newTileNumber, movementType movementType);

}
