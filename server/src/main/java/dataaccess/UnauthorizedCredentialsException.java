package dataaccess;

public class UnauthorizedCredentialsException extends DataAccessException {
    public UnauthorizedCredentialsException(String message) {
        super(message);
    }
}
