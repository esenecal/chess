package client;

import model.AuthData;
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
     * @param username
     * @param password
     * @param email
     * @return String containing message
     */
    public ClientState register(String username, String password, String email) throws Exception {
        RegisterData data = new RegisterData(username, password, email);
        authData = new ServerFacade(host, port).registerUser(data);
        return ClientState.LOGGED_IN;
    }
}
