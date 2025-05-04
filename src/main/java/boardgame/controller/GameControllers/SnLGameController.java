package boardgame.controller.GameControllers;

import java.util.List;

import boardgame.model.boardFiles.Board;
import boardgame.model.boardFiles.Player;
import boardgame.model.boardFiles.Tile;
import boardgame.model.effectFiles.SnL.MovementEffect;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * Handles the core logic of the game, including player movement, turn
 * advancement, and interaction with the board and effects.
 *
 * @author Hector Mendana Morales
 */
public class SnLGameController extends GameController {

    private Player playerToSkip = null;

    /**
     * Constructs a new GameController with the specified board and player list.
     *
     * @param board the game board
     * @param players the list of players participating in the game
     */
    public SnLGameController(Board board, List<Player> players) {
        super(board, players);

        System.out.println("Reached SnL with players: " + players.toString());

    }

    /**
     * Starts the game by placing all players on the first tile.
     */
    @Override
    public void start() {
        for (Player player : players) {
            board.getTiles().get(0).addPlayer(player);
        }
    }

    /**
     * Moves the given player to the specified tile number and executes any
     * effect present on the target tile.
     *
     * @param player the player to move
     * @param tileNumber the target tile number to move the player to
     */
    @Override
    public void movePlayer(Player player, int tileNumber) {

        // Notify path move for initial movement
        player.getObservers().forEach(o -> o.registerPlayerPathMove(player, tileNumber));

        tiles.get(player.getPosition() - 1).popPlayer();
        player.setPosition(tileNumber);
        Tile targetTile = tiles.get(tileNumber - 1);
        targetTile.addPlayer(player);

        if (targetTile.getEffect() != null) {
            targetTile.getEffect().execute(player, this);

            if (targetTile.getEffect() instanceof MovementEffect movementEffect) {
                int effectTarget = movementEffect.getTargetTileIndex();

                PauseTransition pause = new PauseTransition(Duration.millis(300));
                pause.setOnFinished(event -> {
                    player.setPosition(effectTarget);
                    tiles.get(effectTarget - 1).addPlayer(player);
                    player.getObservers().forEach(o -> o.registerPlayerMove(player, effectTarget)); // use direct jump
                });
                pause.play();
            }
        }
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
     * Advances the turn to the next player, skipping any player who is marked
     * to be skipped.
     */
    @Override
    public void advanceTurn() {
        playerWhoseTurn = playerIterator.next();

        if (playerToSkip != null && playerToSkip.equals(playerWhoseTurn)) {
            playerToSkip = null;
            playerWhoseTurn = playerIterator.next();
        }
    }
}
