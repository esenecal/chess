package records;

import chess.ChessGame;

public record JoinGameRequest(ChessGame.TeamColor team, Integer gameID) {
}
