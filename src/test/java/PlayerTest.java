import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import boardgame.model.Observer.PlayerObserver;
import boardgame.model.boardFiles.Player;
import boardgame.model.boardFiles.Tile;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setup() {
        player = new Player("icon.png", "TestPlayer");
    }

    //Tests initial player setup and basic accessors
    @Test
    public void testInitialState() {
        assertEquals("TestPlayer", player.getName());
        assertEquals("icon.png", player.getIcon());
        assertEquals(1, player.getPosition());
    }

    //Tests icon modification
    @Test
    public void testSetIcon() {
        player.setIcon("new_icon.png");
        assertEquals("new_icon.png", player.getIcon());
    }
    
    //Tests moveToTile updates internal position correctly
    @Test
    public void testMoveToTile() {
        Tile tile = new Tile(4); // this corresponds to position 5
        player.moveToTile(tile);
        assertEquals(5, player.getPosition());
    }

    //Tests observer addition and removal
    @Test
    public void testObserverManagement() {
        PlayerObserver dummy = (p, pos, type) -> {};
        player.addObserver(dummy);
        assertTrue(player.getObservers().contains(dummy));

        player.removeObserver(dummy);
        assertFalse(player.getObservers().contains(dummy));
    }
}
