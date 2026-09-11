package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;     // Determines the piece's color according to the TeamColor enum
    private ChessPiece.PieceType type;          // Determines the piece's type according to the PieceType enum

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;       // Add constructor parameter to field.
        this.type = type;                   // Add constructor parameter to field.
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;      // Return the TeamColor value associated with this object.
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;            // Return the PieceType value associated with this object.
    }



    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // https://www.youtube.com/watch?v=mTtK8iRXsZo
        ChessPiece piece = board.getPiece(myPosition);      // get the piece at the given position. We are checking this piece.
        ArrayList<ChessMove> validMoves = new ArrayList<>();    // empty ArrayList for all valid moves.

        switch(piece.getPieceType()) {      // https://www.w3schools.com/java/java_switch.asp
            case PieceType.BISHOP:
                return bishopMoves(board, myPosition, validMoves);
            default:
                return null;
        }
    }

    // logic for bishop movement.
    private static Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition, ArrayList<ChessMove> validMoves) {     // https://codingtechroom.com/question/-java-make-methods-static-best-practice
        // Check upper right. These for loops continue if they are within the board, and have not encountered another piece.
        for (int x = myPosition.getRow()+1, y = myPosition.getColumn()+1; x < 9 && y < 9; x++, y++) {        // https://stackoverflow.com/questions/14752536/java-for-loop-multiple-variables
            validMoves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
        }
        // check lower right
        for (int x = myPosition.getRow()+1, y = myPosition.getColumn()-1; x < 9 && y > 0; x++, y--) {        // https://stackoverflow.com/questions/14752536/java-for-loop-multiple-variables
            validMoves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
        }
        // check lower left
        for (int x = myPosition.getRow()-1, y = myPosition.getColumn()-1; x > 0 && y > 0; x--, y--) {        // https://stackoverflow.com/questions/14752536/java-for-loop-multiple-variables
            validMoves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
        }
        // check upper left
        for (int x = myPosition.getRow()-1, y = myPosition.getColumn()+1; x > 0 && y < 9; x--, y++) {        // https://stackoverflow.com/questions/14752536/java-for-loop-multiple-variables
            validMoves.add(new ChessMove(myPosition, new ChessPosition(x, y), null));
        }

        return validMoves;
    }

    // created with intelliJ as per assignment directions
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessPiece piece)) {
            return false;
        }
        return pieceColor == piece.pieceColor && type == piece.type;
    }

    // Created by intelliJ as per assignment directions.
    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
