package requestresult;

/**
 * Result of a login request. Contains a username and authToken.
 *
 * @param username  username
 * @param authToken authToken associated with user
 */
public record LoginResult(String username, String authToken) {
}
