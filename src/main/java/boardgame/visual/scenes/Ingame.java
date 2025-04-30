package boardgame.visual.scenes;

import javafx.scene.Scene;


/**
 * The main scene handler for the in-game screen.
 * Responsible for initializing all UI elements (board, side column, tokens),
 * and managing gameplay flow such as player movement and dice rolling.
 *
 * This class connects the game's logic (GameController) with the visual layers.
 * Now uses IngameController to separate gameplay logic from scene setup.
 * 
 * @author Hector Mendana Morales
 */
public interface Ingame {

    public Scene getScene();

    public IngameController getIngameController();
}
