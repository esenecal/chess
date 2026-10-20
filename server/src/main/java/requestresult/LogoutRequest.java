package requestresult;

/**
 * Information needed to log a user out.
 *
 * @param authToken
 */
public record LogoutRequest(String authToken) {
}
