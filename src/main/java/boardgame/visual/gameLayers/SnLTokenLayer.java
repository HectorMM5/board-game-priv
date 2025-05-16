package boardgame.visual.gameLayers;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import boardgame.model.boardFiles.Player;
import boardgame.utils.movementType;
import boardgame.visual.elements.SnL.SnLBoardVisual;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * A stateless visual layer for animating player tokens on a Snakes and Ladders board.
 * Uses a single global queue to animate token movements one at a time.
 */
public class SnLTokenLayer extends TokenLayer {

    private final Queue<Runnable> animationQueue = new LinkedList<>();
    private boolean isAnimating = false;

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
        }
    }

    /**
     * Instantly moves the player's token to a given tile.
     */
    @Override
    public void moveToken(Player player, int tileNumber) {
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
     * Enqueues multiple moveToken(...) calls, one per tile step.
     * Actual animation is triggered by the moveToken method itself.
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

    private void runNextAnimation() {
        Runnable next = animationQueue.poll();
        if (next != null) {
            next.run(); // This will call moveToken, which sets the animation
        } else {
            isAnimating = false;
        }
    }

    @Override
    public void registerPlayerMove(Player player, int tileNumber, movementType movementType) {
        switch (movementType) {
            case INSTANT -> moveToken(player, tileNumber);
            case PATH -> moveTokenThroughPath(player, tileNumber);
        }

    }
}
