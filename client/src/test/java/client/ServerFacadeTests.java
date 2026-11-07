package client;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    // See masteryLS phase 5 for facade implementation.
    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        // start the facade, communicating with the server over the correct port.
        facade = new ServerFacade("localhost", port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerUserSuccessTest() {
        String username = "jim";
        String password = "1234";
        String email = "jim@gmail.com";
        UserData userData = new UserData(username, password, email);

        try {
            // register user.
            AuthData authData = facade.registerUser(userData);
            assertEquals(username, authData.username());
            assertNotNull(authData.authToken());
            assertInstanceOf(String.class, authData.authToken());

            // do we need to assert everything was added correctly to the database?
            // No, and this is why (though feel free to check;
            // we know that database calls work correctly from prior tests. This is just testing
            // if we can make a good call to the server, and get the expected result.
            // If we get the right result, then we can assume everything that happened serverside is correct.

        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

}
