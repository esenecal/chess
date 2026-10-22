package dataaccess;

public class NonexistentGameException extends DataAccessException {
    public NonexistentGameException(String message) {
        super(message);
    }
}
