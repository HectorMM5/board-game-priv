package boardgame.visual.gameLayers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import boardgame.controller.LudoGameController;
import boardgame.model.boardFiles.Player;
import boardgame.utils.LudoBoardTiles;
import boardgame.visual.elements.LudoBoardVisual;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class LudoTokenLayer extends TokenLayer {

    private final Map<Color, List<Point>> colorHome = new HashMap<>();

    public LudoTokenLayer(LudoBoardVisual boardVisual, List<Player> players) {
        super(boardVisual, players);

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

        colorHome.put(Color.YELLOW, boardVisual.getYellowHomeTiles());
        colorHome.put(Color.RED, boardVisual.getRedHomeTiles());
        colorHome.put(Color.BLUE, boardVisual.getBlueHomeTiles());
        colorHome.put(Color.GREEN, boardVisual.getGreenHomeTiles());
    }

    @Override
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

        Integer col = houseList.get(tileNumber).x;
        Integer row = houseList.get(tileNumber).y;

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

        System.out.println("CURRENT POSITION = " + currentPosition);

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
    @Override
    public void moveTokenThroughPath(Player player, int endTile) {
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

    public void moveToGoal(Player player) {
        ImageView token = playerTokens.get(player);

        double targetX = 7 * boardVisual.getSpacing();
        double targetY = 7 * boardVisual.getSpacing();

        TranslateTransition move = new TranslateTransition(Duration.millis(300), token);
        move.setToX(targetX);
        move.setToY(targetY);
        move.play();


    }

}
