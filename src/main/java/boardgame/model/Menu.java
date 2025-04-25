package boardgame.model;

import java.util.ArrayList;

import boardgame.model.boardFiles.Player;
import boardgame.model.boardFiles.Rules;

public class Menu {
    
    private static int playerCount;
    private static Rules rules;
    private static int diceSize;

    public Menu(int playerCount, Rules rules, int diceSize) {
        this.playerCount = playerCount;
        this.rules = rules;
        this.diceSize = diceSize;

    }

    public Rules getRules() {
        return rules;
    }

    public int getPlayers() {
        return playerCount;
    }

    public int getDiceSize() {
        return diceSize;
    }

    public void setRules(Rules gameRules) {
        rules = gameRules;
    }

    public void setPlayers(int gamePlayers) {
        playerCount = gamePlayers;
    }

    public void setDiceSize(int gameDiceSize) {
        diceSize = gameDiceSize;
    }
    
    //Create game button when setup is finished using the UI
    public static void createGame() {
        ArrayList<Player> players = new ArrayList<>();

        for (int i = 0; i < playerCount; i++) {
            String playerName = "";
            players.add(new Player("ICON", playerName));

        }
    }


}