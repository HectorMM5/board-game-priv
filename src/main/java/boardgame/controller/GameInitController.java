package boardgame.controller;

import java.util.List;

import boardgame.model.boardFiles.Player;
import boardgame.utils.GameSetup;

public class GameInitController {
    
    public static void handleGameStart(String chosenGame, int boardVariant, List<Player> players) {
        new GameSetup(chosenGame, boardVariant, players).startGame();
        
    }

}
