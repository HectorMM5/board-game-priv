package boardgame.controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * Manages player profile data stored in a CSV file, including player registration,
 * win tracking, and icon updates.
 * 
 * This class utilizes OpenCSV to read and write player information from
 * 'playerProfiles.csv', which is stored in the resources folder.
 * 
 * Each row in the CSV file represents a player and contains:
 * [player name, icon path, win count]
 * 
 * @author Hector Mendana Morales
 * @author Bjørn Adam Vangen
 */
public class PlayerCSV {

    private static final File FILE = new File("src/main/resources/playerProfiles.csv");

    /**
     * Reads and returns the contents of the CSV file as a list of string arrays.
     *
     * @return a list of all player data rows from the CSV file
     */
    private static ArrayList<String[]> getCSVContent() {
        ArrayList<String[]> allPlayers = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(FILE))) {
            String[] row;
            while ((row = reader.readNext()) != null) {
                allPlayers.add(row);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read CSV player file.");
        }

        return allPlayers;
    }

    /**
     * Rewrites the entire CSV file with the provided player data.
     *
     * @param allPlayers the updated list of all player rows to write to the file
     */
    private static void rewriteFile(ArrayList<String[]> allPlayers) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE))) {
            writer.writeAll(allPlayers);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read CSV player file.");
        }
    }

    /**
     * Registers a new player with the given name and icon.
     * Throws an exception if a player with the same name already exists.
     *
     * @param name the name of the player to register
     * @param icon the icon path for the player
     */
    public void registerNewPlayer(String name, String icon) {
        ArrayList<String[]> allPlayers = getCSVContent();
        Iterator<String[]> iterator = allPlayers.iterator();

        while (iterator.hasNext()) {
            if (iterator.next()[0].equals(name)) {
                throw new IllegalArgumentException("A player profile with the given name already exists.");
            }
        }

        allPlayers.add(new String[]{name, icon, "0"});
        rewriteFile(allPlayers);
    }

    /**
     * Increments the win count for the player with the given name.
     *
     * @param name the name of the player whose win count to increment
     */
    public static void incrementPlayerWins(String name) {
        ArrayList<String[]> allPlayers = getCSVContent();
        Iterator<String[]> iterator = allPlayers.iterator();
        String[] row;

        while (iterator.hasNext()) {
            row = iterator.next();
            if (row[0].equals(name)) {
                row[2] = Integer.toString(Integer.parseInt(row[2]) + 1);
                break;
            }
        }

        rewriteFile(allPlayers);
    }

    /**
     * Changes the icon path for the player with the specified name.
     *
     * @param name the name of the player
     * @param icon the new icon path to assign to the player
     */
    public static void changeIcon(String name, String icon) {
        ArrayList<String[]> allPlayers = getCSVContent();
        Iterator<String[]> iterator = allPlayers.iterator();
        String[] row;

        while (iterator.hasNext()) {
            row = iterator.next();
            if (row[0].equals(name)) {
                row[1] = icon;
                break;
            }
        }

        rewriteFile(allPlayers);
    }

    /**
     * Returns an array of all player names stored in the CSV file.
     *
     * @return an array of player names
     */
    public String[] getPlayerNames() {
        ArrayList<String[]> content = getCSVContent();
        String[] playerNames = new String[content.size()];
        for (int i = 0; i < playerNames.length; i++) {
            playerNames[i] = content.get(i)[0];
        }
        return playerNames;
    }

    /**
     * Retrieves the icon path associated with the specified player name.
     *
     * @param playerName the name of the player to look up
     * @return the icon path of the player
     * @throws IllegalArgumentException if the player is not found
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
}
