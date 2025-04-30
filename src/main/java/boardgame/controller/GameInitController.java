package boardgame.controller;

import java.util.List;

import boardgame.model.boardFiles.Player;
import boardgame.utils.GameSetup;
import boardgame.utils.GameType;

public class GameInitController {
    
    public static void handleGameStart(GameType gameType, int boardVariant, List<Player> players) {
        new GameSetup(gameType, boardVariant, players).startGame();
        
    }

}
