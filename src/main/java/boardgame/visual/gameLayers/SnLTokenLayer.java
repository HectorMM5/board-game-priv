package boardgame.visual.gameLayers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import boardgame.model.Player;
import boardgame.utils.movementType;
import boardgame.visual.elements.SnL.SnLBoardVisual;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * A stateless visual layer for animating player tokens on a Snakes and Ladders
 * board. Uses a single global queue to animate token movements one at a time.
 */
public class SnLTokenLayer extends TokenLayer {

    private final Queue<Runnable> animationQueue = new LinkedList<>();
    private boolean isAnimating = false;

    private final Map<Player, Integer> positions = new HashMap<>();

    public SnLTokenLayer(SnLBoardVisual boardVisual, List<Player> players) {
        super(boardVisual, players);

        AtomicBoolean movesRight = new AtomicBoolean(false);

        IntStream.rangeClosed(0, 89).forEach(i -> {
            if ((i % 10) == 0) {
                movesRight.set(!movesRight.get());
            }

            int row = 8 - (i / 10);
            int col = movesRight.get() ? i % 10 : 10 - ((i % 10) + 1);

            cols.put(i + 1, col);
            rows.put(i + 1, row);
        });

        double spacing = boardVisual.getSpacing();

        for (Player player : players) {
            ImageView token = new ImageView(new Image(player.getIcon()));
            token.setFitWidth(50);
            token.setFitHeight(50);
            token.setLayoutX(spacing / 2 - 25);
            token.setLayoutY(spacing / 2 - 25);
            token.setTranslateY(8 * spacing);

            playerTokens.put(player, token);
            this.getChildren().add(token);

            positions.put(player, 1);
        }
    }

    /**
     * Instantly moves the player's token to a given tile.
     */
    @Override
    public void moveToken(Player player, int tileNumber) {
        resetTokenSize(player);

        positions.replace(player, tileNumber);

        ImageView token = playerTokens.get(player);

        int col = cols.get(tileNumber);
        int row = rows.get(tileNumber);

        double targetX = col * boardVisual.getSpacing();
        double targetY = row * boardVisual.getSpacing();

        TranslateTransition move = new TranslateTransition(Duration.millis(300), token);
        move.setToX(targetX);
        move.setToY(targetY);
        move.setOnFinished(e -> runNextAnimation());
        move.play();


    }

    /**
     * Enqueues multiple moveToken(...) calls, one per tile step. Actual
     * animation is triggered by the moveToken method itself.
     */
    @Override
    public void moveTokenThroughPath(Player player, int endTile) {
        int startTile = player.getPosition();
        for (int tile = startTile; tile <= endTile; tile++) {
            final int nextTile = tile;
            animationQueue.add(() -> moveToken(player, nextTile));
        }

        if (!isAnimating) {
            isAnimating = true;
            runNextAnimation();
        }
    }

    public void runNextAnimation() {
        Runnable next = animationQueue.poll();
        if (next != null) {
            next.run(); // This will call moveToken, which sets the animation
        } else {
            refreshTokenSizesAndPositions();
            isAnimating = false;
        }
    }

    @Override
    public void registerPlayerMove(Player player, int tileNumber, movementType movementType) {
        switch (movementType) {
            case INSTANT ->
                animationQueue.add(() -> {
                    PauseTransition pause = new PauseTransition(Duration.millis(400));
                    pause.setOnFinished(e -> moveToken(player, tileNumber));
                    pause.play();

                });

            case PATH ->
                moveTokenThroughPath(player, tileNumber);
        }

    }


    private static double[][] getTokenOffsets(int tokenCount) {
    return switch (tokenCount) {
      case 2 -> new double[][] {{-0.1, -0.1}, {0.35, 0.35}};
      case 3 -> new double[][] {{-0.1, -0.1}, {0.125, 0.125}, {0.35, 0.35}};
      case 4 -> new double[][] {{-0.1, -0.1}, {-0.1, 0.35}, {0.35, -0.1}, {0.35, 0.35}};
      case 5 -> new double[][] {{-0.1, -0.1}, {-0.1, 0.35}, {0.125, 0.125}, {0.35, -0.1}, {0.35, 0.35}};
      default -> new double[][] {{0, 0}};
    };
  }

    public void refreshTokenSizesAndPositions() {
    double spacing = boardVisual.getSpacing();

    List<List<Player>> playerGroups = positions.entrySet().stream()
            .collect(Collectors.groupingBy(
                    Map.Entry::getValue,
                    Collectors.mapping(Map.Entry::getKey, Collectors.toList())
            ))
            .values().stream()
            .toList();

    for (List<Player> group : playerGroups) {
        int tokenCount = group.size();
        double[][] offsets = getTokenOffsets(tokenCount);

        for (int i = 0; i < tokenCount; i++) {
            Player player = group.get(i);
            ImageView token = playerTokens.get(player);

            int tile = positions.get(player);
            int col = cols.get(tile);
            int row = rows.get(tile);

            double baseX = col * spacing;
            double baseY = row * spacing;

            // Size
            if (tokenCount > 1) {
                token.setFitWidth(25);
                token.setFitHeight(25);
            } else {
                token.setFitWidth(50);
                token.setFitHeight(50);
            }

            // Offset from normalized 0..1 range scaled by spacing
            double offsetX = offsets[i][0] * spacing;
            double offsetY = offsets[i][1] * spacing;

            token.setTranslateX(baseX + offsetX);
            token.setTranslateY(baseY + offsetY);
        }
    }
}


    public void resetTokenSize(Player player) {
    double spacing = boardVisual.getSpacing();
    ImageView token = playerTokens.get(player);
    int tile = positions.get(player);

    int col = cols.get(tile);
    int row = rows.get(tile);

    token.setFitWidth(50);
    token.setFitHeight(50);

    token.setTranslateX(col * spacing);
    token.setTranslateY(row * spacing);
}

public void addToAnimationQueue(Runnable animation) {
        animationQueue.add(animation);
        if (!isAnimating) {
            isAnimating = true;
            runNextAnimation(); // ✅ Kick off the queue
        }
    }

}
