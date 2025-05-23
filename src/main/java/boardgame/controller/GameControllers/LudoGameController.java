package boardgame.controller.GameControllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import boardgame.model.Player;
import boardgame.model.boardFiles.Board;
import boardgame.model.boardFiles.LudoBoard;
import boardgame.model.boardFiles.Tile;
import boardgame.utils.LudoBoardTiles;
import boardgame.utils.movementType;
import javafx.scene.paint.Color;

/**
 * Handles the core logic for the Ludo game, including player movement,
 * turn management, and handling movement into home tiles.
 * <p>
 * Extends {@link GameController} and specializes the logic for Ludo's
 * unique board layout and rules, such as per-color home paths and
 * start positions.
 * </p>
 * 
 * 
 */
public class LudoGameController extends GameController {

    private final List<Color> colors = new ArrayList<>(List.of(Color.YELLOW, Color.RED, Color.BLUE, Color.GREEN));

    private final List<Tile> yellowHomeTiles = ((LudoBoard) board).getYellowPath();
    private final List<Tile> redHomeTiles = ((LudoBoard) board).getRedPath();
    private final List<Tile> blueHomeTiles = ((LudoBoard) board).getBluePath();
    private final List<Tile> greenHomeTiles = ((LudoBoard) board).getGreenPath();

    private final Map<Player, Integer> homePosition = new HashMap<>();

    private static final Map<Color, Integer> colorStartPositions = LudoBoardTiles.getColorStartPositions();

    private final Map<Color, List<Tile>> colorTiles = Map.of(
        Color.YELLOW, yellowHomeTiles,
        Color.RED, redHomeTiles,
        Color.BLUE, blueHomeTiles,
        Color.GREEN, greenHomeTiles
    );

    private final HashMap<Player, Color> playerColor = new HashMap<>();

    /**
     * Constructs a LudoGameController with the given board and list of players.
     * Each player is assigned a color and tracked in {@code homePosition}.
     *
     * @param board the Ludo board to use
     * @param players the players in the game
     */
    public LudoGameController(Board board, List<Player> players) {
        super(board, players);

        IntStream.range(0, players.size()).forEach(i -> {
            homePosition.put(players.get(i), 0);
        });
    }

    /**
     * Starts the Ludo game by placing each player on their assigned
     * starting tile based on color.
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
     * Moves a player to a new tile on the main board.
     * If the tile number exceeds 56, it wraps around using modulo logic.
     *
     * @param player the player to move
     * @param tileNumber the destination tile number
     * @param mT the type of movement used
     */
    @Override
    public void movePlayer(Player player, int tileNumber, movementType mT) {
        int adjustedNextPosition = tileNumber > 56 ? tileNumber - 56 : tileNumber;
        tiles.get(player.getPosition() - 1).popPlayer();

        player.setPosition(adjustedNextPosition, mT);
        Tile targetTile = tiles.get(adjustedNextPosition - 1);
        targetTile.addPlayer(player);
    }

    /**
     * Moves a player into their home path (color-specific area) to the
     * specified tile index.
     * Removes the player from the main board if they are just entering.
     *
     * @param player the player to move
     * @param tileNumber the index of the home tile to move to (1–6)
     */
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

    /**
     * Moves a player forward a number of steps in their home path.
     * Will not exceed position 6 (goal tile).
     *
     * @param player the player to move
     * @param steps the number of tiles to move
     */
    public void movePlayerThroughHomeBy(Player player, int steps) {
        int positionInHome = homePosition.get(player);
        int stopTile = positionInHome + steps;
        int adjustedStopTile = Math.min(stopTile, 6);
        movePlayerThroughHome(player, adjustedStopTile);
    }

    /**
     * Removes a player from the current tile on the board.
     * Called when a player enters their home path.
     *
     * @param player the player to disable
     */
    public void disablePlayerOnBoard(Player player) {
        List<Player> playersInPlayerTile = tiles.get(player.getPosition() - 1).getPlayers();
        playersInPlayerTile.removeIf(p -> p.equals(player));
    }

    /**
     * Returns the map of color start positions.
     *
     * @return a map from color to tile index
     */
    public Map<Color, Integer> getStartPositions() {
        return colorStartPositions;
    }

    /**
     * Returns the color assigned to each player.
     *
     * @return a map of player to color
     */
    public Map<Player, Color> getPlayerColor() {
        return playerColor;
    }

    /**
     * @return the list of yellow home tiles
     */
    public List<Tile> getYellowHomeTiles() {
        return yellowHomeTiles;
    }

    /**
     * @return the list of red home tiles
     */
    public List<Tile> getRedHomeTiles() {
        return redHomeTiles;
    }

    /**
     * @return the list of blue home tiles
     */
    public List<Tile> getBlueHomeTiles() {
        return blueHomeTiles;
    }

    /**
     * @return the list of green home tiles
     */
    public List<Tile> getGreenHomeTiles() {
        return greenHomeTiles;
    }

    /**
     * Returns each player's position inside their home path (1–6).
     *
     * @return map of player to their home tile index
     */
    public Map<Player, Integer> getHomePosition() {
        return homePosition;
    }
}
