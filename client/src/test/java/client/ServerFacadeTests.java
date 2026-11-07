package client;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import records.LoginData;
import records.RegisterData;
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
        facade.clearData();
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
        RegisterData registerData = new RegisterData(username, password, email);

        try {
            // register user.
            AuthData authData = facade.registerUser(registerData);
            assertEquals(username, authData.username());
            assertNotNull(authData.authToken());
            assertInstanceOf(String.class, authData.authToken());

            // do we need to assert everything was added correctly to the database?
            // No, and this is why (though feel free to check;
            // we know that database calls work correctly from prior tests. This is just testing
            // if we can make a good call to the server, and get the expected result.
            // If we get the right result, then we can assume everything that happened serverside is correct.

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void registerUserBadRequestTest() {
        String username = "jim";
        String password = "";       // an empty password should be flagged as a bad request.
        String email = "jim@gmail.com";
        RegisterData registerData = new RegisterData(username, password, email);

        try {
            // register user. should throw a bad request exception with the message "Error: bad request"
            assertThrows(BadRequestException.class, () -> facade.registerUser(registerData));

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void registerUserUsernameTakenTest() {
        RegisterData userData = new RegisterData("jim", "1234", "jim@gmail.com");
        RegisterData userData2 = new RegisterData("jim", "0", "who@gmail.com");

        try {
            // register jim.
            facade.registerUser(userData);
            // register another user as jim. should throw an exception.
            assertThrows(UsernameTakenException.class, () -> facade.registerUser(userData));

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void loginUserSuccessfulTest() {
        // If we register a user, we should be able to get a successful log in.
        String username = "jim";
        String password = "1234";
        String email = "jim@gmail.com";
        RegisterData registerData = new RegisterData(username, password, email);

        try {
            // register user.
            AuthData registerAuthData = facade.registerUser(registerData);

            AuthData loginAuthData = facade.loginUser(new LoginData(username, password));

            assertEquals(username, loginAuthData.username());
            assertEquals(registerAuthData.username(), loginAuthData.username());
            assertNotNull(loginAuthData.authToken());
            assertInstanceOf(String.class, loginAuthData.authToken());

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void loginBadRequestTest() {
        // If we register a user, we should be able to get a successful log in.
        String username = "jim";
        String password = "1234";
        String email = "jim@gmail.com";
        RegisterData registerData = new RegisterData(username, password, email);

        try {
            // register user.
            AuthData registerAuthData = facade.registerUser(registerData);

            assertThrows(BadRequestException.class, () -> facade.loginUser(new LoginData(username, "")));

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void loginBadUsernameTest() {
        // If we register a user, we should be able to get a successful log in.
        String username = "jim";
        String password = "1234";
        String email = "jim@gmail.com";
        RegisterData registerData = new RegisterData(username, password, email);

        try {
            // register user.
            AuthData registerAuthData = facade.registerUser(registerData);

            assertThrows(InvalidLoginException.class, () -> facade.loginUser(new LoginData("kim", password)));

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void loginBadPasswordTest() {
        // If we register a user, we should be able to get a successful log in.
        String username = "jim";
        String password = "1234";
        String email = "jim@gmail.com";
        RegisterData registerData = new RegisterData(username, password, email);

        try {
            // register user.
            AuthData registerAuthData = facade.registerUser(registerData);

            assertThrows(InvalidLoginException.class, () -> facade.loginUser(new LoginData(username, "0")));

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void logoutSuccessTest() {
        try {
            // register user
            AuthData authData = facade.registerUser(
                    new RegisterData("jim", "1234", "jim@gmail.com")
            );

            // log out user.
            facade.logoutUser(authData.authToken());

            // if no exception was thrown, then we are all good.
            assertTrue(true);
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    // write tests for clearing data when you can. For now, we can see that it works according to our needs.

}
