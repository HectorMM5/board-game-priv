package boardgame.controller;

import java.io.File;
import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * Provides utility methods for accessing player-related resources such as
 * player icon files and image loading. Inherits functionality from {@link PlayerCSV}
 * to manage player profiles stored in a CSV file.
 * 
 * This class operates on image resources located in the {@code /PlayerIcons/} directory
 * inside the resources folder.
 * 
 * @author Bjørn Adam Vangen
 */
public class PlayerDataAccess extends PlayerCSV {
    private final String ICON_RELATIVE_PATH = "/PlayerIcons/";

    /**
     * Constructs a new PlayerDataAccess object with inherited CSV functionality.
     */
    public PlayerDataAccess() {
        super();
    }

    /**
     * Retrieves the list of icon file names located in the PlayerIcons directory.
     *
     * @return a list of icon file names, or {@code null} if the path is invalid
     */
    public ArrayList<String> getIconNames() {
        ArrayList<String> iconFileNames = new ArrayList<>();
        String directoryPath = "src/main/resources/PlayerIcons/";
        File dir = new File(directoryPath);

        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    iconFileNames.add(file.getName());
                }
            }
            return iconFileNames;
        } else {
            System.out.println("Not a directory: " + directoryPath);
            return null;
        }
    }

    /**
     * Loads and returns a JavaFX {@link Image} object from a given file name
     * in the PlayerIcons resource directory.
     *
     * @param fileName the name of the image file to load
     * @return the {@link Image} object loaded from the file
     */
    public Image getImageFromFileName(String fileName) {
        String resourcePath = ICON_RELATIVE_PATH + fileName;
        return new Image(getClass().getResourceAsStream(resourcePath));
    }
}
