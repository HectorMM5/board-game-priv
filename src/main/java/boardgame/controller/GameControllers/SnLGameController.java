package boardgame.controller.GameControllers;

import java.util.List;

import boardgame.controller.SceneManager;
import boardgame.model.boardFiles.Board;
import boardgame.model.boardFiles.Player;
import boardgame.model.boardFiles.Tile;
import boardgame.utils.movementType;
import boardgame.visual.scenes.WinScreen;
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
    public void movePlayer(Player player, int tileNumber, movementType mT) {

        int playerPosition = player.getPosition();

        if (tileNumber >= 90) {
            handleEndGame(player);
            player.setPosition(90, mT);
            return;
        }

        tiles.get(playerPosition - 1).popPlayer();
        player.setPosition(tileNumber, mT);
        Tile targetTile = tiles.get(tileNumber - 1);
        targetTile.addPlayer(player);

        if (targetTile.getEffect() != null) {
                targetTile.getEffect().execute(player, this);
            }
        }

    

    public void handleEndGame(Player player) {
        int playerPosition = player.getPosition();
        PauseTransition gameEndAnimation = new PauseTransition(Duration.millis((90 - playerPosition + 1) * 300 + 300));
        gameEndAnimation.setOnFinished(event -> {
            SceneManager.getInstance().changeScene(
                    new WinScreen(
                            player.getName(), player.getIcon()
                    ).getScene());
        });
        gameEndAnimation.play();
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
