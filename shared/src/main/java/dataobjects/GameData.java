package dataobjects;

import chess.ChessGame;

/**
 * Contains data about a game
 *
 * @param gameID        a number used for unique identification of a game
 * @param whiteUsername username of player on white team
 * @param blackUsername username of player on black team
 * @param gameName      more usable name for game
 * @param game          ChessGame object that contains game state, data, etc
 */
public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
}
