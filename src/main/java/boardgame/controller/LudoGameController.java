package boardgame.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import boardgame.model.boardFiles.Board;
import boardgame.model.boardFiles.Player;
import boardgame.model.boardFiles.Tile;
import javafx.scene.paint.Color;

/**
 * Handles the core logic of the game, including player movement,
 * turn advancement, and interaction with the board and effects.
 * 
 * @author Hector Mendana Morales
 */
public class LudoGameController extends GameController {

    private static final Map<Color, Integer> colorStartPositions = Map.of(
        Color.YELLOW, 1,
        Color.RED, 15,
        Color.BLUE, 29,
        Color.GREEN, 44
    );

    private static final Map<Color, Integer> colorEndPositions = Map.of(
        Color.YELLOW, 1,
        Color.RED, 15,
        Color.BLUE, 29,
        Color.GREEN, 44

    );

    private final List<Color> colors = new ArrayList<>(List.of(Color.YELLOW, Color.RED, Color.BLUE, Color.GREEN));

    private final HashMap<Player, Color> playerColor = new HashMap<>();

    /**
     * Constructs a new GameController with the specified board and player list.
     *
     * @param board the game board
     * @param players the list of players participating in the game
     */
    public LudoGameController(Board board, List<Player> players) {
        super(board, players);

    }

    /**
     * Starts the game by placing all players on the first tile.
     */
    @Override
    public void start() {
        IntStream.range(0, players.size()).forEach(i -> {
            Player player = players.get(i);
            Color color = colors.get(i);
            playerColor.put(player, color);
        
            int startPosition = colorStartPositions.getOrDefault(color, 0);
            player.setPosition(startPosition);
            board.getTiles().get(startPosition - 1).addPlayer(player);
        });

    }

    /**
     * Moves the given player to the specified tile number and executes
     * any effect present on the target tile.
     *
     * @param player the player to move
     * @param tileNumber the target tile number to move the player to
     */
    @Override
    public void movePlayer(Player player, int tileNumber) {
        tiles.get(player.getPosition() - 1).popPlayer();

        player.setPosition(tileNumber);
        Tile targetTile = tiles.get(tileNumber - 1);
        targetTile.addPlayer(player);

    }

    public Map<Color, Integer> getStartPositions() {
        return colorStartPositions;
    }

    public Map<Color, Integer> getEndPositions() {
        return colorEndPositions;
    }

    public Map<Player, Color> getPlayerColor() {
        return playerColor;
    }

}
