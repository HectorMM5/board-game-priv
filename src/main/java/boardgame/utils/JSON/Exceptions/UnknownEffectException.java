package boardgame.utils.JSON.Exceptions;

/**
 * Custom exception to signal problems when parsing a JSON file.
 * 
 * @author Hector
 */
public class UnknownEffectException extends RuntimeException {

    public UnknownEffectException() {
        super("An error occurred while parsing the JSON file.");
    }

    /**
     * Constructs a new UnkownEffectException with a custom message.
     * 
     * @param message the detailed error message
     */
    public UnknownEffectException(String message) {
        super(message);
    }


}
