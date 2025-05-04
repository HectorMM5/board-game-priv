package boardgame.model.Observer;

import boardgame.model.boardFiles.Player;

public interface PlayerObserver {
    void registerPlayerPathMove(Player player, int newTileNumber);

    void registerPlayerMove(Player player, int newTileNumber);

}
