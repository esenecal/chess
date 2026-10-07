package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;     // Determines the piece's color according to the TeamColor enum
    private final ChessPiece.PieceType type;          // Determines the piece's type according to the PieceType enum

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
        // get the piece at the given position. We are checking this piece.
        ChessPiece piece = board.getPiece(myPosition);

        //noinspection EnhancedSwitchMigration
        switch(piece.getPieceType()) {      // https://www.w3schools.com/java/java_switch.asp
            case PieceType.BISHOP:
                return bishopMoves(board, myPosition, piece.getTeamColor());
            case PieceType.KING:
                return kingMoves(board, myPosition, piece.getTeamColor());
            case PieceType.KNIGHT:
                return knightMoves(board, myPosition, piece.getTeamColor());
            case PieceType.PAWN:
                return pawnMoves(board, myPosition, piece.getTeamColor());
            case PieceType.QUEEN:
                return queenMoves(board, myPosition, piece.getTeamColor());
            case PieceType.ROOK:
                return rookMoves(board, myPosition, piece.getTeamColor());
            default:
                return null;
        }
    }

    // Check a position. If a piece could move, return the move.
    private static ChessMove checkIfValidMove(ChessBoard board,
                                              ChessPosition myPosition,
                                              ChessPosition currentPosition,
                                              ChessGame.TeamColor pieceColor) {

        if (board.getPiece(currentPosition) != null) {     // If there is a piece here...
            // different teams
            if (board.getPiece(currentPosition).getTeamColor() != pieceColor) {
                // We can add a valid move here, a capture.
                return new ChessMove(myPosition, currentPosition, null);
            } else {
                // we cannot make a valid move, as there is our team's piece in the way.
                return null;
            }
        } else {
            // there is NOT a piece at this position, add it to valid moves. We can keep going.
            return new ChessMove(myPosition, currentPosition, null);
        }
    }

    // logic for bishop movement.
    // https://codingtechroom.com/question/-java-make-methods-static-best-practice
    private static Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {

        ChessPosition currentPosition;  // for tracking current positions

        ArrayList<ChessMove> validMoves = new ArrayList<>();    // empty ArrayList for all valid moves.

        // Check upper right. These for loops continue if they are within the board, and have not encountered another piece.
        // https://stackoverflow.com/questions/14752536/java-for-loop-multiple-variables
        // we repeat while y < 9, x < 9
        int y = myPosition.getRow()+1;
        int x = myPosition.getColumn()+1;

        while (y < 9 && x < 9) {
            currentPosition = new ChessPosition(y, x);
            ChessMove move = checkIfValidMove(board, myPosition, currentPosition, pieceColor);
            // if we could actually move, add it to validMoves.
            if (move != null) {
                validMoves.add(move);
            }
            // if there was a piece at the position we just checked, break.
            // if it was a capture, the valid move was added. If it wasn't, then we're just moving on.
            if (board.getPiece(currentPosition) != null) {
                break;
            }
            // move up right to next position
            y++;
            x++;
        }

        // lower right
        y = myPosition.getRow()-1;
        x = myPosition.getColumn()+1;
        while (y > 0 && x < 9) {
            currentPosition = new ChessPosition(y, x);
            ChessMove move = checkIfValidMove(board, myPosition, currentPosition, pieceColor);
            // if we could actually move, add it to validMoves.
            if (move != null) {
                validMoves.add(move);
            }
            // check the spot we just evaluated to see if we need to stop.
            if (board.getPiece(currentPosition) != null) {
                break;
            }
            // move up right to next position
            y--;
            x++;
        }

        // lower left
        y = myPosition.getRow()-1;
        x = myPosition.getColumn()-1;
        while (y > 0 && x > 0) {
            currentPosition = new ChessPosition(y, x);
            ChessMove move = checkIfValidMove(board, myPosition, currentPosition, pieceColor);
            // if we could actually move, add it to validMoves.
            if (move != null) {
                validMoves.add(move);
            }
            // check the spot we just evaluated to see if we need to stop.
            if (board.getPiece(currentPosition) != null) {
                break;
            }
            // move up right to next position
            y--;
            x--;
        }

        // upper left
        y = myPosition.getRow()+1;
        x = myPosition.getColumn()-1;
        while (y < 9 && x > 0) {
            currentPosition = new ChessPosition(y, x);
            ChessMove move = checkIfValidMove(board, myPosition, currentPosition, pieceColor);
            // if we could actually move, add it to validMoves.
            if (move != null) {
                validMoves.add(move);
            }
            // check the spot we just evaluated to see if we need to stop.
            if (board.getPiece(currentPosition) != null) {
                break;
            }
            // move up right to next position
            y++;
            x--;
        }

        return validMoves;
    }

    private static Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove>  validMoves = new ArrayList<>();

        int myPositionY = myPosition.getRow();
        int myPositionX = myPosition.getColumn();

        ChessPosition[] possiblePositions = new ChessPosition[8];       // 8 possible moves. any direction, by 1.

        possiblePositions[0] = new ChessPosition(myPositionY+1, myPositionX);            // up
        possiblePositions[1] = new ChessPosition(myPositionY+1, myPositionX+1);     // up right
        possiblePositions[2] = new ChessPosition(myPositionY, myPositionX+1);            // right
        possiblePositions[3] = new ChessPosition(myPositionY-1, myPositionX+1);     // down right
        possiblePositions[4] = new ChessPosition(myPositionY-1, myPositionX);           // down
        possiblePositions[5] = new ChessPosition(myPositionY-1, myPositionX-1);     // down left
        possiblePositions[6] = new ChessPosition(myPositionY, myPositionX-1);            // left
        possiblePositions[7] = new ChessPosition(myPositionY+1, myPositionX-1);     // up left

        for (ChessPosition position: possiblePositions) {

            // Check if out of bounds. if it is, move on.
            if (position.getColumn() < 9 && position.getColumn() > 0 && position.getRow() < 9 && position.getRow() > 0) {
                // Check if this is a valid move. if so, add it to validMoves.
                ChessMove move = checkIfValidMove(board, myPosition, position, pieceColor);
                if (move != null) {
                    validMoves.add(move);
                }
            }
        }

        return validMoves;
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

        // iterate through valid list to ensure that they meet criteria (within bounds, pieces there, etc.)
        for (ChessPosition position : possibleMoves) {
            // check if out of bounds.
            if (position.getColumn() < 1 || position.getColumn() > 8 || position.getRow() < 1 || position.getRow() > 8) {
                continue;       // move on to the next if out of bounds.
            }

            // Check if this is a valid move. if so, add it to validMoves.
            ChessMove move = checkIfValidMove(board, myPosition, position, pieceColor);
            if (move != null) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    // if a move is promotable, then return a collection of all moves with promotions for all pieces.
    private static Collection<ChessMove> addAllPromotions(ChessPosition myPosition, ChessPosition possiblePosition, boolean promotable) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        if (promotable) {
            validMoves.add(new ChessMove(myPosition, possiblePosition, PieceType.QUEEN));
            validMoves.add(new ChessMove(myPosition, possiblePosition, PieceType.BISHOP));
            validMoves.add(new ChessMove(myPosition, possiblePosition, PieceType.ROOK));
            validMoves.add(new ChessMove(myPosition, possiblePosition, PieceType.KNIGHT));
        } else {
            validMoves.add(new ChessMove(myPosition, possiblePosition, null));
        }

        return validMoves;
    }

    private static Collection<ChessMove> pawnMoves(ChessBoard board,
                                                   ChessPosition myPosition,
                                                   ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        int y = myPosition.getRow();
        int x = myPosition.getColumn();

        // positions for moving forward, moving forward 2, capturing right, and capturing left.
        ChessPosition forwardPosition;
        ChessPosition jumpForwardPosition;



        forwardPosition = new ChessPosition(y + 1, x);       // up 1
        jumpForwardPosition = new ChessPosition(y+2, x);    // up 2
        // Positions for if a pawn captures.
        ChessPosition[] capturePostions = {
                new ChessPosition(y+1,x+1),
                new ChessPosition(y+1,x-1)
        };

        // we can jump forward if we are starting at row 2, and the next two spaces are clear.
        if (y == 2 && board.getPiece(forwardPosition) == null && board.getPiece(jumpForwardPosition) == null) {
            validMoves.add(new ChessMove(myPosition, jumpForwardPosition, null));
        }

        // forward move. If there are no pieces there and we will remain in bounds, then:
        if (forwardPosition.getRow() < 9 && board.getPiece(forwardPosition) == null) {
            if (forwardPosition.getRow() == 8) {
                // promotable.
                validMoves.addAll(addAllPromotions(myPosition, forwardPosition, true));
            } else {
                validMoves.addAll(addAllPromotions(myPosition, forwardPosition, false));
            }
        }

        // Capture moves
        for (ChessPosition endPosition : capturePostions) {
            // if we are in bounds
            if (endPosition.getRow() > 0 &&
                    endPosition.getRow() < 9 &&
                    endPosition.getColumn() > 0 &&
                    endPosition.getColumn() < 9) {
                // and only if there is a piece there of the opposite color
                if (board.getPiece(endPosition) != null &&
                        board.getPiece(endPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    if (forwardPosition.getRow() == 8) {
                        // promotable.
                        validMoves.addAll(addAllPromotions(myPosition, forwardPosition, true));
                    } else {
                        validMoves.addAll(addAllPromotions(myPosition, forwardPosition, false));
                    }

                }
            }
        }



        return validMoves;
    }

    // logic for queen movement. As mentioned in class, functionally a combination of rook and bishop.
    private static Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        Collection<ChessMove> validMoves;

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
        int y = myPosition.getRow()+1;
        int x = myPosition.getColumn();
        while (y < 9) {
            currentPosition = new ChessPosition(y, x);
            ChessMove move = checkIfValidMove(board, myPosition, currentPosition, pieceColor);
            // if we could actually move, add it to validMoves.
            if (move != null) {
                validMoves.add(move);
            }
            // check the spot we just evaluated to see if we need to stop.
            if (board.getPiece(currentPosition) != null) {
                break;
            }
            y++;
        }

        // right
        y = myPosition.getRow();
        x = myPosition.getColumn()+1;
        while (x < 9) {
            currentPosition = new ChessPosition(y, x);
            ChessMove move = checkIfValidMove(board, myPosition, currentPosition, pieceColor);
            // if we could actually move, add it to validMoves.
            if (move != null) {
                validMoves.add(move);
            }
            // check the spot we just evaluated to see if we need to stop.
            if (board.getPiece(currentPosition) != null) {
                break;
            }
            x++;
        }

        // down
        y = myPosition.getRow()-1;
        x = myPosition.getColumn();
        while (y > 0) {
            currentPosition = new ChessPosition(y, x);
            ChessMove move = checkIfValidMove(board, myPosition, currentPosition, pieceColor);
            // if we could actually move, add it to validMoves.
            if (move != null) {
                validMoves.add(move);
            }
            // check the spot we just evaluated to see if we need to stop.
            if (board.getPiece(currentPosition) != null) {
                break;
            }
            y--;
        }

        // left
        y = myPosition.getRow();
        x = myPosition.getColumn()-1;
        while (x > 0) {
            currentPosition = new ChessPosition(y, x);
            ChessMove move = checkIfValidMove(board, myPosition, currentPosition, pieceColor);
            // if we could actually move, add it to validMoves.
            if (move != null) {
                validMoves.add(move);
            }
            // check the spot we just evaluated to see if we need to stop.
            if (board.getPiece(currentPosition) != null) {
                break;
            }
            x--;
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

    // Created by intelliJ as per assignment directions, then edited.
    @Override
    public String toString() {
        return String.format("{%s,%s}", pieceColor, type);
    }
}
