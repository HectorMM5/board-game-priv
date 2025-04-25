package boardgame.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

import boardgame.model.boardFiles.Board;
import boardgame.model.effectFiles.BackToStartEffect;
import boardgame.model.effectFiles.Effect;
import boardgame.model.effectFiles.LadderEffect;
import boardgame.model.effectFiles.PlaceholderEffect;
import boardgame.model.effectFiles.SkipTurnEffect;
import boardgame.model.effectFiles.SnakeEffect;

/**
 * Utility class for loading and constructing a Snakes and Ladders board from a JSON file.
 * The JSON file should be located in the project's resources folder and named "boards.json".
 *
 * It supports various tile effects such as Ladder, Snake, LoseTurn, and BackToStart,
 * and populates the board accordingly.
 *
 * @author Hector Mendana Morales
 */
public class BoardJSON {

    /**
     * Constructs a Snakes and Ladders board by reading a specific board configuration
     * from the boards.json resource file.
     *
     * @param choice the index of the board configuration in the "SnL" array to load
     * @return a populated {@link Board} object with effects set according to the JSON
     */
    public static Board constructSnLBoardFromJSON(int choice) {
        Board board = new Board();

        try (InputStream is = BoardJSON.class.getClassLoader().getResourceAsStream("boards.json")) {
            if (is == null) {
                throw new IOException("boards.json not found in resources!");
            }

            String jsonText = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JSONObject SnLBoard = new JSONObject(jsonText)
                .getJSONArray("games")
                .getJSONObject(0)
                .getJSONArray("SnL")
                .getJSONObject(choice);

            JSONArray tilesWithEffects = SnLBoard.getJSONArray("tiles");

            IntStream.range(0, tilesWithEffects.length())
                .forEach(i -> modifyEffectTileFromJSON(tilesWithEffects.getJSONObject(i), board));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return board;
    }

    /**
     * Parses a single tile with an effect from the JSON object and applies the appropriate
     * {@link Effect} to the target tile on the board.
     *
     * @param tileWithEffect the JSON object representing the tile and its effect
     * @param board the {@link Board} to apply the effect to
     */
    public static void modifyEffectTileFromJSON(JSONObject tileWithEffect, Board board) {
        int tileNumber = tileWithEffect.getInt("tile");
        String effectType = tileWithEffect.getString("effect");

        Effect effect;
        int target;

        switch (effectType) {
            case "Ladder":
                target = tileWithEffect.getInt("target");
                board.getTiles().get(target - 1).setEffect(new PlaceholderEffect());
                effect = new LadderEffect(tileNumber, target);
                break;

            case "Snake":
                target = tileWithEffect.getInt("target");
                board.getTiles().get(target - 1).setEffect(new PlaceholderEffect());
                effect = new SnakeEffect(tileNumber, target);
                break;

            case "LoseTurn":
                effect = new SkipTurnEffect();
                break;

            case "Back":
                effect = new BackToStartEffect(tileNumber, 1);
                break;

            default:
                throw new AssertionError("Unknown effect type: " + effectType);
        }

        board.getTiles().get(tileNumber - 1).setEffect(effect);
    }
}
