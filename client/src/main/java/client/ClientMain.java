package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ClientREPL repl = new ClientREPL();
        repl.run();
        System.out.println("♕ 240 Chess Client: " + piece);
    }

}
