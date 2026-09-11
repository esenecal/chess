package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    // ROW IS Y, COLUMN IS X
    private final int row;  // video: https://www.youtube.com/watch?v=mTtK8iRXsZo
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return col;
    }

    @Override
    public String toString() {      // returns a ChessPosition as a string
        return String.format("[%d,%d]", row, col);        // https://www.youtube.com/watch?v=mTtK8iRXsZo
    }

    // Created with IntelliJ as per assignment directions
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessPosition position)) {
            return false;
        }
        return row == position.row && col == position.col;
    }

    // Created with IntelliJ as per assignment directions
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
