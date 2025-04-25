package boardgame.visual.elements;

import java.util.List;

import boardgame.controller.GameController;
import boardgame.model.boardFiles.Player;
import boardgame.visual.scenes.Ingame;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Represents the vertical side panel of the game UI.
 * This panel includes the dice animation, roll button, and player list.
 * 
 * It is used as an interactive and informative sidebar during gameplay.
 * 
 * @author Hector Mendana Morales
 */
public class SideColumnVisual extends VBox {

    private final DiceAnimation diceAnimation;
    private final DiceButtonVisual rollButton;


    /**
     * Constructs the side column visual with a dice roller, roll button,
     * and a display of all players currently in the game.
     *
     * @param gameController the game logic controller
     * @param players the list of players in the game
     * @param ingame reference to the main ingame UI for callbacks
     */
    public SideColumnVisual(GameController gameController, List<Player> players, Ingame ingame) {
        System.out.println("Reached SideColumnVisual with player list size: " + players.size());

        this.setPrefWidth(500);
        this.setSpacing(150);
        this.setBackground(new Background(new BackgroundFill(Color.AQUA, null, null)));

        this.diceAnimation = new DiceAnimation();
        this.rollButton = new DiceButtonVisual();

        rollButton.setOnAction(e -> {
            ingame.handleRollDice(rollButton);
            rollButton.setDisable(true);
        });

        BorderPane diceWrapper = new BorderPane();
        diceWrapper.setCenter(diceAnimation);
        diceWrapper.setMaxWidth(Double.MAX_VALUE);
        diceWrapper.setPrefWidth(500);

        this.getChildren().add(diceWrapper);
        this.getChildren().add(rollButton);
        this.getChildren().add(new PlayerRowsVisual(players).getPlayerRows());

        this.setAlignment(Pos.CENTER);
    }

    /**
     * Re-enables the roll button to allow the player to roll the dice again.
     */
    public void turnOnButton() {
        rollButton.setDisable(false);
    }

    /**
     * Displays the rolled value using the dice animation.
     *
     * @param diceRoll the result of the dice roll
     */
    public void displayRoll(int diceRoll) {
        diceAnimation.displayRoll(diceRoll);
    }

    /**
     * Returns the roll button element.
     *
     * @return the roll button
     */
    public DiceButtonVisual getRollButton() {
        return rollButton;
    }
}
