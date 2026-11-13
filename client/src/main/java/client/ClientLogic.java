package client;

import model.AuthData;
import records.RegisterData;

public class ClientLogic {

    // server connection information
    private final String host = "localhost";
    private final int port = 8080;

    /**
     * Register user.
     *
     * @param username
     * @param password
     * @param email
     * @return String containing message
     */
    public String register(String username, String password, String email) throws Exception {
        RegisterData data = new RegisterData(username, password, email);
        AuthData authData = new ServerFacade(host, port).registerUser(data);
        // return authToken.
        return authData.authToken();
    }
}
