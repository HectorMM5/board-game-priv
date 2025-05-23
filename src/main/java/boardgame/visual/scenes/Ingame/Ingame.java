package boardgame.visual.scenes.Ingame;

import boardgame.controller.RollHandlers.RollHandler;
import javafx.scene.Scene;


/**
 * The main scene handler for the in-game screen.
 * Responsible for initializing all UI elements (board, side column, tokens),
 * and managing gameplay flow such as player movement and dice rolling.
 * <p>
 * This interface defines the contract for an in-game scene, providing access
 * to the scene itself and the roll handler. Implementations of this interface
 * connect the game's logic with the visual layers.
 *
 *  
 */
public interface Ingame {

    /**
     * Returns the JavaFX {@code Scene} representing the in-game view.
     *
     * @return the in-game scene.
     */
    Scene getScene();

    /**
     * Returns the {@code RollHandler} associated with the in-game scene,
     * which manages dice rolling mechanics.
     *
     * @return the roll handler.
     */
    RollHandler getRollHandler();
}