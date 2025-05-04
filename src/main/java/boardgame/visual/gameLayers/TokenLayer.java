package boardgame.visual.gameLayers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boardgame.model.Observer.PlayerObserver;
import boardgame.model.boardFiles.Player;
import boardgame.visual.elements.BoardVisual;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class TokenLayer extends Pane implements PlayerObserver {
    
    protected final Map<Player, ImageView> playerTokens = new HashMap<>();
    protected final Map<Integer, Integer> cols = new HashMap<>();
    protected final Map<Integer, Integer> rows = new HashMap<>();
    protected final BoardVisual boardVisual;

    
    protected TokenLayer(BoardVisual boardVisual, List<Player> players) {
        this.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: transparent;");
        this.boardVisual = boardVisual;
        this.prefWidthProperty().bind(boardVisual.getTileGrid().widthProperty());
        this.prefHeightProperty().bind(boardVisual.getTileGrid().heightProperty());

    }

    protected abstract void moveToken(Player player, int tileNumber);

    protected abstract void moveTokenThroughPath(Player player, int endTile);

    @Override
    public void registerPlayerMove(Player player, int newTileNumber) {
        moveTokenThroughPath(player, newTileNumber); 
    }
    


    
}
