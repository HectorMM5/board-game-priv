package boardgame.visual.scenes;

import boardgame.controller.GameInitController;
import boardgame.model.boardFiles.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameInitView {
    private Stage primaryStage;
    private List<Button> iconBtns = new ArrayList<>();
    private List<HBox> playerFieldWrappers = new ArrayList<>();
    private List<Button> saveButtons = new ArrayList<>();
    private List<ComboBox<String>> playerDropdowns = new ArrayList<>();
    private VBox playerWrapper;
    
    private Button presetBtn1 = new Button("Preset1");
    private Button presetBtn2 = new Button("Preset2");
    private Button presetBtn3 = new Button("Preset3");
    private Button addPlayerBtn = new Button("Add Player");
    private Button startGameBtn = new Button("Start game!");
    
    private GameInitController controller;
    
    public GameInitView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    public void setController(GameInitController controller) {
        this.controller = controller;
    }
    
    public void init() {
        primaryStage.setTitle("Snakes & Ladders config");

        VBox menuWrapper = new VBox();
        HBox menuPane = new HBox();
        menuWrapper.getChildren().add(menuPane);
        menuPane.setAlignment(Pos.CENTER);
        menuWrapper.setMargin(menuPane, new Insets(50,50,50,50));

        String buttonStyle = "-fx-start-margin: 0; -fx-end-margin: 20; -fx-alignment: center; -fx-pref-height: 30; -fx-background-color: rgba(200,200,200);";

        presetBtn1.setStyle(buttonStyle);
        presetBtn2.setStyle(buttonStyle);
        presetBtn3.setStyle(buttonStyle);
        menuPane.getChildren().addAll(presetBtn1,presetBtn2,presetBtn3);

        addPlayerBtn.setStyle(buttonStyle);
        HBox addPlayerBtnWrapper = new HBox();
        addPlayerBtnWrapper.getChildren().add(addPlayerBtn);
        addPlayerBtnWrapper.setAlignment(Pos.CENTER);
        menuWrapper.getChildren().add(addPlayerBtnWrapper);

        playerWrapper = new VBox();
        playerWrapper.setSpacing(10);
        
        addPlayerBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                controller.addPlayer();
            }
        });

        VBox.setMargin(playerWrapper, new Insets(0,0,50,0));
        menuWrapper.getChildren().add(playerWrapper);

        startGameBtn.setStyle(buttonStyle);
        HBox startButtonWrapper = new HBox();
        startButtonWrapper.setAlignment(Pos.CENTER);
        startButtonWrapper.getChildren().add(startGameBtn);
        menuWrapper.getChildren().add(startButtonWrapper);

        startGameBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                controller.startGame();
            }
        });

        Scene menu = new Scene(menuWrapper, 600, 600);
        primaryStage.setScene(menu);
    }

    public void addPlayerField(String[] playerNames, int index, String initialIconFileName, 
        EventHandler<ActionEvent> iconBtnHandler,
        EventHandler<ActionEvent> dropdownHandler,
        EventHandler<ActionEvent> saveBtnHandler,
        Image initialIconImage) {

        ComboBox<String> playerDropdown = new ComboBox<>();
        playerDropdown.setPromptText("Player " + (index + 1));
        playerDropdown.setEditable(true);
        playerDropdown.setPrefHeight(40);
    
        for (String playername : playerNames) {
            if (playername != null && !playername.isEmpty()) {
                playerDropdown.getItems().add(playername);
            }
        }

        playerDropdowns.add(playerDropdown);

        Button iconBtn = new Button();
        iconBtns.add(iconBtn);

        ImageView buttonIMG = new ImageView(initialIconImage);
        buttonIMG.setFitHeight(40);
        buttonIMG.setFitWidth(40);
        iconBtn.setGraphic(buttonIMG);

        Button saveButton = new Button("Save new");
        saveButtons.add(saveButton);

        HBox playerFieldWrapper = new HBox(10); // 10px spacing
        playerFieldWrapper.getChildren().addAll(iconBtn, playerDropdown, saveButton);
        playerFieldWrapper.setAlignment(Pos.CENTER);
        playerFieldWrappers.add(playerFieldWrapper);

        playerWrapper.getChildren().add(playerFieldWrapper);

        iconBtn.setOnAction(iconBtnHandler);
        playerDropdown.setOnAction(dropdownHandler);
        saveButton.setOnAction(saveBtnHandler);
    }
    
    public void updateIconButton(int playerIndex, Image iconImage) {
        if (playerIndex >= 0 && playerIndex < iconBtns.size()) {
            Button iconBtn = iconBtns.get(playerIndex);
            ImageView buttonIMG = new ImageView(iconImage);
            
            buttonIMG.setFitHeight(40);
            buttonIMG.setFitWidth(40);
            iconBtn.setGraphic(buttonIMG);
        }
    }
    
    public void updateSaveButtonText(int playerIndex, String text) {
        if (playerIndex >= 0 && playerIndex < saveButtons.size()) {
            saveButtons.get(playerIndex).setText(text);
        }
    
    }
    

    public String getPlayerName(int playerIndex) {
        if (playerIndex >= 0 && playerIndex < playerDropdowns.size()) {
            return playerDropdowns.get(playerIndex).getValue();
        }
        return null;
    }
    
    public void addPlayerToDropdown(int playerIndex, String playerName) {
        if (playerIndex >= 0 && playerIndex < playerDropdowns.size()) {
            playerDropdowns.get(playerIndex).getItems().add(playerName);
        }
    }
    
    public void setStartGameButtonText(String text) {
        startGameBtn.setText(text);
    }

    public List<ComboBox<String>> getPlayerDropdowns() {
        return playerDropdowns;
    }

    public Button getAddPlayerBtn() {
        return addPlayerBtn;
    }
    
    public Button getStartGameBtn() {
        return startGameBtn;
    }
    
    public void show() {
        primaryStage.show();
    }
}