package boardgame.controller;
import java.util.ArrayList;

import boardgame.model.boardFiles.Player;
import boardgame.utils.GameSetup;
import javafx.stage.Stage;

public class SnakesAndLaddersInitController extends GameInitController {

    public SnakesAndLaddersInitController(Stage primaryStage) {
        super(primaryStage);
    }

    @Override
    public void startGame(){
        ArrayList<Player> playerList = this.getCurrentPlayers();
        if (playerList != null && playerList.size() > 1) {
            System.out.println("Starting game with " + playerList.size() + " players.");
            new GameSetup("SnL", 0, playerList).start(this.primaryStage);
        }
        System.out.println("Please select at least 2 players!");
    }

}
