package chess;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentPlayer;
    private ChessBoard gameBoard;

    public ChessGame() {
        this.currentPlayer = TeamColor.WHITE;       // set current player to white at game start.
        this.gameBoard = new ChessBoard();
//        this.gameBoard.resetBoard();                // Place board in starting configuration.
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentPlayer;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentPlayer = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        // set as a collection--type determined by piece.pieceMoves.
        Collection<ChessMove> validMoves;

        // get piece at position on board.
        ChessPiece piece = gameBoard.getPiece(startPosition);

        // if piece returns null, then return null--no piece at startPosition.
        if (piece == null) {
            return null;
        }

        System.out.println(piece.getPieceType());
        validMoves = piece.pieceMoves(gameBoard, startPosition);

        // iterate over all valid moves. determine if any will put things in check.

        // return set of valid moves.
        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // if a piece is in check, then the king must be capturable by an opposing team.
        // iterate through all pieces of the correct color. If they land on a piece that is the teamcolor king,
        // then check is made.

        // iterate through all pieces.
        for (int y = 1; y < 9; y++) {
            for (int x = 1; x < 9; x++) {
                // get piece at this position. check that it is not teamColor
                ChessPosition currentPosition = new ChessPosition(y, x);
                ChessPiece currentPiece = gameBoard.getPiece(currentPosition);
                // if the team colors are the same, move on to the next position.
                if (currentPiece.getTeamColor() == teamColor) {
                    continue;
                }
                // else... get valid moves.
                Collection<ChessMove> validMoves = validMoves(currentPosition);

            }
        }


        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

}
