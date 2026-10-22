package requestresult;

/**
 * GameData, but lacking the ChessGame object. Used when game meta data is needed.
 *
 * @param gameID        int
 * @param whiteUsername String
 * @param blackUsername String
 * @param gameName      String
 */
public record GameMetaData(int gameID, String whiteUsername, String blackUsername, String gameName) {
}
