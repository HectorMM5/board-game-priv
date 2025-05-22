package boardgame.utils;

import javafx.stage.Screen;

/**
 * Utility class to retrieve the dimensions of the primary screen.
 */
public class ScreenDimension {

    /**
     * Returns the width of the primary screen in pixels.
     *
     * @return the screen width.
     */
    public static double getScreenWidth() {
        return Screen.getPrimary().getBounds().getWidth();
    }

    /**
     * Returns the height of the primary screen in pixels.
     *
     * @return the screen height.
     */
    public static double getScreenHeight() {
        return Screen.getPrimary().getBounds().getHeight();
    }
}