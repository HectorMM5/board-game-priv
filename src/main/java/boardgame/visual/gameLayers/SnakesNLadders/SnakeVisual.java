package boardgame.visual.gameLayers.SnakesNLadders;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * A visual representation of a snake for the Snakes and Ladders game.
 * This class draws a vertical snake using basic JavaFX shapes, consisting of
 * a body, a head (skull), and a tongue.
 *
 * The snake is dynamically rotated and positioned by {@link LadderLayer}
 * to connect two tiles visually.
 * 
 * @author Hector Mendana Morales
 */
public class SnakeVisual extends Group {

    /**
     * Constructs a visual snake with a given vertical length.
     *
     * @param length the total height of the snake in pixels
     */
    public SnakeVisual(double length) {
        Circle skull = new Circle();
        skull.setCenterX(25);
        skull.setCenterY(length - 5);
        skull.setRadius(15);
        skull.setFill(Color.ORANGE);

        Rectangle body = new Rectangle();
        body.setY(0);
        body.setX(15);
        body.setWidth(20);
        body.setHeight(length - 15);
        body.setFill(Color.ORANGE);

        Rectangle toungue = new Rectangle();
        toungue.setY(length + 5);
        toungue.setX(22.5);
        toungue.setWidth(5);
        toungue.setHeight(10);
        toungue.setFill(Color.RED); 
        
        this.getChildren().addAll(toungue, skull, body);
    }
}
