package boardgame.model.boardFiles;

/**
 * Represents a player in the board game, containing identity, icon, position,
 * and optional color data. Each player can move between tiles on the board.
 * 
 * The player starts at position 1 by default.
 * 
 * @author Hector Mendana Morales
 */
public class Player {
    private String icon;
    final String name;
    int position;

    /**
     * Constructs a player with the given icon path and name.
     *
     * @param icon the path to the player's icon image
     * @param name the name of the player
     */
    public Player(String icon, String name) {
        this.icon = icon;
        this.name = name;
        this.position = 1;
    }

    /**
     * Returns the current tile position of the player.
     *
     * @return the player's position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets the player's tile position.
     *
     * @param position the new tile position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Returns the icon file path associated with the player.
     *
     * @return the player's icon path
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon file path for the player.
     *
     * @param icon the new icon path
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * Returns the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Moves the player to the given tile.
     *
     * @param tile the target tile to move the player to
     */
    public void moveToTile(Tile tile) {
        position = tile.getNumber() + 1;
    }
}
