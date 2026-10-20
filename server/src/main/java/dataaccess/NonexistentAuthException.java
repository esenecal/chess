package dataaccess;

public class NonexistentAuthException extends DataAccessException {
    public NonexistentAuthException(String message) {
        super(message);
    }
}
