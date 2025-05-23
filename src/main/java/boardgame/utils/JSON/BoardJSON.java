package boardgame.utils.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

import boardgame.model.boardFiles.SnLBoard;
import boardgame.model.effectFiles.BackToStartEffect;
import boardgame.model.effectFiles.Effect;
import boardgame.model.effectFiles.LadderEffect;
import boardgame.model.effectFiles.PlaceholderEffect;
import boardgame.model.effectFiles.SkipTurnEffect;
import boardgame.model.effectFiles.SnakeEffect;
import boardgame.utils.JSON.Exceptions.JSONParsingException;
import boardgame.utils.JSON.Exceptions.UnknownEffectException;

/**
 * Utility class for loading and constructing a Snakes and Ladders board from a JSON file.
 * The JSON file should be located in the project's resources folder and named "boards.json".
 *
 * It supports various tile effects such as Ladder, Snake, LoseTurn, and BackToStart,
 * and populates the board accordingly.
 * 
 *  
 */
public class BoardJSON {

    /**
     * Constructs a Snakes and Ladders board by reading a specific board configuration
     * from the boards.json resource file.
     *
     * @param choice the index of the board configuration in the "SnL" array to load
     * @return a populated {@link SnLBoard} object with effects set according to the JSON
     * @throws JSONParsingException if loading or parsing the JSON file fails
     */
    public static SnLBoard constructSnLBoardFromJSON(int choice) {
        SnLBoard board = new SnLBoard();

        try (InputStream is = BoardJSON.class.getClassLoader().getResourceAsStream("boards.json")) {
            if (is == null) {
                throw new JSONParsingException("boards.json not found in resources.");
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
            throw new JSONParsingException("Failed to parse SnL board from JSON.");
        } catch (IndexOutOfBoundsException e) {
            throw new JSONParsingException("Effect was attempted placed at an illegal tile.");
        }

        return board;
    }

    /**
     * Parses a single tile with an effect from the JSON object and applies the appropriate
     * {@link Effect} to the target tile on the board.
     *
     * @param tileWithEffect the JSON object representing the tile and its effect
     * @param board the {@link SnLBoard} to apply the effect to
     * @throws UnknownEffectException if the effect type is unknown
     */
    public static void modifyEffectTileFromJSON(JSONObject tileWithEffect, SnLBoard board) {
        int tileNumber = tileWithEffect.getInt("tile");

        if (tileNumber < 1 || tileNumber > board.getTiles().size()) {
            throw new JSONParsingException("Tile number out of bounds.");
        }

        if (tileNumber == 1 || tileNumber == 90) {
            throw new JSONParsingException("Tile number 1 and 90 cannot have an effect.");
        }

        String effectType = tileWithEffect.getString("effect");

        Effect effect;
        int target;

        switch (effectType) {
            case "Ladder" -> {
                target = tileWithEffect.getInt("target");
                board.getTiles().get(target - 1).setEffect(new PlaceholderEffect());
                effect = new LadderEffect(tileNumber, target);
            }

            case "Snake" -> {
                target = tileWithEffect.getInt("target");
                board.getTiles().get(target - 1).setEffect(new PlaceholderEffect());
                effect = new SnakeEffect(tileNumber, target);
            }

            case "LoseTurn" -> effect = new SkipTurnEffect();

            case "Back" -> effect = new BackToStartEffect(tileNumber, 1);

            default -> throw new UnknownEffectException("Unknown effect type");
        }

        board.getTiles().get(tileNumber - 1).setEffect(effect);
    }
}
