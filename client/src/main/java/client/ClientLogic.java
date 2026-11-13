package client;

import chess.ChessGame;
import model.AuthData;
import records.LoginData;
import records.RegisterData;

public class ClientLogic {

    // server connection information
    private final String host;
    private final int port;

    // The auth data, or whether someone is logged in.
    // kept here, because the specific authToken is not a concern to the repl.
    private AuthData authData;

    public ClientLogic() {
        this.host = "localhost";
        this.port = 8080;
        // initialize with null so that we can't use it until defined.
        this.authData = null;
    }

    /**
     * Register user.
     *
     * @param username Username String
     * @param password Password String
     * @param email    Email String
     * @return String containing message
     */
    public void register(String username, String password, String email) throws Exception {
        RegisterData data = new RegisterData(username, password, email);
        authData = new ServerFacade(host, port).registerUser(data);
    }

    /**
     * Log in the user
     *
     * @param username username string
     * @param password password string
     */
    public void login(String username, String password) throws Exception {
        LoginData loginData = new LoginData(username, password);
        authData = new ServerFacade(host, port).loginUser(loginData);
    }

    public void logout() throws Exception {
        ServerFacade facade = new ServerFacade(host, port);
        checkAuthNotNull();
        facade.logoutUser(authData.authToken());
        // clear authData
        authData = null;
    }

    public int createGame(String gameName) throws Exception {
        checkAuthNotNull();
        return new ServerFacade(host, port).createGame(gameName, authData.authToken());
    }

    public void joinGame(int gameID, String team) throws Exception {
        checkAuthNotNull();
        // convert to team color. team is
        // Guaranteed to be white or black due to checking the input at the repl.
        ChessGame.TeamColor teamColor;
        if (team.equals("WHITE")) {
            teamColor = ChessGame.TeamColor.WHITE;
        } else {
            teamColor = ChessGame.TeamColor.BLACK;
        }
        ServerFacade facade = new ServerFacade(host, port);
        facade.playGame(teamColor, gameID, authData.authToken());
    }

    // for checking.
    private void checkAuthNotNull() throws Exception {
        if (authData == null) {
            throw new InvalidAuthException("You are not logged in or otherwise have an invalid authentication");
        }
    }
}
