package boardgame.controller.GameBuilding;

import java.util.List;

import boardgame.controller.PlayerDataAccess;
import boardgame.model.boardFiles.Player;
import boardgame.utils.GameSetup;

public class GameInitController {

    private final PlayerDataAccess playerDataAccess = new PlayerDataAccess();

    public GameInitController() {

    }

    public void getPlayerData() {
        
    }

    public static void handleGameStart(String chosenGame, int boardVariant, List<Player> players) {
        new GameSetup(chosenGame, boardVariant, players).startGame();
        
        
    }

    

}
