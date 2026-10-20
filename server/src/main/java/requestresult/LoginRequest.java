package requestresult;

/**
 * Information being requested by a login request
 *
 * @param username
 * @param password
 */
public record LoginRequest(String username, String password) {
}
