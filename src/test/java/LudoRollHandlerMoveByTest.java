
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import boardgame.controller.GameControllers.LudoGameController;
import boardgame.controller.RollHandlers.LudoRollHandler;
import boardgame.model.boardFiles.Ludo.LudoBoard;
import boardgame.model.boardFiles.Player;
import boardgame.utils.movementType;
import boardgame.visual.elements.SideColumn.DiceButtonVisual;
import boardgame.visual.elements.SideColumn.SideColumnVisual;
import boardgame.visual.gameLayers.LudoTokenLayer;

public class LudoRollHandlerMoveByTest {
    private LudoGameController gameController;
    private LudoTokenLayer tokenLayer;
    private SideColumnVisual sideColumn;
    private DiceButtonVisual diceButton;
    private LudoRollHandler handler;
    private Player player;
    private LudoBoard board;

    @BeforeEach
    public void setup() {
        player = new Player("TestPlayer", "file:icon.png");
        board = new LudoBoard();
        List<Player> players = new ArrayList<>();
        players.add(player);

        gameController = new LudoGameController(board, players);
        gameController.start();
        tokenLayer = mock(LudoTokenLayer.class);
        sideColumn = mock(SideColumnVisual.class);

        handler = new LudoRollHandler(gameController, tokenLayer, sideColumn);
    }

    @Test
    public void testMoveByThroughBoard() {
        handler.moveBy(player, 3, diceButton);
        //Yellow (first player) starts at tile 43, so check tile 45 after moving 3, accounting for 0-based index
        assertTrue(board.getTiles().get(45).getPlayers().contains(player));
        verify(tokenLayer, atLeastOnce()).addToAnimationQueue(any());
    }

    @Test
    public void testMoveByEnteringHome() {
        gameController.movePlayer(player, 38, movementType.INSTANT); // Move player to the start of the home path
        handler.getTilesMoved().replace(player, 53);

        handler.moveBy(player, 6, diceButton);
        handler.applyHomeMove(player, 4); //acts as the deferred (due to animation) move
        
        assertFalse(board.getTiles().get(39).getPlayers().contains(player));
        verify(tokenLayer, atLeastOnce()).addToAnimationQueue(any());

        //2 spent getting to tile 40, 4 spent in home path
        assertEquals(4, gameController.getHomePosition().get(player)); 
    }


    @Test
    public void testMoveByInHomePath() {
        gameController.movePlayer(player, 40, movementType.INSTANT); 
        handler.getTilesMoved().replace(player, 53);

        handler.moveBy(player, 2, diceButton); 
        handler.applyHomeMove(player, 2); //acts as the deferred (due to animation) move

        handler.moveBy(player, 3, diceButton); 
        handler.applyHomeByMove(player, 3); //acts as the deferred (due to animation) move

        assertEquals(5, gameController.getHomePosition().get(player));
        assertFalse(board.getTiles().get(39).getPlayers().contains(player));
        verify(tokenLayer, atLeastOnce()).addToAnimationQueue(any());
    }
}
