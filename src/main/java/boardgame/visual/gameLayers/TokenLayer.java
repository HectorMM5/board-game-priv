package boardgame.visual.gameLayers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boardgame.model.Observer.PlayerObserver;
import boardgame.model.Player;
import boardgame.utils.movementType;
import boardgame.visual.elements.BoardVisual;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * An abstract layer for displaying and animating player tokens on a game board.
 * Subclasses are specific to the type of board (e.g., Ludo, Snakes and Ladders).
 */
public abstract class TokenLayer extends Pane implements PlayerObserver {

    protected final Map<Player, ImageView> playerTokens = new HashMap<>();
    protected final Map<Integer, Integer> cols = new HashMap<>();
    protected final Map<Integer, Integer> rows = new HashMap<>();
    protected final BoardVisual boardVisual;

    protected final List<Player> players;

    /**
     * Constructs a new {@code TokenLayer}.
     *
     * @param boardVisual the visual representation of the game board.
     * @param players     the list of players in the game.
     */
    protected TokenLayer(BoardVisual boardVisual, List<Player> players) {
        this.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: transparent;");
        this.boardVisual = boardVisual;
        this.prefWidthProperty().bind(boardVisual.getTileGrid().widthProperty());
        this.prefHeightProperty().bind(boardVisual.getTileGrid().heightProperty());

        this.players = players;

    }

    /**
     * Moves a player's token to a specific tile number on the board.
     * The implementation will handle the visual transition.
     *
     * @param player     the player whose token to move.
     * @param tileNumber the target tile number.
     */
    protected abstract void moveToken(Player player, int tileNumber);

    /**
     * Animates a player's token moving through a sequence of tiles to reach a destination.
     *
     * @param player  the player whose token to move.
     * @param endTile the final tile number to reach.
     */
    protected abstract void moveTokenThroughPath(Player player, int endTile);

    /**
     * Registers a player's move and initiates the corresponding visual update
     * of the token's position.
     *
     * @param player        the player who made the move.
     * @param newTileNumber the new tile number the player moved to.
     * @param movementType  the type of movement (e.g., INSTANT, PATH).
     */
    @Override
    public abstract void registerPlayerMove(Player player, int newTileNumber, movementType movementType);


}