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
    private ChessPosition currentKingPosition;      // used for checkmate calculations. Current king being checked.
    private ChessPosition currentAttackerPosition;  // used for checkmate calculations. Piece threatening king.

    public ChessGame() {
        this.currentPlayer = TeamColor.WHITE;       // set current player to white at game start.
        this.gameBoard = new ChessBoard();
        this.gameBoard.resetBoard();                // Place board in starting configuration.
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

        validMoves = piece.pieceMoves(gameBoard, startPosition);

        // for all invalid moves (moves that result in check).
        ArrayList<ChessMove> invalidMoves = new ArrayList<>();

        // check to see if a valid move will put the king in check. if it does, well, it is not a valid move.
        for (ChessMove move : validMoves) {
            // run a mock movement.
            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();
            ChessPiece pieceAtEnd = gameBoard.getPiece(end);        // piece at the end position.

            // move piece from start to end, promote if needed.
            movePiece(move);
            // now, run check. If we run in check, add to invalidMoves.
            if (isInCheck(piece.getTeamColor())) {
                invalidMoves.add(move);
            }
            // reset. Place old piece at end position, place piece at start.
            gameBoard.addPiece(end, pieceAtEnd);
            gameBoard.addPiece(start, piece);
        }

        // remove all invalidMoves from validMoves.
        validMoves.removeAll(invalidMoves);

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
        ChessPosition startPosition = move.getStartPosition();

        ChessPiece piece = gameBoard.getPiece(startPosition);
        if (piece == null) {
            // if there is no piece to move, throw an exception.
            throw new InvalidMoveException("No piece at position");
        }

        if (piece.getTeamColor() != currentPlayer) {
            // if it is not the piece's turn, throw error.
            throw new InvalidMoveException("Out of Turn");
        }

        // determine if something is a legal move.
        Collection<ChessMove> validMoves = validMoves(startPosition);
        if (!validMoves.contains(move)) {
            // if it cannot make a proper move, then throw exception.
            throw new InvalidMoveException("Invalid Move");
        }

        movePiece(move);

        // Change team turn
        if (currentPlayer == TeamColor.WHITE) {
            currentPlayer = TeamColor.BLACK;
        } else {
            currentPlayer = TeamColor.WHITE;
        }
    }

    /**
     * Handles logic for moving a piece.
     */
    private void movePiece(ChessMove move) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();

        ChessPiece piece = gameBoard.getPiece(startPosition);

        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            // if we are at the ends of the board, then we must promote.
            if (endPosition.getRow() == 1 || endPosition.getRow() == 8) {
                // get the promotion piece, which will be the new piece added.
                piece = new ChessPiece(currentPlayer, promotionPiece);    //move.getPromotionPiece()
            }
        }

        // Now, we can actually make the move!
        gameBoard.addPiece(endPosition, piece);     // add piece to end piece.
        gameBoard.addPiece(startPosition, null);    // remove piece from old position.

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
                // if the team colors are the same, or there is no piece, move on to the next position.
                if (currentPiece == null || currentPiece.getTeamColor() == teamColor) {
                    continue;
                }

                // else... get all possible moves. we do not need to worry about "valid" moves, because a piece does not need to move away (thus potentially exposing themselves for check) to put a king in check. It can protect its king and put in check simultaneously.
                Collection<ChessMove> possibleMoves = currentPiece.pieceMoves(gameBoard, currentPosition);
                // iterate over all possible moves
                for (ChessMove move : possibleMoves) {
                    // check to see if there is a piece here, and the piece is a king.
                    ChessPosition endPosition = move.getEndPosition();
                    if (gameBoard.getPiece(endPosition) != null && gameBoard.getPiece(endPosition).getPieceType() == ChessPiece.PieceType.KING) {
                        currentAttackerPosition = move.getStartPosition();
                        currentKingPosition = endPosition;
                        return true;
                    }
                }

            }
        }

        // if we have not found a capture, then return false.
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // currentKingPosition and currentAttackerPosition change frequently. This keeps track of the main
        // king and attacker, the ones that are the subject of this check.
        ChessPosition kingPosition;
        ChessPosition attackerPosition;

        // 3 conditions:
        // king cannot move in any adjacent squares.
        // no piece can move between the king and attacker.
        // no piece can capture the attacking piece.

        // currentKingPosition is the position of the king that is currently under scrutiny. Thus, it is the same
        // team color as teamColor.

        // ensure we are in check. This also sets kingPosition and attackerPosition.
        if (!isInCheck(teamColor)) {
            clearCheckMateValues();
            return false;
        }

        kingPosition = currentKingPosition;
        attackerPosition = currentAttackerPosition;

        // **********************   king cannot move in any adjacent squares    *********************************
        Collection<ChessMove> validKingMoves = validMoves(currentKingPosition);

        // if the king can make valid moves, check those moves.
        if (!validKingMoves.isEmpty()) {

            // check all moves the king can make. If none of them allow the king to be in check, then reset.
            for (ChessMove move : validKingMoves) {
                // run a mock movement.
                ChessPiece kingPiece = gameBoard.getPiece(move.getStartPosition());
                ChessPiece pieceAtEnd = gameBoard.getPiece(move.getEndPosition());            // piece at the end position.

                // move piece from start to end, promote if needed.
                movePiece(move);

                // if at any point we do not end up in check, then we are not in checkmate.
                if (!isInCheck(kingPiece.getTeamColor())) {
                    // reset. Place old piece at end position, place piece at start.
                    gameBoard.addPiece(move.getEndPosition(), pieceAtEnd);
                    gameBoard.addPiece(move.getStartPosition(), kingPiece);
                    clearCheckMateValues(); // clear checkmate values.
                    return false;
                }

                // reset. Place old piece at end position, place piece at start.
                gameBoard.addPiece(move.getEndPosition(), pieceAtEnd);
                gameBoard.addPiece(move.getStartPosition(), kingPiece);
            }
        }   // otherwise, the king cannot make any valid moves, so that check works.

        // **********************  attacking piece cannot be captured.  ***********************************

        // iterate through all pieces and see if the attacking piece can be captured
        for (int y = 1; y < 9; y++) {
            for (int x = 1; x < 9; x++) {
                // get piece at this position.
                ChessPosition currentPosition = new ChessPosition(y, x);
                ChessPiece currentPiece = gameBoard.getPiece(currentPosition);

                // if the team colors are different, or there is no piece, move on to the next position.
                if (currentPiece == null || currentPiece.getTeamColor() != teamColor) {
                    continue;
                }
                // else... get valid moves.
                Collection<ChessMove> validMoves = validMoves(currentPosition);
                // iterate over valid moves
                for (ChessMove move : validMoves) {
                    // Check to see if the end position is the attacker's position.
                    // if it is, this piece can capture to break checkmate.
                    ChessPosition endPosition = move.getEndPosition();
                    if (endPosition.equals(attackerPosition)) {
                        clearCheckMateValues();
                        return false;
                    }
                }
            }
        }

        // All checks have passed. The king cannot move and the attacker cannot be captured.
        // Checkmate.
        clearCheckMateValues();
        return true;
    }

    /**
     * Clear the variables containing checkmate test values.
     */
    private void clearCheckMateValues() {
        currentKingPosition = null;
        currentAttackerPosition = null;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) { // we are in check. we cannot be in stalemate.
            clearCheckMateValues();
            return false;
        }

        // check all pieces of this color. ensure that none have any valid moves.
        for (int y = 1; y < 9; y++) {
            for (int x = 1; x < 9; x++) {
                ChessPiece piece = gameBoard.getPiece(new ChessPosition(y, x));

                // skip if there is no piece here, or the opposite team.
                if (piece == null || piece.getTeamColor() != teamColor) {
                    continue;
                }

                Collection<ChessMove> validMoves = validMoves(new ChessPosition(y, x));

                // there is a valid move! Not a stalemate.
                if (!validMoves.isEmpty()) {
                    return false;
                }

            }
        }

        // we are not in check, and we have no valid moves. stalemate.
        return true;

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

    // Created by IntelliJ
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessGame chessGame)) {
            return false;
        }
        return currentPlayer == chessGame.currentPlayer && Objects.equals(gameBoard, chessGame.gameBoard) && Objects.equals(currentKingPosition, chessGame.currentKingPosition) && Objects.equals(currentAttackerPosition, chessGame.currentAttackerPosition);
    }

    // Created by IntelliJ
    @Override
    public int hashCode() {
        return Objects.hash(currentPlayer, gameBoard, currentKingPosition, currentAttackerPosition);
    }
}
