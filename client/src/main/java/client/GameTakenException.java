package client;

public class GameTakenException extends RuntimeException {
    public GameTakenException(String message) {
        super(message);
    }
}
