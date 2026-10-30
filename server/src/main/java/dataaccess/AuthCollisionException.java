package dataaccess;

public class AuthCollisionException extends DataAccessException {
    public AuthCollisionException(String message) {
        super(message);
    }
}
