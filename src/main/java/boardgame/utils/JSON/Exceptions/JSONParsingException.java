package boardgame.utils.JSON;

/**
 * Custom exception to signal problems when parsing a JSON file.
 * 
 * @author Hector
 */
public class JSONParsingException extends RuntimeException {


    public JSONParsingException() {
        super("An error occurred while parsing the JSON file.");
    }

    /**
     * Constructs a new JsonParsingException with a custom message.
     * 
     * @param message the detailed error message
     */
    public JSONParsingException(String message) {
        super(message);
    }


}
