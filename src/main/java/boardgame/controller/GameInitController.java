package boardgame.controller;

import java.util.List;

import boardgame.model.Player;
import boardgame.utils.GameFactory;
import boardgame.utils.GameType;

public class GameInitController {
    
    public static void handleGameStart(GameType gameType, int boardVariant, List<Player> players) {
        new GameFactory(gameType, boardVariant, players).startGame();
        
    }

}
