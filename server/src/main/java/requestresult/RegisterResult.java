package requestresult;

/**
 * Result of a register request; note that the parameters are flipped from authData for representation to the user.
 *
 * @param username
 * @param authToken
 */
public record RegisterResult(String username, String authToken) {
}
