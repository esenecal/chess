package client;

import chess.ChessGame;
import model.AuthData;
import model.GameMetaData;
import model.UserData;
import org.junit.jupiter.api.*;
import records.LoginData;
import records.RegisterData;
import server.Server;

import java.util.Collection;

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
            facade.clearData();

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
            facade.clearData();

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
            facade.clearData();

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
            facade.clearData();

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
            facade.clearData();

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
            facade.clearData();

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
            facade.clearData();

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
            facade.clearData();

            // register user
            AuthData authData = facade.registerUser(
                    new RegisterData("jim", "1234", "jim@gmail.com")
            );

            // log out user.
            facade.logoutUser(authData.authToken());

            // if no exception was thrown, then we are all good. We assume everything worked server side,
            // as that was all tested.
            assertTrue(true);

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void logoutNoAuthTest() {
        try {
            facade.clearData();

            // log out nonexistent user.
            // if no exception was thrown, then we are all good.
            assertThrows(InvalidAuthException.class, () -> facade.logoutUser("12345"));

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void createGameSuccessfulTest() {
        try {
            facade.clearData();

            AuthData authData = facade.registerUser(
                    new RegisterData("jim", "1234", "jim@gmail.com")
            );

            int gameID = facade.createGame("game", authData.authToken());
            assertEquals(1, gameID);

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void createGameBadRequestTest() {
        try {
            facade.clearData();

            AuthData authData = facade.registerUser(
                    new RegisterData("fred", "4", "fred@gmail.com")
            );

            // create game with an invalid name.
            assertThrows(BadRequestException.class, () -> facade.createGame("", authData.authToken()));

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void createGameInvalidAuthTest() {
        try {
            facade.clearData();

            AuthData authData = facade.registerUser(
                    new RegisterData("fred", "4", "fred@gmail.com")
            );

            // create game with an invalid name.
            assertThrows(InvalidAuthException.class, () -> facade.createGame("game1", "1"));

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void listGamesSuccessfulTest() {
        try {
            facade.clearData();

            // Create a user and two games.
            AuthData authData = facade.registerUser(
                    new RegisterData("jim", "1234", "jim@gmail.com")
            );
            int gameOneID = facade.createGame("game1", authData.authToken());
            int gameTwoID = facade.createGame("game2", authData.authToken());

            // get list of games.
            Collection<GameMetaData> gameList = facade.listGames(authData.authToken());

            assertTrue(gameList.contains(
                    new GameMetaData(gameOneID, null, null, "game1")));
            assertTrue(gameList.contains(
                    new GameMetaData(gameTwoID, null, null, "game2")));

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void listGamesInvalidAuthTest() {
        try {
            facade.clearData();

            // Create a user and a game.
            AuthData authData = facade.registerUser(
                    new RegisterData("Joe", "0000", "joe@gmail.com")
            );
            int gameOneID = facade.createGame("game1", authData.authToken());

            // get list of games with wrong authData.
            assertThrows(InvalidAuthException.class, () -> facade.listGames("1234"));

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void playGameSuccessfulTest() {
        try {
            facade.clearData();

            // create a user and a game
            AuthData authData = facade.registerUser(
                    new RegisterData("jim", "0000", "jim@gmail.com")
            );
            int gameOneID = facade.createGame("game1", authData.authToken());

            // now join the game. We assume that it all worked well if no error is thrown, as we know that
            // the server works well.
            facade.playGame(ChessGame.TeamColor.WHITE, gameOneID, authData.authToken());

            assertTrue(true);

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void playGameBadRequestTest() {
        try {
            facade.clearData();

            // create a user and a game
            AuthData authData = facade.registerUser(
                    new RegisterData("jim", "0000", "jim@gmail.com")
            );
            int gameOneID = facade.createGame("game1", authData.authToken());

            // now join the game. We assume that it all worked well if no error is thrown, as we know that
            // the server works well.
            assertThrows(BadRequestException.class,
                    () -> facade.playGame(ChessGame.TeamColor.WHITE, null, authData.authToken()));

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void playGameInvalidAuthTest() {
        try {
            facade.clearData();

            // create a user and a game
            AuthData authData = facade.registerUser(
                    new RegisterData("jim", "1234", "jim@gmail.com")
            );
            int gameOneID = facade.createGame("game1", authData.authToken());

            // now join the game. We assume that it all worked well if no error is thrown, as we know that
            // the server works well.
            assertThrows(BadRequestException.class,
                    () -> facade.playGame(ChessGame.TeamColor.WHITE, gameOneID, "1"));

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void playGameTakenTest() {
        try {
            facade.clearData();

            AuthData jimAuthData = facade.registerUser(
                    new RegisterData("jim", "1234", "jim@gmail.com")
            );
            AuthData fredAuthData = facade.registerUser(
                    new RegisterData("fred", "0000", "fred@gmail.com")
            );
            int gameOneID = facade.createGame("game1", jimAuthData.authToken());
            // fred and jim both join the game, jim as white and fred as black.
            facade.playGame(ChessGame.TeamColor.WHITE, gameOneID, jimAuthData.authToken());
            facade.playGame(ChessGame.TeamColor.BLACK, gameOneID, fredAuthData.authToken());

            // here comes Kyle, who tries to join the game as white and as black.
            AuthData kyleAuthData = facade.registerUser(
                    new RegisterData("kyle", "1", "kyle@gmail.com")
            );

            assertThrows(GameTakenException.class,
                    () -> facade.playGame(ChessGame.TeamColor.WHITE, gameOneID, kyleAuthData.authToken()));
            assertThrows(GameTakenException.class,
                    () -> facade.playGame(ChessGame.TeamColor.BLACK, gameOneID, kyleAuthData.authToken()));

            facade.clearData();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    // write tests for clearing data when you can. For now, we can see that it works according to our needs.

}
