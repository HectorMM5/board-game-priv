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
public final class SnLTokenLayer extends TokenLayer {

    private final Queue<Runnable> animationQueue = new LinkedList<>();
    private boolean isAnimating = false;

    private final Map<Player, Integer> positions = new HashMap<>();

    /**
     * Constructs a new {@code SnLTokenLayer}.
     *
     * @param boardVisual the visual representation of the Snakes and Ladders board.
     * @param players     the list of players in the game.
     */
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

        players.stream().forEach(player -> {
            ImageView token = new ImageView(new Image(player.getIcon()));
            token.setFitWidth(50);
            token.setFitHeight(50);
            token.setLayoutX(spacing / 2 - 25);
            token.setLayoutY(spacing / 2 - 25);
            token.setTranslateY(8 * spacing);

            playerTokens.put(player, token);
            this.getChildren().add(token);

            positions.put(player, 1);
        });

        refreshTokenSizesAndPositions();
    }

    /**
     * Instantly moves the player's token to a given tile with a small animation.
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
     * Enqueues multiple {@code moveToken(...)} calls, one per tile step, to animate movement.
     * The actual animation is triggered sequentially.
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

    /**
     * Executes the next animation in the queue. If the queue is empty, it refreshes
     * token sizes and positions and resets the animating flag.
     */
    public void runNextAnimation() {
        Runnable next = animationQueue.poll();
        if (next != null) {
            next.run(); // This will call moveToken, which sets the animation
        } else {
            refreshTokenSizesAndPositions();
            isAnimating = false;
        }
    }

    /**
     * Registers a player's move and adds a delayed instant move animation to the queue.
     *
     * @param player        the player making the move.
     * @param tileNumber    the target tile number.
     * @param movementType  the type of movement (INSTANT or PATH).
     */
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


    /**
     * Calculates the offset for tokens occupying the same tile to prevent overlap.
     *
     * @param tokenCount the number of tokens on the tile.
     * @return a 2D array of x and y offsets for each token.
     */
    private static double[][] getTokenOffsets(int tokenCount) {
        return switch (tokenCount) {
            case 2 -> new double[][]{{-0.1, -0.1}, {0.35, 0.35}};
            case 3 -> new double[][]{{-0.1, -0.1}, {0.125, 0.125}, {0.35, 0.35}};
            case 4 -> new double[][]{{-0.1, -0.1}, {-0.1, 0.35}, {0.35, -0.1}, {0.35, 0.35}};
            case 5 -> new double[][]{{-0.1, -0.1}, {-0.1, 0.35}, {0.125, 0.125}, {0.35, -0.1}, {0.35, 0.35}};
            default -> new double[][]{{0, 0}};
        };
    }

    /**
     * Adjusts the size and position of tokens on the board to handle multiple
     * tokens on the same tile.
     */
    public void refreshTokenSizesAndPositions() {
        double spacing = boardVisual.getSpacing();

        List<List<Player>> playerGroups = positions.entrySet().stream()
                .collect(Collectors.groupingBy(
                        Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())
                ))
                .values().stream()
                .toList();

        playerGroups.forEach(group -> {
            int tokenCount = group.size();
            double[][] offsets = getTokenOffsets(tokenCount);

            IntStream.range(0, tokenCount).forEach(i -> {
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
            });
        });
    }

    /**
     * Resets the size of a player's token to its default.
     *
     * @param player the player whose token size to reset.
     */
    public void resetTokenSize(Player player) {
        ImageView token = playerTokens.get(player);

        token.setFitWidth(50);
        token.setFitHeight(50);
    }

    /**
     * Adds an animation to the animation queue to be executed sequentially.
     *
     * @param animation the {@code Runnable} representing the animation.
     */
    public void addToAnimationQueue(Runnable animation) {
        animationQueue.add(animation);
        if (!isAnimating) {
            isAnimating = true;
            runNextAnimation();
        }
    }

}