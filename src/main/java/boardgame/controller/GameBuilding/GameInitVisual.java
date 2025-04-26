package boardgame.controller.GameBuilding;

import java.util.ArrayList;
import java.util.List;

import boardgame.model.boardFiles.Player;
import boardgame.utils.GameSetup;
import boardgame.visual.elements.PlayerCreationRow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameInitVisual {

    private final VBox playerRowsContainer;
    private final Button addPlayerButton = new Button("Add Player");
    private final Button startGameButton = new Button("Start Game");

    public GameInitVisual() {
        this.playerRowsContainer = new VBox(10); 
        playerRowsContainer.setStyle("-fx-padding: 20;");  

    }

    public Scene getScene(String chosenGame) {
        Label titleLabel;

        Label subtitleLabel = new Label("Create your players!");

        switch (chosenGame) {
            case "Snakes & Ladders" -> {
                titleLabel = new Label("Snakes & Ladders");
            }

            case "Ludo" -> {
                titleLabel = new Label("Ludo");
            }
                
            default ->
                throw new AssertionError();
        }

        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: #333; -fx-font-weight: bold;");
        subtitleLabel.setAlignment(Pos.CENTER);
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555;");
    
        addPlayerButton.setOnAction(e -> addEmptyPlayerRow());
        startGameButton.setOnAction(e -> { 
            List<Player> players = new ArrayList<>();
        
            playerRowsContainer.getChildren().forEach(node -> {
                if (node instanceof PlayerCreationRow playerRow) {
                    String playerName = playerRow.getNameField().getText();
                    String iconName = playerRow.getSelectedIconName();
                    players.add(new Player(iconName, playerName));
                }
            });

            
            GameInitController.handleGameStart(chosenGame, 0, players); // TODO: Add board variant
        
        
        }); 
            

        VBox root = new VBox(20);
        root.getChildren().addAll(titleLabel, subtitleLabel, playerRowsContainer, addPlayerButton, startGameButton);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        VBox.setMargin(subtitleLabel, new Insets(0, 0, 50, 0));
         

        Scene scene = new Scene(root);
        

        // Show the first row by default
        addEmptyPlayerRow();

        return scene;

    }

    public void addEmptyPlayerRow() {
        if (playerRowsContainer.getChildren().size() == 3) {
            addPlayerButton.setDisable(true); 
        } 

        PlayerCreationRow row = new PlayerCreationRow();
        playerRowsContainer.getChildren().add(row);
    }

}
