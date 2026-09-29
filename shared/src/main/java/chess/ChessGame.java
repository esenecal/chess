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

        validMoves = piece.pieceMoves(gameBoard, startPosition);

        // for all invalid moves (moves that result in check).
        ArrayList<ChessMove> invalidMoves = new ArrayList<>();

        // check to see if a valid move will put the king in check. if it does, well, it is not a valid move.
        for (ChessMove move : validMoves) {
            // run a mock movement.
            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();
            ChessPiece.PieceType promote = move.getPromotionPiece();
            ChessPiece pieceAtEnd = gameBoard.getPiece(end);        // piece at the end position.

            // move piece from start to end, promote if needed.
            movePiece(start, end, promote, piece);
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
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promotionPiece = move.getPromotionPiece();

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

        movePiece(startPosition, endPosition, promotionPiece, piece);

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
    private void movePiece(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece, ChessPiece piece) {

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

                // else... get all possible moves. we do not need to worry about "valid" moves,
                // because a piece does not need to move away (thus potentially exposing themselves for check)
                // to put a king in check. It can protect its king and put in check simultaneously.
                Collection<ChessMove> possibleMoves = currentPiece.pieceMoves(gameBoard, currentPosition);
                // iterate over valid moves and ensure that
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

        // 3 conditions:
        // king cannot move in any adjacent squares.
        // no piece can move between the king and attacker.
        // no piece can capture the attacking piece.

        // currentKingPosition is the position of the king that is currently under scrutiny. Thus, it is the same
        // team color as teamColor.

        // ensure we are in check
        if (isInCheck(teamColor)) {
            // **********************   king cannot move in any adjacent squares    *********************************
            Collection<ChessMove> validKingMoves = validMoves(currentKingPosition);
            // if the king can make any valid moves, return false.
            if (!validKingMoves.isEmpty()) {
                return false;
            }

            // **********************   no piece can move between the king and the attacker     *********************

                // this only really applies to bishops, rooks, and queens. This is because if a king is being
                // threatened by another king, a pawn, or a knight, no piece can move between them.
            // We can use this to calculate all possible positions that a piece must move to.
            // We start at the attacker position. we y up or down one, towards the king position. then x. we repeat until we reach it.
            // we include the attacking piece. This means that we are checking if it can be captured, which nullifies checkmate.

            ArrayList<ChessPosition> lineOfSight = new ArrayList<>();   // all position between attacking piece and king, including attacking position and excluding king's.
            int row = currentAttackerPosition.getRow();       // get y, x of attacking piece.
            int col = currentAttackerPosition.getColumn();

            // repeat until we reach king position
            while (row != currentKingPosition.getRow() && col != currentAttackerPosition.getColumn()) {
                // add y, x to line of sight.
                lineOfSight.add(new ChessPosition(row, col));
                // update y, x.
                if (row < currentAttackerPosition.getRow()) {
                    // if y is less, then increase by one.
                    row++;
                } else if (row > currentAttackerPosition.getRow()) {
                    // if y is greater than current attacker position, decrement. do nothing otherwise.
                    row--;
                }
                if (col < currentAttackerPosition.getColumn()) {
                    col++;
                } else if (col > currentAttackerPosition.getColumn()) {
                    col--;
                }
            }
            // now we have all pieces. we iterate through all pieces, seeing if a piece lands on the line of sight.
            // iterate through all pieces.
            for (int y = 1; y < 9; y++) {
                for (int x = 1; x < 9; x++) {
                    // get piece at this position. check that it is not teamColor
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
                        // Check to see if the end position is in the line of sight. If it is, return false.
                        ChessPosition endPosition = move.getEndPosition();
                        if (lineOfSight.contains(endPosition)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        } else {
            // if we are not in check, we cannot be in checkmate.
            return false;
        }
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
