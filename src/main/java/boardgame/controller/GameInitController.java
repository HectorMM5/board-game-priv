package boardgame.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import boardgame.model.boardFiles.Player;
import boardgame.visual.scenes.GameInitView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GameInitController {

    private final String ICON_RELATIVE_PATH = "/PlayerIcons/";
    private List<String> iconFileNames = new ArrayList<>();
    private PlayerDataAccess playerDataAccess = new PlayerDataAccess();
    protected Stage primaryStage;
    private GameInitView view;
    private ArrayList<Integer> playerIconIndexes = new ArrayList<>();

    public GameInitController(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.view = new GameInitView(this.primaryStage);
        view.setController(this);

        init();
    }

    private void init() {
        iconFileNames = playerDataAccess.getIconNames();
        view.init();
        addPlayer();
    }

    public void addPlayer() {
        int index = playerIconIndexes.size();
        playerIconIndexes.add(0);

        String[] playerNames = playerDataAccess.getPlayerNames();
        Image initialIconImage = playerDataAccess.getImageFromFileName(iconFileNames.get(0));

        EventHandler<ActionEvent> iconBtnHandler = e -> handleIconButtonClick(index);
        EventHandler<ActionEvent> dropdownHandler = e -> handleDropdownSelection(index);
        EventHandler<ActionEvent> saveBtnHandler = e -> handleSaveButton(index);

        view.addPlayerField(playerNames, index, iconFileNames.get(0),
                iconBtnHandler, dropdownHandler, saveBtnHandler, initialIconImage);
    }

    private void handleIconButtonClick(int playerIndex) {
        int currentIndex = playerIconIndexes.get(playerIndex);
        if (currentIndex < iconFileNames.size() - 1) {
            currentIndex++;
        } else {
            currentIndex = 0;
        }

        playerIconIndexes.set(playerIndex, currentIndex);
        Image iconImage = playerDataAccess.getImageFromFileName(iconFileNames.get(currentIndex));
        view.updateIconButton(playerIndex, iconImage);
    }

    private void handleDropdownSelection(int playerIndex) {
        String selectedPlayer = view.getPlayerName(playerIndex);
        String[] playerNames = playerDataAccess.getPlayerNames();

        if (selectedPlayer != null && Arrays.stream(playerNames).anyMatch(selectedPlayer::equals)) {
            try {
                String relativeIconPath = playerDataAccess.getPlayerIconByPlayerName(selectedPlayer);
                String fileName = new File(relativeIconPath).getName();
                int iconIndex = iconFileNames.indexOf(fileName);

                if (iconIndex == -1) {
                    System.out.println("Icon not found in loaded images");
                    return;
                }

                playerIconIndexes.set(playerIndex, iconIndex);
                Image iconImage = playerDataAccess.getImageFromFileName(fileName);
                view.updateIconButton(playerIndex, iconImage);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleSaveButton(int playerIndex) {
        String playerName = view.getPlayerName(playerIndex);
        if (playerName == null || playerName.trim().isEmpty()) {
            System.out.println("Player name cannot be empty.");
            return;
        }

        int selectedIconIndex = playerIconIndexes.get(playerIndex);
        String iconFileName = iconFileNames.get(selectedIconIndex);
        String relativeIconPath = ICON_RELATIVE_PATH + iconFileName;

        try {
            String[] playerNames = playerDataAccess.getPlayerNames();
            if (Arrays.stream(playerNames).anyMatch(playerName::equals)) {
                playerDataAccess.changeIcon(playerName, relativeIconPath);
                view.updateSaveButtonText(playerIndex, "Updated");
            } else {
                playerDataAccess.registerNewPlayer(playerName, relativeIconPath);
                view.updateSaveButtonText(playerIndex, "Saved");
                view.addPlayerToDropdown(playerIndex, playerName);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            view.updateSaveButtonText(playerIndex, "Failed");
        }
    }

    public void startGame() {
        view.setStartGameButtonText("Something wrong. Restart application");
        //not supposed to ever be called. Will only happen if something is wrong
        //method kinda stupid though so have to change later probably

    }

    public ArrayList<Player> getCurrentPlayers() {
        ArrayList<Player> playerList = new ArrayList<>();
        List<ComboBox<String>> playerDropdowns = view.getPlayerDropdowns();

        for (int i = 0; i < playerDropdowns.size(); i++) {
            String playerName = playerDropdowns.get(i).getValue();

            if (playerName != null && !playerName.trim().isEmpty()) {
                try {
                    String relativeIconPath = playerDataAccess.getPlayerIconByPlayerName(playerName);

                    if (!relativeIconPath.startsWith(ICON_RELATIVE_PATH)) {
                        String iconFileName = new File(relativeIconPath).getName();
                        relativeIconPath = ICON_RELATIVE_PATH + iconFileName;

                    }

                    Player player = new Player(relativeIconPath, playerName);
                    playerList.add(player);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        if (playerList.isEmpty()) {
            System.out.println("No valid players selected");
        }
        for (Player player : playerList) {
            System.out.println("Player: " + player.getName() + ", Icon: " + player.getIcon());
        }

        System.out.println("Player list size: " + playerList.size());
        return playerList;
    }

    public void start() {
        view.show();
    }
}
