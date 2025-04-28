package boardgame.controller;

import java.util.List;

import boardgame.model.boardFiles.Player;
import boardgame.model.boardFiles.SnL.SnLBoard;
import boardgame.model.boardFiles.Tile;
import boardgame.model.effectFiles.MovementEffect;
import boardgame.utils.LoopingIterator;
import boardgame.visual.scenes.Ingame;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * Handles the core logic of the game, including player movement,
 * turn advancement, and interaction with the board and effects.
 * 
 * @author Hector Mendana Morales
 */
public class GameController {
    private final SnLBoard board;
    private final List<Tile> tiles;
    private final List<Player> players;
    private Player playerWhoseTurn;
    private final LoopingIterator<Player> playerIterator;
    private Player playerToSkip = null;
    private Ingame ingame;

    /**
     * Constructs a new GameController with the specified board and player list.
     *
     * @param board the game board
     * @param players the list of players participating in the game
     */
    public GameController(SnLBoard board, List<Player> players) {
        this.board = board;
        this.tiles = board.getTiles();
        System.out.println("Reached GameController with player list size: " + players.size());
        this.players = players;
        this.playerIterator = new LoopingIterator<>(players);
        this.playerWhoseTurn = playerIterator.next();
    }

    /**
     * Starts the game by placing all players on the first tile.
     */
    public void start() {
        for (Player player : players) {
            board.getTiles().get(0).addPlayer(player);
        }
    }

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

        if (!(targetTile.getEffect() == null)) {
            targetTile.getEffect().execute(player, this);
            PauseTransition pause = new PauseTransition(Duration.millis(300));
            pause.setOnFinished(event -> {
                if (targetTile.getEffect() instanceof MovementEffect movementEffect) {
                    ingame.getIngameController().moveToken(player, movementEffect.getTargetTileIndex());
                }
            });
            pause.play();
        }
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
     * Marks a player to skip their next turn.
     *
     * @param player the player who should skip their next turn
     */
    public void markPlayerToSkip(Player player) {
        playerToSkip = player;
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
    public SnLBoard getBoard() {
        return board;
    }

    /**
     * Advances the turn to the next player, skipping any player
     * who is marked to be skipped.
     */
    public void advanceTurn() {
        playerWhoseTurn = playerIterator.next();

        if (playerToSkip != null && playerToSkip.equals(playerWhoseTurn)) {
            playerToSkip = null;
            playerWhoseTurn = playerIterator.next();
        }
    }
}
