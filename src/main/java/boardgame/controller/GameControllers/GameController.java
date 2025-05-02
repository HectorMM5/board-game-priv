package boardgame.controller.GameControllers;

import java.util.List;

import boardgame.model.boardFiles.Board;
import boardgame.model.boardFiles.Player;
import boardgame.model.boardFiles.Tile;
import boardgame.utils.LoopingIterator;
import boardgame.visual.scenes.Ingame.Ingame;

/**
 * Handles the core logic of the game, including player movement,
 * turn advancement, and interaction with the board and effects.
 * 
 * @author Hector Mendana Morales
 */
public abstract class GameController {
    public final Board board;
    public final List<Tile> tiles;
    public final List<Player> players;
    public Player playerWhoseTurn;
    public final LoopingIterator<Player> playerIterator;
    public Ingame ingame;

    /**
     * Constructs a new GameController with the specified board and player list.
     *
     * @param board the game board
     * @param players the list of players participating in the game
     */
    public GameController(Board board, List<Player> players) {
        this.board = board;
        this.tiles = board.getTiles();
        this.players = players;
        this.playerIterator = new LoopingIterator<>(players);
        this.playerWhoseTurn = playerIterator.next();

    }

    /**
     * Starts the game by placing all players on the first tile.
     */
    public abstract void start();

    /**
     * Moves the given player to the specified tile number and executes
     * any effect present on the target tile.
     *
     * @param player the player to move
     * @param tileNumber the target tile number to move the player to
     */
    public void movePlayer(Player player, int tileNumber) {
        tiles.get(player.getPosition() - 1).popPlayer();

        player.setPosition(tileNumber);
        Tile targetTile = tiles.get(tileNumber - 1);
        targetTile.addPlayer(player);
    
    }

    /**
     * Sets the reference to the Ingame UI object for triggering visual effects.
     *
     * @param ingame the Ingame instance associated with this controller
     */
    public void setIngame(Ingame ingame) {
        this.ingame = ingame;
    }

    /**
     * Returns the player whose turn it currently is.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return playerWhoseTurn;
    }

    /**
     * Returns the list of players in the game.
     *
     * @return the player list
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the game board.
     *
     * @return the board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Advances the turn to the next player, skipping any player
     * who is marked to be skipped.
     */
    public void advanceTurn() {
        playerWhoseTurn = playerIterator.next();

    }
}
