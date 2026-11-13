package client;

import chess.*;
import ui.ClientREPL;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        // run the repl here.
        ClientREPL clientREPL = new ClientREPL();
        clientREPL.run();
        
    }

}
