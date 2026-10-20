package requestresult;

/**
 * Result of a login request. Contains a username and authtoken.
 */
public record LoginResult(String username, String authToken) {
}
