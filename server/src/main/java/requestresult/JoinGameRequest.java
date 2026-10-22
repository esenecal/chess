package requestresult;

import chess.ChessGame.TeamColor;

/**
 * Data sent for joining a game
 *
 * @param authToken   String
 * @param playerColor enum TeamColor
 * @param gameID      int
 */
public record JoinGameRequest(String authToken, TeamColor playerColor, int gameID) {
}
