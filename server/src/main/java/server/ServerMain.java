package server;

import chess.*;

public class ServerMain {
    public static void main(String[] args) {
        // starter code from Phase 3: Getting Started
        int port = 8080;
        // instantiate server object, call run method.
        Server server = new Server();
        server.run(port);

        System.out.println("240 Chess Server running on port " + port);
    }
}
