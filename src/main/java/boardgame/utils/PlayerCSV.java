package boardgame.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Manages player profile data stored in a CSV file, including player
 * registration, win tracking, and icon updates.
 * <p>
 * This class utilizes OpenCSV to read and write player information from
 * 'playerProfiles.csv', which is stored in the resources folder.
 * <p>
 * Each row in the CSV file represents a player and contains: [player name, icon
 * path, win count]
 *
 * @author Hector Mendana Morales
 * @author Bjørn Adam Vangen
 */
public class PlayerCSV {

    private final static String PATH = "src/main/resources/playerProfiles.csv";
    private static final File DEFAULT_FILE = new File(PATH);
    private static File currentFile = DEFAULT_FILE;
    private static PlayerCSV instance = null;
    private static final List<String> ALLOWED_COLORS = Arrays.asList("Red", "White", "Orange", "Purple", "Lime", "Yellow");


    private PlayerCSV() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns the singleton instance of the {@code PlayerCSV} class.
     * If no instance exists, a new one is created.
     *
     * @return the singleton instance of {@code PlayerCSV}.
     */
    public static PlayerCSV instance() {
        if (instance == null) {
            instance = new PlayerCSV();
        }
        return instance;
    }

    /**
     * Sets the file that PlayerCSV will operate on. This is useful for imports.
     *
     * @param file The new CSV file to use.
     */
    private static void setCurrentFile(File file) {
        currentFile = file;
    }

    /**
     * Gets the file that PlayerCSV is currently operating on.
     *
     * @return The current CSV file.
     */
    private static File getCurrentFile() {
        return currentFile;
    }

    /**
     * Shows a file chooser dialog and allows the user to select a CSV file
     * for importing player data. If a file is selected, it sets this file
     * as the current file for PlayerCSV.
     *
     * @param primaryStage The primary stage of the application.
     * @return true if a file was successfully selected, false otherwise.
     */
    public boolean importProfiles(Stage primaryStage) {
        //ChatGPT
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Player Profiles");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            setCurrentFile(selectedFile);
            return true;
        }
        return false;
    }

    /**
     * Reads and returns the contents of the currently active CSV file as a list
     * of string arrays. Each inner array represents a row of player data.
     *
     * @return a list of all player data rows from the CSV file.
     * @throws IllegalArgumentException if there is an issue reading the CSV file.
     */
    public static ArrayList<String[]> getCSVContent() {
        ArrayList<String[]> allPlayers = new ArrayList<>();
        File fileToRead = getCurrentFile();
        try (CSVReader reader = new CSVReader(new FileReader(fileToRead))) {
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length > 1) {
                    String iconColor = row[1].trim();
                    if (!ALLOWED_COLORS.stream().anyMatch(color -> color.equalsIgnoreCase(iconColor))) {
                        ErrorDialog.showAndExit(
                                "Invalid Player Data",
                                "Invalid color found in player profile.",
                                String.format("Color '%s' not supported. Please remove it.", iconColor)
                        );
                        return new ArrayList<>(); // Return empty list to indicate failure
                    }
                } else if (row.length < 3) {
                    ErrorDialog.showAndExit(
                            "Invalid Player Data",
                            "Incorrect number of columns in player profile.",
                            "Each row must contain at least 'name, color, win_count'."
                    );
                    return new ArrayList<>();
                }
                allPlayers.add(row);
            }
        } catch (IOException e) {
            ErrorDialog.showAndExit(
                    "File Reading Error",
                    "Could not read the player profiles file.",
                    "Please ensure the file exists and is in the correct format.\n" + e.getMessage()
            );
            return new ArrayList<>(); // Return empty list on read failure
        }
        return allPlayers;
    }


    /**
     * Rewrites the currently active CSV file with the provided list of player data.
     *
     * @param allPlayers the updated list of all player rows to write to the file.
     * @throws IllegalArgumentException if there is an issue writing to the CSV file.
     */
    private static void rewriteFile(ArrayList<String[]> allPlayers) {
        File fileToWrite = getCurrentFile();
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileToWrite, false))) { // Overwrite the file
            writer.writeAll(allPlayers);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to write to CSV player file: " + fileToWrite.getAbsolutePath(), e);
        }
    }

    /**
     * Registers a new player with the given name and icon. If a player with the
     * same name already exists, their icon will be updated instead.
     *
     * @param name the name of the player to register.
     * @param icon the icon path for the player.
     * @throws IllegalArgumentException if the player name is empty.
     */
    public void registerNewPlayer(String name, String icon) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty.");
        }

        ArrayList<String[]> allPlayers = getCSVContent();
        Iterator<String[]> iterator = allPlayers.iterator();
        boolean found = false;

        while (iterator.hasNext()) {
            String[] row = iterator.next();
            if (row[0].equals(name)) {
                found = true;
                row[1] = icon; // Update the icon
                break;
            }
        }

        if (!found) {
            allPlayers.add(new String[]{name, icon, "0"}); // Initialize win count to 0
        }
        rewriteFile(allPlayers);
    }

    /**
     * Changes the icon path for the player with the specified name.
     *
     * @param name the name of the player.
     * @param icon the new icon path to assign to the player.
     */
    public static void changeIcon(String name, String icon) {
        ArrayList<String[]> allPlayers = getCSVContent();
        allPlayers.stream()
            .filter(row -> row[0].equals(name))
            .findFirst()
            .ifPresent(row -> row[1] = icon);
        rewriteFile(allPlayers);
    }

    /**
     * Returns an array of all player names stored in the CSV file.
     *
     * @return an array of player names.
     */
    public String[] getPlayerNames() {
        ArrayList<String[]> content = getCSVContent();
        return content.stream()
            .map(row -> row[0])
            .toArray(String[]::new);
    }

    /**
     * Retrieves the icon path associated with the specified player name.
     *
     * @param playerName the name of the player to look up.
     * @return the icon path of the player.
     * @throws IllegalArgumentException if the player is not found.
     */
    public String getPlayerIconByPlayerName(String playerName) {
        ArrayList<String[]> content = getCSVContent();
        for (String[] row : content) {
            if (row[0].equals(playerName)) {
                return row[1];
            }
        }
        throw new IllegalArgumentException("Player not found.");
    }

    /**
     * Imports player profiles from a user-selected CSV file, replacing the
     * currently loaded profiles.
     *
     * @param primaryStage The primary stage for the file chooser.
     * @return true if import was successful, false otherwise.
     */
    public boolean handleImport(Stage primaryStage) {
        if (importProfiles(primaryStage)) {
            // Optionally, you could reload the content or notify other parts of the application
            System.out.println("Player profiles imported successfully from: " + getCurrentFile().getAbsolutePath());
            return true;
        }
        System.out.println("No file selected for import.");
        return false;
    }
}