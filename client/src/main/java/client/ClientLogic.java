package client;

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
        facade.logoutUser(authData.authToken());
    }
}
