package chess;

import java.lang.reflect.Array;
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

        switch(piece.getPieceType()) {      // https://www.w3schools.com/java/java_switch.asp
            case PieceType.BISHOP:
                return bishopMoves(board, myPosition, piece.getTeamColor());
            case PieceType.KNIGHT:
                return knightMoves(board, myPosition, piece.getTeamColor());
            case PieceType.QUEEN:
                return queenMoves(board, myPosition, piece.getTeamColor());
            case PieceType.ROOK:
                return rookMoves(board, myPosition, piece.getTeamColor());
            default:
                return null;
        }
    }

    //

    // logic for bishop movement.
    // // board for checking if there are pieces at positions. myPosition for move start. pieceColor for comparing with existing piece colors. validMoves for valid moves.
    private static Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {     // https://codingtechroom.com/question/-java-make-methods-static-best-practice

        ChessPosition currentPosition;  // for tracking current positions

        ArrayList<ChessMove> validMoves = new ArrayList<>();    // empty ArrayList for all valid moves.

        // Check upper right. These for loops continue if they are within the board, and have not encountered another piece.
        for (int x = myPosition.getColumn()+1, y = myPosition.getRow()+1; x < 9 && y < 9; x++, y++) {        // https://stackoverflow.com/questions/14752536/java-for-loop-multiple-variables
            currentPosition = new ChessPosition(y, x);  // set current position

            if (board.getPiece(currentPosition) != null) {     // If there is a piece here...
                // and both the piece we are checking and the piece in this position are of the same color...
                if (board.getPiece(currentPosition).getTeamColor() == pieceColor) {
                    break;      // stop here. We cannot add the current position to valid moves
                } else {        // however, if they are different team colors...
                    // We can add a valid move here. But, we must break, because this is also a capture.
                    validMoves.add(new ChessMove(myPosition, currentPosition, null));
                    break;
                }
            } else {
                // there is NOT a piece at this position, add it to valid moves.
                validMoves.add(new ChessMove(myPosition, currentPosition, null));
            }
        }
        // check lower right
        for (int x = myPosition.getColumn()+1, y = myPosition.getRow()-1; x < 9 && y > 0; x++, y--) {
            currentPosition = new ChessPosition(y, x);  // set current position

            if (board.getPiece(currentPosition) != null) {     // If there is a piece here...
                // and both the piece we are checking and the piece in this position are of the same color...
                if (board.getPiece(currentPosition).getTeamColor() == pieceColor) {
                    break;      // stop here. We cannot add the current position to valid moves
                } else {        // however, if they are different team colors...
                    // We can add a valid move here. But, we must break, because this is also a capture.
                    validMoves.add(new ChessMove(myPosition, currentPosition, null));
                    break;
                }
            } else {
                // there is NOT a piece at this position, add it to valid moves.
                validMoves.add(new ChessMove(myPosition, currentPosition, null));
            }
        }
        // check lower left
        for (int x = myPosition.getColumn()-1, y = myPosition.getRow()-1; x > 0 && y > 0; x--, y--) {
            currentPosition = new ChessPosition(y, x);  // set current position

            if (board.getPiece(currentPosition) != null) {     // If there is a piece here...
                // and both the piece we are checking and the piece in this position are of the same color...
                if (board.getPiece(currentPosition).getTeamColor() == pieceColor) {
                    break;      // stop here. We cannot add the current position to valid moves
                } else {        // however, if they are different team colors...
                    // We can add a valid move here. But, we must break, because this is also a capture.
                    validMoves.add(new ChessMove(myPosition, currentPosition, null));
                    break;
                }
            } else {
                // there is NOT a piece at this position, add it to valid moves.
                validMoves.add(new ChessMove(myPosition, currentPosition, null));
            }
        }
        // check upper left
        for (int x = myPosition.getColumn()-1, y = myPosition.getRow()+1; x > 0 && y < 9; x--, y++) {
            currentPosition = new ChessPosition(y, x);  // set current position

            if (board.getPiece(currentPosition) != null) {     // If there is a piece here...
                // and both the piece we are checking and the piece in this position are of the same color...
                if (board.getPiece(currentPosition).getTeamColor() == pieceColor) {
                    break;      // stop here. We cannot add the current position to valid moves
                } else {        // however, if they are different team colors...
                    // We can add a valid move here. But, we must break, because this is also a capture.
                    validMoves.add(new ChessMove(myPosition, currentPosition, null));
                    break;
                }
            } else {
                // there is NOT a piece at this position, add it to valid moves.
                validMoves.add(new ChessMove(myPosition, currentPosition, null));
            }
        }

        return validMoves;
    }

    private static Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        return null;
    }

    private static Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int x = myPosition.getColumn();
        int y = myPosition.getRow();

        // create an array of possible finial positions. A knight has 8 possible moves
        ChessPosition[] possibleMoves = new ChessPosition[8];
        possibleMoves[0] = new ChessPosition(y+2, x-1);  // up2 left1
        possibleMoves[1] = new ChessPosition(y+2, x+1);  // up2 right1
        possibleMoves[2] = new ChessPosition(y+1, x+2);  // up1 right2
        possibleMoves[3] = new ChessPosition(y-1, x+2);  // down1 right2
        possibleMoves[4] = new ChessPosition(y-2, x-1);  // down2 left1
        possibleMoves[5] = new ChessPosition(y-2, x+1);  // down2 right1
        possibleMoves[6] = new ChessPosition(y+1, x-2);  // up1 left2
        possibleMoves[7] = new ChessPosition(y-1, x-2);  // down1 left2

        // iterate through valid list to ensure that they meet criteria (within bounds, pieces there, etc.
        for (ChessPosition position : possibleMoves) {
            // check if out of bounds.
            if (position.getColumn() < 1 || position.getColumn() > 8 || position.getRow() < 1 || position.getRow() > 8) {
                continue;       // move on to the next if out of bounds.
            }

            // check for existing pieces in location. Similar logic from bishop/rook methods
            if (board.getPiece(position) != null) {     // If there is a piece here...
                // and both the piece we are checking and the piece in this position are NOT of the same color...
                if (board.getPiece(position).getTeamColor() != pieceColor) {
                    // We can add a valid move here. Then we move on.
                    validMoves.add(new ChessMove(myPosition, position, null));
                }
                // if they were the same color, this still automatically moves to the not position. At this point in
                // The if statement, we move on automatically, so we do not need to check.
            } else {
                // there is NOT a piece at this position, add it to valid moves.
                validMoves.add(new ChessMove(myPosition, position, null));
            }
        }

        return validMoves;
    }

    private static Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int myPositionX = myPosition.getColumn();
        int myPositionY = myPosition.getRow();

        ChessPosition possiblePosition;
        ChessPiece.PieceType promotion;

        // The color of the piece determines the direction it can move in.
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            // first move allows for moving forward two squares. For black pieces, this means if they are on row 7. For white, row 2.
            // if we are in the starting position and two up is open
            possiblePosition = new ChessPosition(myPositionX, myPositionY+2);       // up 2
            if (myPositionY == 2 && board.getPiece(possiblePosition) != null) {
                // Do not have to worry about promotions; a piece cannot be promoted off start.
                validMoves.add(new ChessMove(myPosition, possiblePosition, null));
            }
            // now check 1 up, and diagonal by one. check if out of bounds, and if landing on edge, promotion. Fow now, hard code it as a queen.
            possiblePosition = new ChessPosition(myPositionX, myPositionY+1);       // up 1
            if (board.getPiece(possiblePosition) != null && myPositionY < 9) {
                if (possiblePosition.getRow() == 8) {     // promotable if we land on
                    promotion = PieceType.QUEEN;
                } else {
                    promotion = null;
                }
            }

            // If there is a piece there, just move on.
        } else {

        }



        return validMoves;
    }

    // logic for queen movement. As mentioned in class, functionally a combination of rook and bishop.
    private static Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        Collection<ChessMove> rookMovement = rookMoves(board, myPosition, pieceColor);
        Collection<ChessMove> bishopMovement = bishopMoves(board, myPosition, pieceColor);

        validMoves = rookMovement;      // add all valid rook movement to valid moves.

        validMoves.addAll(bishopMovement);  // add all valid bishop movement.

        return validMoves;
    }

    // logic for rook movement. functionally similar to bishop moves, just row and column instead of diagonal.
    private static Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        ChessPosition currentPosition;  // for tracking current positions

        ArrayList<ChessMove> validMoves = new ArrayList<>();    // empty ArrayList for all valid moves.

        // Check up. These for loops continue if they are within the board, and have not encountered another piece.
        for (int y = myPosition.getRow()+1; y < 9; y++) {
            currentPosition = new ChessPosition(y, myPosition.getColumn());  // set current position. Row stays constant.

            if (board.getPiece(currentPosition) != null) {     // If there is a piece here...
                // and both the piece we are checking and the piece in this position are of the same color...
                if (board.getPiece(currentPosition).getTeamColor() == pieceColor) {
                    break;      // stop here. We cannot add the current position to valid moves
                } else {        // however, if they are different team colors...
                    // We can add a valid move here. But, we must break, because this is also a capture.
                    validMoves.add(new ChessMove(myPosition, currentPosition, null));
                    break;
                }
            } else {
                // there is NOT a piece at this position, add it to valid moves.
                validMoves.add(new ChessMove(myPosition, currentPosition, null));
            }
        }
        // check right
        for (int x = myPosition.getColumn()+1; x < 9; x++) {
            currentPosition = new ChessPosition(myPosition.getRow(), x);  // set current position. Column stays constant.

            if (board.getPiece(currentPosition) != null) {     // If there is a piece here...
                // and both the piece we are checking and the piece in this position are of the same color...
                if (board.getPiece(currentPosition).getTeamColor() == pieceColor) {
                    break;      // stop here. We cannot add the current position to valid moves
                } else {        // however, if they are different team colors...
                    // We can add a valid move here. But, we must break, because this is also a capture.
                    validMoves.add(new ChessMove(myPosition, currentPosition, null));
                    break;
                }
            } else {
                // there is NOT a piece at this position, add it to valid moves.
                validMoves.add(new ChessMove(myPosition, currentPosition, null));
            }
        }
        // check down
        for (int y = myPosition.getRow()-1; y > 0; y--) {
            currentPosition = new ChessPosition(y, myPosition.getColumn());  // set current position. Row stays constant.

            if (board.getPiece(currentPosition) != null) {     // If there is a piece here...
                // and both the piece we are checking and the piece in this position are of the same color...
                if (board.getPiece(currentPosition).getTeamColor() == pieceColor) {
                    break;      // stop here. We cannot add the current position to valid moves
                } else {        // however, if they are different team colors...
                    // We can add a valid move here. But, we must break, because this is also a capture.
                    validMoves.add(new ChessMove(myPosition, currentPosition, null));
                    break;
                }
            } else {
                // there is NOT a piece at this position, add it to valid moves.
                validMoves.add(new ChessMove(myPosition, currentPosition, null));
            }
        }
        // check left
        for (int x = myPosition.getColumn()-1; x > 0; x--) {
            currentPosition = new ChessPosition(myPosition.getRow(), x);  // set current position. column stays constant

            if (board.getPiece(currentPosition) != null) {     // If there is a piece here...
                // and both the piece we are checking and the piece in this position are of the same color...
                if (board.getPiece(currentPosition).getTeamColor() == pieceColor) {
                    break;      // stop here. We cannot add the current position to valid moves
                } else {        // however, if they are different team colors...
                    // We can add a valid move here. But, we must break, because this is also a capture.
                    validMoves.add(new ChessMove(myPosition, currentPosition, null));
                    break;
                }
            } else {
                // there is NOT a piece at this position, add it to valid moves.
                validMoves.add(new ChessMove(myPosition, currentPosition, null));
            }
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
