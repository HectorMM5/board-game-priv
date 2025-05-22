package SnL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import boardgame.controller.GameControllers.SnLGameController;
import boardgame.controller.RollHandlers.SnLRollHandler;
import boardgame.model.Player;
import boardgame.utils.movementType;
import boardgame.visual.elements.SideColumn.SideColumnVisual;
import boardgame.visual.gameLayers.SnLTokenLayer;

public class SnLRollHandlerMoveByTest {

    //TESTS IN THIS CLASS ARE LARGELY WRITTEN WITH THE ASSISTANCE OF AI

    private SnLGameController mockGameController;
    private SideColumnVisual mockSideColumn;
    private SnLTokenLayer mockTokenLayer;
    private SnLRollHandler handler;
    private Player mockPlayer;

    @BeforeEach
    public void setUp() {
        mockGameController = mock(SnLGameController.class);
        mockSideColumn = mock(SideColumnVisual.class);
        mockTokenLayer = mock(SnLTokenLayer.class);
        handler = new SnLRollHandler(mockGameController, mockTokenLayer, mockSideColumn);
        mockPlayer = mock(Player.class);

        when(mockGameController.getCurrentPlayer()).thenReturn(mockPlayer);
    }

    @Test
    public void testHandleRollDiceCalls() {
        when(mockPlayer.getPosition()).thenReturn(1);

        handler.handleRollDice();

        // We can't assert exact dice roll here, but we know moveBy is called with player and a number > 0
        verify(mockSideColumn).displayRoll(anyInt());
        verify(mockGameController).movePlayer(eq(mockPlayer), anyInt(), eq(movementType.PATH));
        verify(mockGameController).advanceTurn();
    }

    @Test
    public void testMoveByAddsQueueWhenBelow90() {
        when(mockPlayer.getPosition()).thenReturn(1);

        handler.moveBy(mockPlayer, 5);

        verify(mockGameController).movePlayer(mockPlayer, 6, movementType.PATH);
        verify(mockTokenLayer).addToAnimationQueue(any(Runnable.class));
    }

    @Test
    public void testMoveByDoesNotAddQueuePosition90() {
        when(mockPlayer.getPosition()).thenReturn(85);

        handler.moveBy(mockPlayer, 5);

        verify(mockGameController).movePlayer(mockPlayer, 90, movementType.PATH);
        verify(mockTokenLayer, never()).addToAnimationQueue(any());
    }
}
