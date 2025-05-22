package boardgame.visual.elements.SideColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Visual representation of a six-sided die animation. It displays a sequence
 * of random rolls before settling on the final result.
 */
public class DiceAnimation extends GridPane {

    private List<Circle> points;
    private final Random random = new Random();

    /**
     * Constructs a new {@code DiceAnimation}.
     */
    public DiceAnimation() {
        this.points = new ArrayList<>(9);

        this.setHgap(0);
        this.setVgap(0);
        this.setPadding(Insets.EMPTY);
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(100);
        this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        this.setStyle("-fx-background-color: white;");

        IntStream.rangeClosed(0, 8)
                .forEach(i -> {
                    StackPane die = new StackPane();
                    Rectangle background = new Rectangle(75, 75, Color.WHITE);
                    background.setStroke(null);

                    Circle circle = new Circle(20, Color.WHITE);
                    points.add(circle);

                    die.getChildren().addAll(background, circle);
                    this.add(die, i % 3, i / 3);
                });

    }

    /**
     * Initiates the dice roll animation, displaying a sequence of random
     * values before showing the final roll result.
     *
     * @param finalRoll the result of the dice roll to be displayed at the end
     * of the animation.
     */
    public void displayRoll(int finalRoll) {
        Timeline timeline = new Timeline();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            int fakeRoll = random.nextInt(6) + 1;

            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * 50), e -> {
                displayFace(fakeRoll);
            });

            timeline.getKeyFrames().add(keyFrame);

        });

        KeyFrame finalKeyFrame = new KeyFrame(Duration.millis(500), e -> {
            displayFace(finalRoll);
        });

        timeline.getKeyFrames().add(finalKeyFrame);
        timeline.play();
    }

    /**
     * Updates the visual representation of the die to show the face corresponding
     * to the given roll value.
     *
     * @param roll the value of the die roll (between 1 and 6).
     * @throws IllegalArgumentException if the roll value is not between 1 and 6.
     */
    public void displayFace(int roll) {
        points.forEach(c -> c.setFill(Color.WHITE));

        switch (roll) {
            case 1 ->
                    setPips(4);
            case 2 ->
                    setPips(0, 8);
            case 3 ->
                    setPips(0, 4, 8);
            case 4 ->
                    setPips(0, 2, 6, 8);
            case 5 ->
                    setPips(0, 2, 4, 6, 8);
            case 6 ->
                    setPips(0, 2, 3, 5, 6, 8);
            default ->
                    throw new IllegalArgumentException("Roll must be between 1 and 6");
        }
    }

    /**
     * Sets the pips (black circles) on the die face at the specified indices.
     *
     * @param indices the indices of the points to be displayed as pips (0-8,
     * corresponding to the 3x3 grid).
     */
    private void setPips(int... indices) {
        IntStream.of(indices).forEach(i -> points.get(i).setFill(Color.BLACK));
    }

}