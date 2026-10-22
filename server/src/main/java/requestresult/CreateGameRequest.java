package requestresult;

/**
 * Data to be sent in a createGame
 *
 * @param authToken String
 * @param gameName  String
 */
public record CreateGameRequest(String authToken, String gameName) {
}
