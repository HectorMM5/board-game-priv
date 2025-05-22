package boardgame.controller;

import java.util.List;

import boardgame.model.Player;
import boardgame.utils.GameFactory;
import boardgame.utils.GameType;

public class GameInitController {

    /**
     * Handles the start of a new game based on the specified game type, board variant, and players.
     * It uses a {@link GameFactory} to initialize and start the game.
     *
     * @param gameType     the type of game to start (e.g., Snakes and Ladders, Ludo).
     * @param boardVariant the specific variant of the game board to use.
     * @param players      the list of players participating in the game.
     */
    public static void handleGameStart(GameType gameType, int boardVariant, List<Player> players) {
        new GameFactory(gameType, boardVariant, players).startGame();

    }

}