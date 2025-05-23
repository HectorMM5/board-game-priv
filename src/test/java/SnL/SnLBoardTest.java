package SnL;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import boardgame.model.boardFiles.SnLBoard;
import boardgame.model.boardFiles.Tile;
import boardgame.model.effectFiles.LadderEffect;
import boardgame.model.effectFiles.SnakeEffect;

public class SnLBoardTest {

    //TESTS IN THIS CLASS ARE LARGELY WRITTEN WITH THE ASSISTANCE OF AI

    private SnLBoard defaultBoard;
    private SnLBoard customBoard;

    @BeforeEach
    public void setUp() {
        defaultBoard = new SnLBoard();
        customBoard = new SnLBoard(5, 5); 
    }

    @Test
    @DisplayName("Default constructor should create a 10x9 board (90 tiles)")
    public void defaultConstructorShouldCreateCorrectSizeBoard() {
        assertEquals(10, defaultBoard.getBoardWidth(), "Default board width should be 10");
        assertEquals(9, defaultBoard.getBoardHeight(), "Default board height should be 9");
        assertEquals(90, defaultBoard.getTileCount(), "Default board should have 90 tiles");
        assertEquals(90, defaultBoard.getTiles().size(), "Default tiles list size should be 90");
    }

    @Test
    @DisplayName("Custom constructor should create a board with specified dimensions")
    public void customConstructorShouldCreateCorrectSizeBoard() {
        assertEquals(5, customBoard.getBoardWidth(), "Custom board width should be 5");
        assertEquals(5, customBoard.getBoardHeight(), "Custom board height should be 5");
        assertEquals(25, customBoard.getTileCount(), "Custom board should have 25 tiles");
        assertEquals(25, customBoard.getTiles().size(), "Custom tiles list size should be 25");
    }

    @Test
    @DisplayName("All tiles should be initialized with correct 1-based indices")
    public void allTilesShouldHaveCorrectIndices() {
        // Test default board
        List<Tile> defaultTiles = defaultBoard.getTiles();
        for (int i = 0; i < defaultBoard.getTileCount(); i++) {
            assertEquals(i + 1, defaultTiles.get(i).getNumber(), "Default board tile " + i + " should have number " + (i + 1));
        }

        // Test custom board
        List<Tile> customTiles = customBoard.getTiles();
        for (int i = 0; i < customBoard.getTileCount(); i++) {
            assertEquals(i + 1, customTiles.get(i).getNumber(), "Custom board tile " + i + " should have number " + (i + 1));
        }
    }

    @Test
    @DisplayName("getTilesWithLadders should return only tiles with LadderEffect")
    public void getTilesWithLaddersShouldReturnCorrectTiles() {
        // Add some effects for testing
        defaultBoard.getTiles().get(5).setEffect(new LadderEffect(6, 20)); // Tile 6
        defaultBoard.getTiles().get(19).setEffect(new LadderEffect(20, 40)); // Tile 20
        defaultBoard.getTiles().get(30).setEffect(new SnakeEffect(31, 10)); // Tile 31 (should not be included)

        List<Tile> ladders = defaultBoard.getTilesWithLadders();

        assertNotNull(ladders, "List of ladders should not be null");
        assertEquals(2, ladders.size(), "Should find 2 ladders");
        assertTrue(ladders.contains(defaultBoard.getTiles().get(5)), "Should contain tile 6");
        assertTrue(ladders.contains(defaultBoard.getTiles().get(19)), "Should contain tile 20");
        assertFalse(ladders.contains(defaultBoard.getTiles().get(30)), "Should not contain tile 31 (snake)");
    }

    @Test
    @DisplayName("getTilesWithSnakes should return only tiles with SnakeEffect")
    public void getTilesWithSnakesShouldReturnCorrectTiles() {
        // Add some effects for testing
        defaultBoard.getTiles().get(10).setEffect(new SnakeEffect(11, 5)); // Tile 11
        defaultBoard.getTiles().get(24).setEffect(new SnakeEffect(25, 15)); // Tile 25
        defaultBoard.getTiles().get(4).setEffect(new LadderEffect(5, 10)); // Tile 5 (should not be included)

        List<Tile> snakes = defaultBoard.getTilesWithSnakes();

        assertNotNull(snakes, "List of snakes should not be null");
        assertEquals(2, snakes.size(), "Should find 2 snakes");
        assertTrue(snakes.contains(defaultBoard.getTiles().get(10)), "Should contain tile 11");
        assertTrue(snakes.contains(defaultBoard.getTiles().get(24)), "Should contain tile 25");
        assertFalse(snakes.contains(defaultBoard.getTiles().get(4)), "Should not contain tile 5 (ladder)");
    }

    @Test
    @DisplayName("getTilesWithLadders should return empty list if no ladders exist")
    public void getTilesWithLaddersShouldReturnEmptyIfNone() {
        List<Tile> ladders = defaultBoard.getTilesWithLadders();
        assertTrue(ladders.isEmpty(), "List of ladders should be empty");
    }

    @Test
    @DisplayName("getTilesWithSnakes should return empty list if no snakes exist")
    public void getTilesWithSnakesShouldReturnEmptyIfNone() {
        List<Tile> snakes = defaultBoard.getTilesWithSnakes();
        assertTrue(snakes.isEmpty(), "List of snakes should be empty");
    }
}