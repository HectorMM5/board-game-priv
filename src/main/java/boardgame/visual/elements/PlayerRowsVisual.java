package boardgame.visual.elements;

import java.util.List;

import boardgame.model.boardFiles.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * A vertical layout container that displays a list of player rows,
 * where each row consists of a player's icon and name.
 * 
 * Used as a side panel or summary to show the current game participants visually.
 * 
 * Each row is styled with padding, spacing, and consistent fonts and sizes.
 * 
 * @author Hector Mendana Morales
 */
public class PlayerRowsVisual extends VBox {

    /**
     * Constructs a visual container to display all players passed in.
     * Each player is rendered as a row with an icon and a name.
     *
     * @param players the list of players to display
     */
    public PlayerRowsVisual(List<Player> players) {
        System.out.println("Reached PlayerRowsVisual with player list size: " + players.size());
        players.stream().forEach(this::createPlayerRow);
    }

    /**
     * Creates and adds a visual row for a single player.
     * The row contains the player's icon and name in an HBox.
     *
     * @param player the player to display
     */
    public void createPlayerRow(Player player) {
        HBox row = new HBox();
        row.setBackground(Background.fill(Color.WHITE));

        ImageView playerIcon = new ImageView(new Image(getClass().getResourceAsStream(player.getIcon())));
        playerIcon.setFitWidth(50);
        playerIcon.setFitHeight(50);

        Label name = new Label(player.getName());
        name.setAlignment(Pos.CENTER);
        name.setFont(Font.font("System", FontWeight.BOLD, 18));
        name.setPrefWidth(100);

        row.setAlignment(Pos.CENTER);
        row.setSpacing(75);
        row.getChildren().addAll(playerIcon, name);

        this.getChildren().addAll(row);
    }

    /**
     * Returns the current instance of {@code PlayerRowsVisual}.
     * Can be used fluently if needed.
     *
     * @return this visual container
     */
    public PlayerRowsVisual getPlayerRows() {
        return this;
    }
}
