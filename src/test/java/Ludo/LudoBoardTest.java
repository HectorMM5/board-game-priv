package Ludo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import boardgame.model.boardFiles.LudoBoard;
import boardgame.model.boardFiles.Tile;

public class LudoBoardTest {

    //TESTS IN THIS CLASS ARE LARGELY WRITTEN WITH THE ASSISTANCE OF AI
    
    private LudoBoard ludoBoard;

    @BeforeEach
    public void setUp() {
        ludoBoard = new LudoBoard();
    }

    @Test
    @DisplayName("Should initialize main tiles list with 56 tiles")
    public void shouldInitializeMainTilesListWithCorrectSize() {
        assertNotNull(ludoBoard.getTiles(), "Tiles list should not be null");
        assertEquals(56, ludoBoard.getTiles().size(), "Main tiles list should contain 56 tiles");
    }

    @Test
    @DisplayName("Should initialize main tiles with correct 1-based indices")
    public void shouldInitializeMainTilesWithCorrectIndices() {
        List<Tile> tiles = ludoBoard.getTiles();
        for (int i = 0; i < 56; i++) {
            assertNotNull(tiles.get(i), "Tile at index " + i + " should not be null");
            assertEquals(i + 1, tiles.get(i).getNumber(), "Tile at index " + i + " should have number " + (i + 1));
        }
    }

    @Test
    @DisplayName("Should initialize redPath with 6 tiles")
    public void shouldInitializeRedPathWithCorrectSize() {
        assertNotNull(ludoBoard.getRedPath(), "Red path should not be null");
        assertEquals(6, ludoBoard.getRedPath().size(), "Red path should contain 6 tiles");
    }

    @Test
    @DisplayName("Should initialize greenPath with 6 tiles")
    public void shouldInitializeGreenPathWithCorrectSize() {
        assertNotNull(ludoBoard.getGreenPath(), "Green path should not be null");
        assertEquals(6, ludoBoard.getGreenPath().size(), "Green path should contain 6 tiles");
    }

    @Test
    @DisplayName("Should initialize yellowPath with 6 tiles")
    public void shouldInitializeYellowPathWithCorrectSize() {
        assertNotNull(ludoBoard.getYellowPath(), "Yellow path should not be null");
        assertEquals(6, ludoBoard.getYellowPath().size(), "Yellow path should contain 6 tiles");
    }

    @Test
    @DisplayName("Should initialize bluePath with 6 tiles")
    public void shouldInitializeBluePathWithCorrectSize() {
        assertNotNull(ludoBoard.getBluePath(), "Blue path should not be null");
        assertEquals(6, ludoBoard.getBluePath().size(), "Blue path should contain 6 tiles");
    }

    @Test
    @DisplayName("Should initialize color paths with correct 1-based indices")
    public void shouldInitializeColorPathsWithCorrectIndices() {
        // All paths are initialized with tiles numbered 1 to 6
        List<List<Tile>> colorPaths = List.of(
                ludoBoard.getRedPath(),
                ludoBoard.getGreenPath(),
                ludoBoard.getYellowPath(),
                ludoBoard.getBluePath()
        );

        for (List<Tile> path : colorPaths) {
            for (int i = 0; i < 6; i++) {
                assertNotNull(path.get(i), "Tile in path at index " + i + " should not be null");
                assertEquals(i + 1, path.get(i).getNumber(), "Tile in path at index " + i + " should have number " + (i + 1));
            }
        }
    }
}