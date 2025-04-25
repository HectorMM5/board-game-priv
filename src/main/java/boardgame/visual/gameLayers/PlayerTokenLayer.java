package boardgame.visual.gameLayers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import boardgame.model.boardFiles.Player;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

/**
 * A visual layer used to display and animate player tokens on top of the board.
 * This class calculates tile positions based on a zig-zag layout and supports both
 * direct movement and animated path traversal.
 * 
 * Each token is represented using an {@link ImageView}, and movement is animated
 * using JavaFX transitions.
 * 
 * @author Hector Mendana Morales
 */
public class PlayerTokenLayer extends Pane {

    private final Map<Player, ImageView> playerTokens = new HashMap<>();
    private final Map<Integer, Integer> cols = new HashMap<>();
    private final Map<Integer, Integer> rows = new HashMap<>();

    /**
     * Constructs the token layer for a given list of players.
     * Initializes visual tokens and maps logical tile numbers to grid positions.
     *
     * @param players the list of players whose tokens should be displayed
     */
    public PlayerTokenLayer(List<Player> players) {
        this.setPrefSize(536, 482);
        this.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        for (Player player : players) {
            ImageView token = new ImageView(new Image(player.getIcon()));
            token.setFitWidth(50);
            token.setFitHeight(50);
            playerTokens.put(player, token);
            this.getChildren().add(token);
        }

        AtomicBoolean movesRight = new AtomicBoolean(false);

        // Map logical tile numbers (1–90) to grid row and column positions
        IntStream.rangeClosed(0, 89).forEach(i -> {
            if ((i % 10) == 0) {
                movesRight.set(!movesRight.get());
            }

            int row = i / 10;
            int col = movesRight.get()
                    ? i % 10
                    : 10 - ((i % 10) + 1);

            cols.put(i + 1, col);
            rows.put(i + 1, row);

            System.out.println("Row: " + row + " Col: " + col + " Tile: " + (i + 1));
        });
    }

    /**
     * Moves a player's token to the given tile number with an animation.
     *
     * @param player the player whose token to move
     * @param tileNumber the target tile number (1-indexed)
     */
    public void moveToken(Player player, int tileNumber) {
        ImageView token = playerTokens.get(player);

        int spacing = 54; // TILE_SIZE (50) + GAP (4)
        int col = cols.get(tileNumber);
        int row = rows.get(tileNumber);

        double targetX = col * spacing;
        double targetY = row * spacing;

        token.setLayoutX(0);
        token.setLayoutY(0);

        TranslateTransition move = new TranslateTransition(Duration.millis(300), token);
        move.setToX(targetX);
        move.setToY(targetY);
        move.play();
    }

    /**
     * Animates a player's token as it moves across multiple tiles from its current position to the end tile.
     * A short delay is introduced between each step to simulate progression.
     *
     * @param player the player to move
     * @param endTile the final tile number to reach
     */
    public void movePlayerThroughPath(Player player, int endTile) {
        int playerPosition = player.getPosition();

        IntStream.rangeClosed(0, endTile - playerPosition).forEach(i -> {
            PauseTransition pause = new PauseTransition(Duration.millis(i * 200));
            pause.setOnFinished(event -> {
                moveToken(player, playerPosition + i);
            });
            pause.play();
        });
    }

}
