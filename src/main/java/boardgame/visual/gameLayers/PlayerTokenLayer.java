package boardgame.visual.gameLayers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import boardgame.controller.LudoGameController;
import boardgame.model.boardFiles.Player;
import boardgame.model.boardFiles.Tile;
import boardgame.utils.GameType;
import boardgame.utils.LudoBoardTiles;
import boardgame.visual.elements.BoardVisual;
import boardgame.visual.elements.LudoBoardVisual;
import boardgame.visual.elements.SnL.SnLBoardVisual;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * A visual layer used to display and animate player tokens on top of the board.
 * This class calculates tile positions based on a zig-zag layout and supports
 * both direct movement and animated path traversal.
 *
 * Each token is represented using an {@link ImageView}, and movement is
 * animated using JavaFX transitions.
 *
 * @author Hector Mendana Morales
 */
public class PlayerTokenLayer extends Pane {

    private final Map<Player, ImageView> playerTokens = new HashMap<>();
    private final Map<Integer, Integer> cols = new HashMap<>();
    private final Map<Integer, Integer> rows = new HashMap<>();
    private final BoardVisual boardVisual;

    Map<Color, List<Tile>> colorToHouseTiles = Map.of(
            Color.YELLOW, LudoGameController.getYellowHomeTiles(),
            Color.RED, LudoGameController.getRedHomeTiles(),
            Color.BLUE, LudoGameController.getBlueHomeTiles(),
            Color.GREEN, LudoGameController.getGreenHomeTiles()
    );

    private final Map<Color, List<Point>> colorHome = new HashMap<>();

    /**
     * Constructs the token layer for a given list of players. Initializes
     * visual tokens and maps logical tile numbers to grid positions.
     *
     * @param players the list of players whose tokens should be displayed
     */
    public PlayerTokenLayer(GameType gameType, BoardVisual boardVisual, List<Player> players) {
        this.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: transparent;");
        this.boardVisual = boardVisual;
        this.prefWidthProperty().bind(boardVisual.getTileGrid().widthProperty());
        this.prefHeightProperty().bind(boardVisual.getTileGrid().heightProperty());

        switch (gameType) {
            case SnakesNLadders -> {
                for (Player player : players) {
                    ImageView token = new ImageView(new Image(player.getIcon()));
                    token.setFitWidth(50);
                    token.setFitHeight(50);
                    token.setLayoutX(((SnLBoardVisual) boardVisual).getSpacing() / 2 - 25);
                    token.setLayoutY(((SnLBoardVisual) boardVisual).getSpacing() / 2 - 25);
                    playerTokens.put(player, token);
                    this.getChildren().add(token);
                }

                AtomicBoolean movesRight = new AtomicBoolean(false);

                // Map logical tile numbers (1–90) to grid row and column positions
                IntStream.rangeClosed(0, 89).forEach(i -> {
                    if ((i % 10) == 0) {
                        movesRight.set(!movesRight.get());
                    }

                    Integer row = i / 10;
                    Integer col = movesRight.get()
                            ? i % 10
                            : 10 - ((i % 10) + 1);

                    cols.put(i + 1, col);
                    rows.put(i + 1, row);

                });
            }

            case Ludo -> {
                List<Point> playableTileCoordinates = LudoBoardTiles.getPlayableTiles();

                IntStream.rangeClosed(1, playableTileCoordinates.size()).forEach(i -> {
                    cols.put(i, playableTileCoordinates.get(i - 1).x);
                    rows.put(i, playableTileCoordinates.get(i - 1).y);

                });

                Map<Color, Integer> colorStartPositions = LudoBoardTiles.getColorStartPositions();

                List<Color> colors = new ArrayList<>(List.of(Color.YELLOW, Color.RED, Color.BLUE, Color.GREEN));

                IntStream.range(0, players.size()).forEach(i -> {
                    Player player = players.get(i);
                    ImageView token = new ImageView(new Image(player.getIcon()));
                    token.setFitWidth(50);
                    token.setFitHeight(50);

                    playerTokens.put(player, token);
                    this.getChildren().add(token);

                    moveToken(player, colorStartPositions.get(colors.get(i)));
                
                });

                colorHome.put(Color.YELLOW, ((LudoBoardVisual) boardVisual).getYellowHomeTiles());
                colorHome.put(Color.RED, ((LudoBoardVisual) boardVisual).getRedHomeTiles());
                colorHome.put(Color.BLUE, ((LudoBoardVisual) boardVisual).getBlueHomeTiles());
                colorHome.put(Color.GREEN, ((LudoBoardVisual) boardVisual).getGreenHomeTiles());
            }
        }
    }

    /**
     * Moves a player's token to the given tile number with an animation.
     *
     * @param player the player whose token to move
     * @param tileNumber the target tile number (1-indexed)
     */
    public void moveToken(Player player, int tileNumber) {
        ImageView token = playerTokens.get(player);

        Integer col = cols.get(tileNumber);
        Integer row = rows.get(tileNumber);

        double targetX = col * boardVisual.getSpacing();
        double targetY = row * boardVisual.getSpacing();

        TranslateTransition move = new TranslateTransition(Duration.millis(300), token);
        move.setToX(targetX);
        move.setToY(targetY);
        move.play();
    }

    public void moveTokenThroughHome(Player player, Color color, int tileNumber) {
        ImageView token = playerTokens.get(player);
        List<Point> houseList = colorHome.get(color);


        Integer col = houseList.get(tileNumber - 1).x;
        Integer row = houseList.get(tileNumber - 1).y;

        double targetX = col * boardVisual.getSpacing();
        double targetY = row * boardVisual.getSpacing();

        TranslateTransition move = new TranslateTransition(Duration.millis(300), token);
        move.setToX(targetX);
        move.setToY(targetY);
        move.play();

    }

    public void movePlayerThroughHomePath(Player player, Color color, int stopTile, LudoGameController gameController) {
        // Find current position in home path (0 if not yet inside)
        int currentPosition = gameController.getHomePosition().get(player);
    
        IntStream.range(currentPosition, stopTile).forEach(i -> {
            PauseTransition pause = new PauseTransition(Duration.millis((i - currentPosition) * 300));
            pause.setOnFinished(event -> moveTokenThroughHome(player, color, i));
            pause.play();
        });
    }
    

    /**
     * Animates a player's token as it moves across multiple tiles from its
     * current position to the end tile. A short delay is introduced between
     * each step to simulate progression.
     *
     * @param player the player to move
     * @param endTile the final tile number to reach
     */
    public void movePlayerThroughPath(Player player, int endTile) {
        int playerPosition = player.getPosition();

        IntStream.rangeClosed(0, endTile - playerPosition).forEach(i -> {
            PauseTransition pause = new PauseTransition(Duration.millis(i * 300));
            pause.setOnFinished(event -> {
                int nextPosition = playerPosition + i;
                if (nextPosition > 56) {
                    nextPosition -= 56;
                }

                moveToken(player, nextPosition);
            });
            pause.play();
        });
    }

}
