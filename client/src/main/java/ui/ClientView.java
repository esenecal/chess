package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ClientState;
import model.GameData;
import model.GameMetaData;

/**
 * Functions for actually displaying various things. Set text to null and newline to true to just
 * display a newline.
 */
public class ClientView {

    private static final char[] columnLabel = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

    public static void displayText(String text, boolean newline) {
        if (!(text == null)) {
            System.out.print(text);
        }
        if (newline) {
            System.out.println();
        }
    }

    public static void displayError(String message) {
        System.err.println(message);
    }

    public static void displayHelp(ClientState state) {
        if (state == ClientState.LOGGED_OUT) {
            displayText(
                    // bless intelliJ for teaching me that this could be a text block.
                    """
                            Commands:
                            register - register account with username, password, and email.
                            login - login with username and password, and start playing chess!
                            help - display valid commands. Call this at any point.
                            quit - stop the application.
                            
                            Written in 2026 for BYU CS 240. Go Cougs!
                            """,
                    true
            );
        } else {
            displayText("""
                            Commands:
                            create - create a game with name.
                            list - list all games that are in the server.
                            play - join a game as white or black.
                            observe - watch a game without playing.
                            help - display valid commands. Call this at any point.
                            logout - log out and return to home screen.
                            """,
                    true);
        }
    }

    public static String formatGameMetaData(GameMetaData game) {
        String whiteUsername;
        String blackUsername;
        if (game.whiteUsername() == null) {
            whiteUsername = "NO PLAYER";
        } else {
            whiteUsername = game.whiteUsername();
        }

        if (game.blackUsername() == null) {
            blackUsername = "NO PLAYER";
        } else {
            blackUsername = game.blackUsername();
        }
        return "[" + game.gameID() + "] - " + game.gameName() +
                " | " + whiteUsername + " | " + blackUsername;
    }

    public static void renderBoard(GameData game, String username) {
        if (username.equals(game.whiteUsername())) {
            renderWhiteBoard(game.game());
        } else {
            renderBlackBoard(game.game());
        }
    }

    private static void renderWhiteBoard(ChessGame game) {
        // iterate through the board.
        ChessBoard board = game.getBoard();
        displayText(EscapeSequences.RESET_TEXT_COLOR, false);
        displayText(EscapeSequences.RESET_BG_COLOR, false);
        // start at top.
        displayText(EscapeSequences.SET_BG_COLOR_BLACK + "   ", false);
        for (int x = 0; x < 8; x++) {
            displayText(EscapeSequences.SET_BG_COLOR_BLACK + " " +
                    columnLabel[x] + EscapeSequences.ALMOST_EMPTY, false);
        }
        displayText(EscapeSequences.SET_BG_COLOR_BLACK + "   ", false);
        displayText(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR, true);
        for (int y = 8; y > 0; y--) {
            displayText(EscapeSequences.SET_BG_COLOR_BLACK + " " + y + " ", false);
            for (int x = 1; x < 9; x++) {
                printSquare(board, y, x);
            }
            displayText(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR, false);
            displayText(EscapeSequences.SET_BG_COLOR_BLACK + " " + y + " ", false);
            displayText(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR, true);
        }
        displayText(EscapeSequences.SET_BG_COLOR_BLACK + "   ", false);
        for (int x = 0; x < 8; x++) {
            displayText(EscapeSequences.SET_BG_COLOR_BLACK + " " +
                    columnLabel[x] + EscapeSequences.ALMOST_EMPTY, false);
        }
        displayText(EscapeSequences.SET_BG_COLOR_BLACK + "   ", false);
        displayText(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR, true);
    }

    private static void renderBlackBoard(ChessGame game) {
        // iterate through the board.
        ChessBoard board = game.getBoard();
        displayText(EscapeSequences.RESET_TEXT_COLOR, false);
        displayText(EscapeSequences.RESET_BG_COLOR, false);

        // start at top.
        displayText(EscapeSequences.SET_BG_COLOR_BLACK + "   ", false);
        for (int x = 7; x > -1; x--) {
            displayText(EscapeSequences.SET_BG_COLOR_BLACK + " " +
                    columnLabel[x] + EscapeSequences.ALMOST_EMPTY, false);
        }
        displayText(EscapeSequences.SET_BG_COLOR_BLACK + "   ", false);
        displayText(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR, true);
        for (int y = 1; y < 9; y++) {
            displayText(EscapeSequences.SET_BG_COLOR_BLACK + " " + y + " ", false);
            for (int x = 8; x > 0; x--) {
                printSquare(board, y, x);
            }
            displayText(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR, false);
            displayText(EscapeSequences.SET_BG_COLOR_BLACK + " " + y + " ", false);
            displayText(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR, true);
        }
        displayText(EscapeSequences.SET_BG_COLOR_BLACK + "   ", false);
        for (int x = 7; x > -1; x--) {
            displayText(EscapeSequences.SET_BG_COLOR_BLACK + " " +
                    columnLabel[x] + EscapeSequences.ALMOST_EMPTY, false);
        }
        displayText(EscapeSequences.SET_BG_COLOR_BLACK + "   ", false);
        displayText(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR, true);
    }

    private static void printSquare(ChessBoard board, int y, int x) {
        ChessPiece piece = board.getPiece(new ChessPosition(y, x));
        String pieceIcon = getPieceIcon(piece);
        // odd y and odd x, and even y and even x are white squares. Actually, this is wrong.
        if ((y % 2 == 1 && x % 2 == 1) || (y % 2 == 0 && x % 2 == 0)) {
            pieceIcon = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + pieceIcon;
        } else {
            pieceIcon = EscapeSequences.SET_BG_COLOR_DARK_GREY + pieceIcon;
        }
        displayText(pieceIcon, false);
        // clear color.
    }

    private static String getPieceIcon(ChessPiece piece) {
        String pieceIcon;
        String pieceColor;
        if (piece == null) {
            // no piece. set to empty.
            pieceIcon = EscapeSequences.EMPTY;
            return pieceIcon;
        } else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            pieceColor = EscapeSequences.SET_TEXT_COLOR_RED;
            switch (piece.getPieceType()) {
                case KING:
                    pieceIcon = EscapeSequences.WHITE_KING;
                    break;
                case QUEEN:
                    pieceIcon = EscapeSequences.WHITE_QUEEN;
                    break;
                case BISHOP:
                    pieceIcon = EscapeSequences.WHITE_BISHOP;
                    break;
                case KNIGHT:
                    pieceIcon = EscapeSequences.WHITE_KNIGHT;
                    break;
                case ROOK:
                    pieceIcon = EscapeSequences.WHITE_ROOK;
                    break;
                case PAWN:
                    pieceIcon = EscapeSequences.WHITE_PAWN;
                    break;
                default:
                    pieceIcon = EscapeSequences.EMPTY;
                    break;
            }
        } else {
            pieceColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
            switch (piece.getPieceType()) {
                case KING:
                    pieceIcon = EscapeSequences.BLACK_KING;
                    break;
                case QUEEN:
                    pieceIcon = EscapeSequences.BLACK_QUEEN;
                    break;
                case BISHOP:
                    pieceIcon = EscapeSequences.BLACK_BISHOP;
                    break;
                case KNIGHT:
                    pieceIcon = EscapeSequences.BLACK_KNIGHT;
                    break;
                case ROOK:
                    pieceIcon = EscapeSequences.BLACK_ROOK;
                    break;
                case PAWN:
                    pieceIcon = EscapeSequences.BLACK_PAWN;
                    break;
                default:
                    pieceIcon = EscapeSequences.EMPTY;
                    break;
            }
        }

        return pieceColor + pieceIcon;
    }
}
