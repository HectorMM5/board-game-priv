package boardgame.visual.gameLayers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import boardgame.controller.GameControllers.LudoGameController;
import boardgame.model.Player;
import boardgame.utils.LudoBoardTiles;
import boardgame.utils.movementType;
import boardgame.visual.elements.LudoBoardVisual;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Visual layer for Ludo game tokens, managing their placement and movement
 * on the Ludo board.
 */
public class LudoTokenLayer extends TokenLayer {

    private final Map<Color, List<Point>> colorHome = new HashMap<>();
    private final Map<Player, Integer> positions = new HashMap<>();

    private final Queue<Runnable> animationQueue = new LinkedList<>();
    private boolean isAnimating = false;

    private final Map<Player, Color> playerColors = new HashMap<>();

    private static final Map<Color, Integer> homeEntryTiles = Map.of(
            Color.YELLOW, 40,
            Color.RED, 54,
            Color.BLUE, 12,
            Color.GREEN, 26
    );

    /**
     * Constructs a new {@code LudoTokenLayer}.
     *
     * @param boardVisual the visual representation of the Ludo board.
     * @param players     the list of players in the game.
     */
    public LudoTokenLayer(LudoBoardVisual boardVisual, List<Player> players) {
        super(boardVisual, players);

        List<Point> playableTileCoordinates = LudoBoardTiles.getPlayableTiles();

        IntStream.rangeClosed(1, playableTileCoordinates.size()).forEach(i -> {
            cols.put(i, playableTileCoordinates.get(i - 1).x);
            rows.put(i, playableTileCoordinates.get(i - 1).y);
        });

        Map<Color, Integer> colorStartPositions = LudoBoardTiles.getColorStartPositions();

        List<Color> colors = new ArrayList<>(List.of(Color.YELLOW, Color.RED, Color.BLUE, Color.GREEN));

        double spacing = boardVisual.getSpacing();

        IntStream.range(0, players.size()).forEach(i -> {
            Player player = players.get(i);
            Color color = colors.get(i);
            playerColors.put(player, color); // Store player color
            ImageView token = new ImageView(new Image(player.getIcon()));
            token.setFitWidth(50);
            token.setFitHeight(50);

            playerTokens.put(player, token);
            this.getChildren().add(token);

            Integer startPosition = colorStartPositions.get(colors.get(i));
            int x = cols.get(startPosition);
            int y = rows.get(startPosition);
            token.setTranslateX(x * spacing);
            token.setTranslateY(y * spacing);

            positions.put(player, colorStartPositions.get(colors.get(i)));

            ImageView startPortrait = new ImageView(new Image(player.getIcon()));
            startPortrait.setFitWidth(spacing * 3);
            startPortrait.setFitHeight(spacing * 3);

            switch (i) {
                case 0 -> {
                    startPortrait.setTranslateX(spacing * 1);
                    startPortrait.setTranslateY(spacing * 1);
                }

                case 1 -> {
                    startPortrait.setTranslateX(spacing * 11);
                    startPortrait.setTranslateY(spacing * 1);
                }
                case 2 -> {
                    startPortrait.setTranslateX(spacing * 11);
                    startPortrait.setTranslateY(spacing * 11);
                }

                case 3 -> {
                    startPortrait.setTranslateX(spacing * 1);
                    startPortrait.setTranslateY(spacing * 11);
                }

            }

            this.getChildren().add(startPortrait);

        });

        colorHome.put(Color.YELLOW, boardVisual.getYellowHomeTiles());
        colorHome.put(Color.RED, boardVisual.getRedHomeTiles());
        colorHome.put(Color.BLUE, boardVisual.getBlueHomeTiles());
        colorHome.put(Color.GREEN, boardVisual.getGreenHomeTiles());
    }

    /**
     * Moves a player's token to a specific tile number on the main path.
     *
     * @param player     the player whose token to move.
     * @param tileNumber the target tile number.
     */
    @Override
    public void moveToken(Player player, int tileNumber) {
        resetTokenSize(player);
        positions.replace(player, tileNumber);

        ImageView token = playerTokens.get(player);

        Integer col = cols.get(tileNumber);
        Integer row = rows.get(tileNumber);

        double targetX = col * boardVisual.getSpacing();
        double targetY = row * boardVisual.getSpacing();

        TranslateTransition move = new TranslateTransition(Duration.millis(300), token);
        move.setToX(targetX);
        move.setToY(targetY);
        move.setOnFinished(e -> runNextAnimation());
        move.play();
    }

    /**
     * Moves a player's token through their home path to a specific tile number within the home path.
     *
     * @param player     the player whose token to move.
     * @param color      the color of the player, used to determine the home path.
     * @param tileNumber the target tile number within the home path (0-indexed).
     */
    public void moveTokenThroughHome(Player player, Color color, int tileNumber) {
        resetTokenSize(player);
        ImageView token = playerTokens.get(player);
        List<Point> houseList = colorHome.get(color);

        Integer col = houseList.get(tileNumber).x;
        Integer row = houseList.get(tileNumber).y;

        double targetX = col * boardVisual.getSpacing();
        double targetY = row * boardVisual.getSpacing();

        TranslateTransition move = new TranslateTransition(Duration.millis(300), token);
        move.setToX(targetX);
        move.setToY(targetY);
        move.setOnFinished(e -> runNextAnimation());
        move.play();

    }

    /**
     * Animates a player's token moving through their home path.
     *
     * @param player         the player whose token to move.
     * @param color          the color of the player, used to determine the home path.
     * @param stopTile       the final tile number to reach within the home path (exclusive).
     * @param gameController the Ludo game controller to access game state.
     */
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
     * Animates a player's token moving across multiple tiles on the main path.
     * A short delay is introduced between each step.
     *
     * @param player  the player to move.
     * @param endTile the final tile number to reach on the main path.
     */
    @Override
    public void moveTokenThroughPath(Player player, int endTile) {
        int playerPosition = player.getPosition();

        int adjustedNextPosition = endTile < playerPosition ? endTile + 56 : endTile;

        IntStream.rangeClosed(0, Math.abs(adjustedNextPosition - playerPosition)).forEach(i -> {

            AtomicInteger nextTile = new AtomicInteger(playerPosition + i);
            if (nextTile.get() > 56) {
                nextTile.set(nextTile.get() - 56);
            }

            animationQueue.add(() -> moveToken(player, nextTile.get()));

        });

        if (!isAnimating) {
            isAnimating = true;
            runNextAnimation();
        }
    }

    /**
     * Moves a player's token to the goal tile.
     *
     * @param player the player whose token to move.
     */
    public void moveToGoal(Player player) {
        ImageView token = playerTokens.get(player);

        double targetX = 7 * boardVisual.getSpacing();
        double targetY = 7 * boardVisual.getSpacing();

        TranslateTransition move = new TranslateTransition(Duration.millis(300), token);
        move.setToX(targetX);
        move.setToY(targetY);
        move.play();

    }

    /**
     * Executes the next animation in the queue. If the queue is empty, it resets the animation flag
     * and refreshes token sizes and positions.
     */
    public void runNextAnimation() {
        Runnable next = animationQueue.poll();
        if (next != null) {
            next.run(); // This will call moveToken, which sets the animation
        } else {
            isAnimating = false;
            refreshTokenSizesAndPositions();
        }
    }

    /**
     * Registers a player's move and initiates the corresponding visual animation.
     *
     * @param player        the player who made the move.
     * @param tileNumber    the target tile number.
     * @param movementType  the type of movement (INSTANT or PATH).
     */
    @Override
    public void registerPlayerMove(Player player, int tileNumber, movementType movementType) {
        switch (movementType) {
            case INSTANT -> {
                animationQueue.add(() -> moveToken(player, tileNumber));
                if (!isAnimating) {
                    isAnimating = true;
                    runNextAnimation(); // ✅ Kick off the queue
                }
            }

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
            case 2 ->
                    new double[][]{{-0.1, -0.1}, {0.35, 0.35}};
            case 3 ->
                    new double[][]{{-0.1, -0.1}, {0.125, 0.125}, {0.35, 0.35}};
            case 4 ->
                    new double[][]{{-0.1, -0.1}, {-0.1, 0.35}, {0.35, -0.1}, {0.35, 0.35}};
            case 5 ->
                    new double[][]{{-0.1, -0.1}, {-0.1, 0.35}, {0.125, 0.125}, {0.35, -0.1}, {0.35, 0.35}};
            default ->
                    new double[][]{{0, 0}};
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

        for (List<Player> group : playerGroups) {
            int tokenCount = group.size();
            double[][] offsets = getTokenOffsets(tokenCount);

            for (int i = 0; i < tokenCount; i++) {
                Player player = group.get(i);
                ImageView token = playerTokens.get(player);

                int pos = positions.get(player);
                Color color = playerColors.get(player);

                Integer homeEntry = homeEntryTiles.get(color);
                if (homeEntry != null && pos == homeEntry) {
                    continue; // Don't move token if it's at the home path entry
                }

                Integer col = cols.get(pos);
                Integer row = rows.get(pos);

                double baseX = col * spacing;
                double baseY = row * spacing;

                if (tokenCount > 1) {
                    token.setFitWidth(25);
                    token.setFitHeight(25);
                } else {
                    token.setFitWidth(50);
                    token.setFitHeight(50);
                }

                double offsetX = offsets[i][0] * spacing;
                double offsetY = offsets[i][1] * spacing;

                token.setTranslateX(baseX + offsetX);
                token.setTranslateY(baseY + offsetY);
            }
        }
    }


    /**
     * Resets a player's token to its standard size and its current position.
     *
     * @param player the player whose token to reset.
     */
    public void resetTokenSizeAndPosition(Player player) {
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

    /**
     * Resets a player's token to its standard size.
     *
     * @param player the player whose token size to reset.
     */
    public void resetTokenSize(Player player) {
        ImageView token = playerTokens.get(player);

        token.setFitWidth(50);
        token.setFitHeight(50);

    }

    /**
     * Adds an animation to the animation queue to be played sequentially.
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