import org.json.JSONObject;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boardgame.model.boardFiles.SnL.SnLBoard;
import boardgame.model.effectFiles.SnL.BackToStartEffect;
import boardgame.model.effectFiles.SnL.Effect;
import boardgame.model.effectFiles.SnL.LadderEffect;
import boardgame.model.effectFiles.SnL.SkipTurnEffect;
import boardgame.model.effectFiles.SnL.SnakeEffect;
import boardgame.utils.JSON.BoardJSON;
import boardgame.utils.JSON.Exceptions.JSONParsingException;
import boardgame.utils.JSON.Exceptions.UnknownEffectException;

public class BoardJSONTest {

    private JSONObject validLadderTile;
    private JSONObject validSnakeTile;
    private JSONObject skipTurnTile;
    private JSONObject backToStartTile;
    private JSONObject invalidEffectTile;

    @BeforeEach
    public void setup() {
        validLadderTile = new JSONObject()
            .put("tile", 5)
            .put("effect", "Ladder")
            .put("target", 15);

        validSnakeTile = new JSONObject()
            .put("tile", 20)
            .put("effect", "Snake")
            .put("target", 10);

        skipTurnTile = new JSONObject()
            .put("tile", 25)
            .put("effect", "LoseTurn");

        backToStartTile = new JSONObject()
            .put("tile", 30)
            .put("effect", "Back");

        invalidEffectTile = new JSONObject()
            .put("tile", 40)
            .put("effect", "Teleport");
    }

    @Test
    public void testConstructSnLBoardFromJSON() {
        SnLBoard board = BoardJSON.constructSnLBoardFromJSON(0);
        assertNotNull(board);
        assertEquals(90, board.getTiles().size());
    }

    @Test
    public void testValidLadderEffectParsed() {
        SnLBoard board = new SnLBoard();
        assertDoesNotThrow(() -> {
            BoardJSON.modifyEffectTileFromJSON(validLadderTile, board);
        });
        Effect effect = board.getTiles().get(4).getEffect();
        assertTrue(effect instanceof LadderEffect);
    }

    @Test
    public void testValidSnakeEffectParsed() {
        SnLBoard board = new SnLBoard();
        assertDoesNotThrow(() -> {
            BoardJSON.modifyEffectTileFromJSON(validSnakeTile, board);
        });
        Effect effect = board.getTiles().get(19).getEffect();
        assertTrue(effect instanceof SnakeEffect);
    }

    @Test
    public void testSkipTurnEffectParsed() {
        SnLBoard board = new SnLBoard();
        assertDoesNotThrow(() -> {
            BoardJSON.modifyEffectTileFromJSON(skipTurnTile, board);
        });
        Effect effect = board.getTiles().get(24).getEffect();
        assertTrue(effect instanceof SkipTurnEffect);
    }

    @Test
    public void testBackToStartEffectParsed() {
        SnLBoard board = new SnLBoard();
        assertDoesNotThrow(() -> {
            BoardJSON.modifyEffectTileFromJSON(backToStartTile, board);
        });
        Effect effect = board.getTiles().get(29).getEffect();
        assertTrue(effect instanceof BackToStartEffect);
    }

    @Test
    public void testUnknownEffectThrowsException() {
        SnLBoard board = new SnLBoard();
        UnknownEffectException ex = assertThrows(UnknownEffectException.class, () -> {
            BoardJSON.modifyEffectTileFromJSON(invalidEffectTile, board);
        });
        assertNotNull(ex.getMessage());
    }

    @Test
    public void testInvalidTileNumberThrowsException() {
        SnLBoard board = new SnLBoard();
        JSONObject badTile = new JSONObject().put("tile", 200).put("effect", "LoseTurn");
        JSONParsingException ex = assertThrows(JSONParsingException.class, () -> {
            BoardJSON.modifyEffectTileFromJSON(badTile, board);
        });
        assertNotNull(ex.getMessage());
    }

    @Test
    public void testIllegalStartOrEndTileThrowsException() {
        SnLBoard board = new SnLBoard();
        JSONObject startTile = new JSONObject().put("tile", 1).put("effect", "LoseTurn");
        JSONObject endTile = new JSONObject().put("tile", 90).put("effect", "LoseTurn");

        JSONParsingException ex1 = assertThrows(JSONParsingException.class, () -> {
            BoardJSON.modifyEffectTileFromJSON(startTile, board);
        });
        assertNotNull(ex1.getMessage());

        JSONParsingException ex2 = assertThrows(JSONParsingException.class, () -> {
            BoardJSON.modifyEffectTileFromJSON(endTile, board);
        });
        assertNotNull(ex2.getMessage());
    }
}
