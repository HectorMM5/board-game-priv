import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boardgame.controller.GameControllers.LudoGameController;
import boardgame.model.boardFiles.Ludo.LudoBoard;
import boardgame.model.boardFiles.Player;
import boardgame.utils.movementType;
import javafx.scene.paint.Color;

public class LudoGCTest {

    private LudoGameController controller;
    private LudoBoard board;
    private List<Player> players;

    @BeforeEach
    public void setup() {
        board = new LudoBoard();
        players = new ArrayList<>();
        players.add(new Player("Alice", "icon1.png"));
        players.add(new Player("Bob", "icon2.png"));
        players.add(new Player("Charlie", "icon3.png"));
        players.add(new Player("Diana", "icon4.png"));
        controller = new LudoGameController(board, players);
        controller.start();
    }

    @Test
    public void testMovePlayer() {
        Player player = players.get(0);

        //Moves player to tile 5
        controller.movePlayer(player, 5, movementType.PATH);

        //Due to 0-indexing, player should be found in tile 4
        assertEquals(5, player.getPosition());
        assertTrue(board.getTiles().get(4).getPlayers().contains(player));
    }

    @Test
    public void testMovePlayerWrapAround() {
        Player player = players.get(0);

        //Moves player to a tile beyond 56, which should wrap
        controller.movePlayer(player, 60, movementType.PATH); // wraps to 4

        //Move to 60 means four tiles beyond 56, therefore player should be found in tile 4
        assertEquals(4, player.getPosition());
        assertTrue(board.getTiles().get(3).getPlayers().contains(player));
    }

    @Test
    public void testMovePlayerReplacesOldPosition() {
        Player player = players.get(0);
        int oldPosition = player.getPosition();

        //Moving player should remove them from the old position
        //In other words, the player should not have been "cloned"
        controller.movePlayer(player, 10, movementType.PATH);
        assertFalse(board.getTiles().get(oldPosition - 1).getPlayers().contains(player));
        assertTrue(board.getTiles().get(9).getPlayers().contains(player));
    }

    @Test
    public void testMovePlayerThroughHomeInitial() {
        Player player = players.get(0);

        //Initial position in home is 0
        assertEquals(0, controller.getHomePosition().get(player));

        //Moves player to home tile 1
        controller.movePlayerThroughHome(player, 1);

        //After moving through home, the position should be 1
        assertEquals(1, controller.getHomePosition().get(player));
    }

    @Test
    public void testMovePlayerThroughHomeProgress() {
        Player player = players.get(0);

        //Initial position in home is 0
        assertEquals(0, controller.getHomePosition().get(player));
        
        controller.movePlayerThroughHome(player, 1);
        //After moving through home, the position should be 1
        assertEquals(1, controller.getHomePosition().get(player));

        controller.movePlayerThroughHome(player, 3);
        //After moving two more steps, the position should be 3
        assertEquals(3, controller.getHomePosition().get(player));
    }

    @Test
    public void testMovePlayerThroughHomeByNormal() {
        Player player = players.get(0);

        //Should be moved to home tile 2
        controller.movePlayerThroughHome(player, 2); 

        //Then moved by 2 more steps - thus should land at 4
        controller.movePlayerThroughHomeBy(player, 2);
        assertEquals(4, controller.getHomePosition().get(player));
    }

    @Test
    public void testMovePlayerThroughHomeByOverflow() {
        Player player = players.get(0);

        //Attempt to move player to home tile beyond 6
        controller.movePlayerThroughHome(player, 5); 
        controller.movePlayerThroughHomeBy(player, 3);

        //Should be capped at 6 (game end screen would be shown after a short animation delay)
        assertEquals(6, controller.getHomePosition().get(player));
    }

    @Test
    public void testDisablePlayerOnBoard() {
        Player player = players.get(0);

        //Moves player to tile 10, then dissables them
        controller.movePlayer(player, 10, movementType.PATH);
        controller.disablePlayerOnBoard(player);

        //Checks if player is removed from the board
        assertFalse(board.getTiles().get(9).getPlayers().contains(player));
    }

    @Test
    public void testGetPlayerColor() {
        Map<Player, Color> colorMap = controller.getPlayerColor();
        //Player color assignment is based on the order of players
        //Should follow: YELLOW, RED, BLUE, GREEN
        assertEquals(Color.YELLOW, colorMap.get(players.get(0)));
        assertEquals(Color.RED, colorMap.get(players.get(1)));        
        assertEquals(Color.BLUE, colorMap.get(players.get(2)));
        assertEquals(Color.GREEN, colorMap.get(players.get(3)));
    }

    @Test
    public void testEdgeCaseMoveTo56() {
        Player player = players.get(0);

        //Moves player to tile 56
        //This is the last tile before wrap
        controller.movePlayer(player, 56, movementType.PATH);
        assertEquals(56, player.getPosition());
        assertTrue(board.getTiles().get(55).getPlayers().contains(player));
    }

    @Test
    public void testEdgeCaseMoveTo1AfterWrap() {
        Player player = players.get(0);

        //Moves player to tile 57, which should wrap to 1
        controller.movePlayer(player, 57, movementType.PATH); // wraps to 1
        assertEquals(1, player.getPosition());
        assertTrue(board.getTiles().get(0).getPlayers().contains(player));
    }
}
