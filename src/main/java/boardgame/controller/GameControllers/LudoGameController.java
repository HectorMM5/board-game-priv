package boardgame.controller.GameControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import boardgame.model.boardFiles.Board;
import boardgame.model.boardFiles.Player;
import boardgame.model.boardFiles.Tile;
import boardgame.utils.LudoBoardTiles;
import boardgame.utils.movementType;
import javafx.scene.paint.Color;

/**
 * Handles the core logic of the game, including player movement, turn
 * advancement, and interaction with the board and effects.
 *
 * @author Hector Mendana Morales
 */
public class LudoGameController extends GameController {

    private final List<Color> colors = new ArrayList<>(List.of(Color.YELLOW, Color.RED, Color.BLUE, Color.GREEN));

    private static final List<Tile> yellowHomeTiles = new ArrayList<>();
    private static final List<Tile> redHomeTiles = new ArrayList<>();
    private static final List<Tile> blueHomeTiles = new ArrayList<>();
    private static final List<Tile> greenHomeTiles = new ArrayList<>();

    private Map<Player, Integer> homePosition = new HashMap<>();

    static {
        IntStream.rangeClosed(1, 6).forEach(i -> {
            yellowHomeTiles.add(new Tile(i));
            redHomeTiles.add(new Tile(i));
            blueHomeTiles.add(new Tile(i));
            greenHomeTiles.add(new Tile(i));
        });
    }

    private static final Map<Color, Integer> colorStartPositions = LudoBoardTiles.getColorStartPositions();

    private static final Map<Color, List<Tile>> colorTiles = Map.of(
            Color.YELLOW, yellowHomeTiles,
            Color.RED, redHomeTiles,
            Color.BLUE, blueHomeTiles,
            Color.GREEN, greenHomeTiles
    );

    private final HashMap<Player, Color> playerColor = new HashMap<>();

    /**
     * Constructs a new GameController with the specified board and player list.
     *
     * @param board the game board
     * @param players the list of players participating in the game
     */
    public LudoGameController(Board board, List<Player> players) {
        super(board, players);

        IntStream.range(0, players.size()).forEach(i -> {
            homePosition.put(players.get(i), 0);
        });

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

            int startPosition = colorStartPositions.get(color);
            player.setPosition(startPosition, movementType.INSTANT);
            board.getTiles().get(startPosition - 1).addPlayer(player);
        });

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
        int adjustedNextPosition = tileNumber > 56 ? tileNumber - 56 : tileNumber;
        tiles.get(player.getPosition() - 1).popPlayer();

        player.setPosition(adjustedNextPosition, mT);
        Tile targetTile = tiles.get(adjustedNextPosition - 1);
        targetTile.addPlayer(player);

    }

    public void movePlayerThroughHome(Player player, int tileNumber) {
        Color color = playerColor.get(player);
        List<Tile> colorHome = colorTiles.get(color);
        int positionInHome = homePosition.get(player);

        if (positionInHome == 0) {
            disablePlayerOnBoard(player);

        } else {
            colorHome.get(positionInHome - 1).popPlayer();
        }

        colorHome.get(tileNumber - 1).addPlayer(player);
        homePosition.replace(player, tileNumber);

    }

    public void movePlayerThroughHomeBy(Player player, int steps) {
        int positionInHome = homePosition.get(player);

        int stopTile = positionInHome + steps;

        int adjustedStopTile = stopTile > 6 ? 6 : stopTile;

        movePlayerThroughHome(player, adjustedStopTile);

    }

    public void disablePlayerOnBoard(Player player) {
        List<Player> playersInPlayerTile = tiles.get(player.getPosition() - 1).getPlayers();
        playersInPlayerTile.removeIf(p -> p.equals(player));
    }

    public Map<Color, Integer> getStartPositions() {
        return colorStartPositions;
    }

    public Map<Player, Color> getPlayerColor() {
        return playerColor;
    }

    public static List<Tile> getYellowHomeTiles() {
        return yellowHomeTiles;
    }

    public static List<Tile> getRedHomeTiles() {
        return redHomeTiles;
    }

    public static List<Tile> getBlueHomeTiles() {
        return blueHomeTiles;
    }

    public static List<Tile> getGreenHomeTiles() {
        return greenHomeTiles;
    }

    public Map<Player, Integer> getHomePosition() {
        return homePosition;
    }
}
