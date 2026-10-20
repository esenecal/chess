package dataaccess;

public class NonexistentUserException extends DataAccessException {
    public NonexistentUserException(String message) {
        super(message);
    }
}
