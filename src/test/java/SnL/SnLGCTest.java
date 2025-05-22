package SnL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boardgame.controller.GameControllers.SnLGameController;
import boardgame.model.Player;
import boardgame.model.boardFiles.SnLBoard;
import boardgame.model.boardFiles.Tile;
import boardgame.model.effectFiles.SnakeEffect;
import boardgame.utils.movementType;

public class SnLGCTest {

    //TESTS IN THIS CLASS ARE LARGELY WRITTEN WITH THE ASSISTANCE OF AI

    private SnLGameController controller;
    private SnLBoard board;
    private List<Player> players;

    @BeforeEach
    public void setup() {
        board = new SnLBoard();
        players = new ArrayList<>();
        players.add(new Player("Alice", "icon1.png"));
        players.add(new Player("Bob", "icon2.png"));
        controller = new SnLGameController(board, players);
        controller.start();
    }

    /**
     * Verifies that all players are correctly placed on the first tile at game start.
     */
    @Test
    public void testStartPositions() {
        Tile startingTile = board.getTiles().get(0);
        for (Player player : players) {
            assertTrue(startingTile.getPlayers().contains(player));
        }
    }

    /**
     * Checks that a player moves correctly to a given tile and is tracked on that tile.
     */
    @Test
    public void testMovePlayerToValidTile() {
        Player player = players.get(0);
        controller.movePlayer(player, 10, movementType.PATH);
        assertEquals(10, player.getPosition());
        assertTrue(board.getTiles().get(9).getPlayers().contains(player));
    }

    /**
     * Ensures a player who moves is removed from the previous tile.
     */
    @Test
    public void testOldTileCleared() {
        Player player = players.get(0);
        controller.movePlayer(player, 10, movementType.PATH);
        assertFalse(board.getTiles().get(0).getPlayers().contains(player));
    }

    /**
     * Checks that tile effects are executed if present.
     */
    @Test
    public void testEffectExecution() {
        Player player = players.get(0);
        Tile tile = board.getTiles().get(9);

        //Effect is added to tile
        tile.setEffect(new SnakeEffect(10, 5));

        //Move player to tile with effect
        controller.movePlayer(player, 10, movementType.PATH);

        //Check if player is moved to the snake's end
        assertEquals(5, player.getPosition());
        assertTrue(board.getTiles().get(4).getPlayers().contains(player));
    }

}
