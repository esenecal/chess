package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    // https://www.youtube.com/watch?v=mTtK8iRXsZo
    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;   // https://www.youtube.com/watch?v=mTtK8iRXsZo
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;     // https://www.youtube.com/watch?v=mTtK8iRXsZo
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;  // https://www.youtube.com/watch?v=mTtK8iRXsZo
    }

    @Override
    public String toString() {      // return chess move as a string.
        return String.format("[%s, %s]", startPosition, endPosition);  // https://www.youtube.com/watch?v=mTtK8iRXsZo
    }

    // Created with IntelliJ as per assignment directions
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessMove chessMove)) {
            return false;
        }
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && promotionPiece == chessMove.promotionPiece;
    }

    // Created with IntelliJ as per assignment directions
    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }
}
