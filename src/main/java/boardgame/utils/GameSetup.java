package boardgame.utils;

import java.util.List;

import boardgame.controller.BoardJSON;
import boardgame.controller.GameController;
import boardgame.model.boardFiles.Board;
import boardgame.model.boardFiles.Player;
import boardgame.visual.scenes.Ingame;
import javafx.stage.Stage;

/**
 * Sets up and initializes a new game session, including the board, players,
 * game logic controller, and game scene.
 * 
 * This class serves as a central container for game-related components
 * and provides methods to launch the game UI.
 * 
 * @author Hector Mendana Morales
 */
public class GameSetup {

    private final Board board;
    private final List<Player> players;
    private final GameController gameController;
    private final Ingame ingame;

    /**
     * Constructs a GameSetup instance by initializing the board, player list,
     * game logic controller, and ingame UI wrapper.
     *
     * @param game the game mode identifier (currently unused)
     * @param boardChoice the index of the board to load from JSON
     * @param players the list of players participating in the game
     */
    public GameSetup(String game, int boardChoice, List<Player> players) {
        System.out.println("Reached GameSetup with player list size: " + players.size());
        this.board = BoardJSON.constructSnLBoardFromJSON(boardChoice);
        this.players = players;
        this.gameController = new GameController(board, players);
        this.ingame = new Ingame(this);
    }

    /**
     * Returns the board associated with this game setup.
     *
     * @return the game board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the game controller managing the logic for this game.
     *
     * @return the game controller
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Returns the list of players for this game.
     *
     * @return the player list
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Launches the game scene on the given stage.
     *
     * @param primaryStage the JavaFX stage to attach the game scene to
     */
    public void start(Stage primaryStage) {
        ingame.createGameScene(primaryStage);
    }
}
