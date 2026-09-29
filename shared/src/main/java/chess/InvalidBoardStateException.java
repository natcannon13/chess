package chess;
/**
Indicates that the board state is invalid.
 */
public class InvalidBoardStateException extends Exception {
    public InvalidBoardStateException(String message) {
        super(message);
    }
}
