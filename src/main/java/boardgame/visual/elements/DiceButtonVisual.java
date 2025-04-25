package boardgame.visual.elements;

import javafx.geometry.Pos;
import javafx.scene.control.Button;

/**
 * A custom JavaFX button used for rolling dice in the game UI.
 * This button is styled with centered alignment and initialized with
 * the text "Roll dice".
 * 
 * This class can be extended or styled further for visual consistency.
 * 
 * @author Hector Mendana Morales
 */
public class DiceButtonVisual extends Button {

    /**
     * Constructs a DiceButtonVisual instance with default label "Roll dice"
     * and centered alignment.
     */
    public DiceButtonVisual() {
        super("Roll dice");
        this.setAlignment(Pos.CENTER);
    }
}
