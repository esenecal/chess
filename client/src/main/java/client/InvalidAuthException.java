package client;

public class InvalidAuthException extends RuntimeException {
    public InvalidAuthException(String message) {
        super(message);
    }
}
