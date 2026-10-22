package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import requestresult.*;

import model.GameData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    // Initialize memory. Placed here.
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();

    @Test
    public void listGamesPositiveTest() {

        // Games
        GameData game1 = new GameData(1, "jim", "kim", "game1", new ChessGame());
        GameData game2 = new GameData(2, "fred", "john", "game2", new ChessGame());

        // Authentication
        AuthData authData = new AuthData(Util.generateAuthToken(), "jim");

        try {
            // add games to storage. Remember, createGame assigns its custom ID according to db schema;
            // in this case, we simply created the games with the ID they will be assigned to.
            gameDAO.createGame(game1);
            gameDAO.createGame(game2);

            // Log user in.
            authDAO.createAuth(authData);

            // Get all games
            GameService gameService = new GameService(authDAO, gameDAO);
            ListGamesResult result = gameService.listGames(new ListGamesRequest(authData.authToken()));

            // Verify.
            GameMetaData game1Data = new GameMetaData(game1.gameID(), game1.whiteUsername(),
                    game1.blackUsername(), game1.gameName());
            GameMetaData game2Data = new GameMetaData(game2.gameID(), game2.whiteUsername(),
                    game2.blackUsername(), game2.gameName());
            assertTrue(result.gameList().contains(game1Data));
            assertTrue(result.gameList().contains(game2Data));

        } catch (Exception ex) {
            clearStorage();
            fail("Exception thrown");
        }

        clearStorage();

    }

    @Test
    public void listGamesNoAuthTest() {
        // largely the same to the other listGames test, just we do not log in the user.

        // Games
        GameData game1 = new GameData(1, "jim", "kim", "game1", new ChessGame());
        GameData game2 = new GameData(2, "fred", "john", "game2", new ChessGame());

        // Authentication
        AuthData authData = new AuthData(Util.generateAuthToken(), "jim");

        try {
            // add games to storage
            gameDAO.createGame(game1);
            gameDAO.createGame(game2);

            // Get all games
            GameService gameService = new GameService(authDAO, gameDAO);

            // Throw an error due to no authorization.
            assertThrows(NonexistentAuthException.class,
                    () -> gameService.listGames(new ListGamesRequest(authData.authToken())));

        } catch (Exception ex) {
            clearStorage();
            fail("Exception thrown");
        }

        clearStorage();
    }

    @Test
    public void createGamePositiveTest() {
        // log in a user.
        // we do not use UserService because then we would need to create a user and everything.
        // This allows us to only create the data that we need.
        // It also avoids this test failing if any of the other service methods are not correct.
        AuthData authData = new AuthData(Util.generateAuthToken(), "jim");
        try {
            authDAO.createAuth(authData);
        } catch (Exception ex) {
            fail("Exception thrown: " + ex.getMessage());
        }

        // create a game.
        try {
            // Create game called game1.
            String gameName = "game1";
            CreateGameResult result = new GameService(authDAO, gameDAO)
                    .createGame(new CreateGameRequest(authData.authToken(), gameName));

            // Check that we got something at the ID number in result, with the same name, no players,
            // and an empty game.
            GameData resultGame = gameDAO.getGame(result.gameID());
            assertNotNull(resultGame);
            assertEquals(gameName, resultGame.gameName());
            assertNull(resultGame.whiteUsername());
            assertNull(resultGame.blackUsername());
            assertEquals(new ChessGame(), resultGame.game());
        } catch (Exception ex) {
            fail("Exception thrown: " + ex.getMessage());
        }

        clearStorage();
    }

    @Test
    public void createGameNoAuthTest() {

        // Create authData but do not log the user in.
        AuthData authData = new AuthData(Util.generateAuthToken(), "jim");

        // create a game.
        try {
            // Create game called game1.
            String gameName = "game1";
            CreateGameRequest request = new CreateGameRequest(authData.authToken(), gameName);
            GameService service = new GameService(authDAO, gameDAO);

            // it should throw an error.
            assertThrows(NonexistentAuthException.class, () -> service.createGame(request));

        } catch (Exception ex) {
            fail("Exception thrown: " + ex.getMessage());
        }

        clearStorage();
    }

    @Test
    public void joinGameWhiteTest() {

        // this test provides the template for other joinGame tests; others are modifications of this one.

        // create a game and an authorized user.
        GameData game = new GameData(1, null, null, "game1", new ChessGame());
        AuthData authData = new AuthData(Util.generateAuthToken(), "jim");
        ChessGame.TeamColor playerColor = ChessGame.TeamColor.WHITE;

        try {
            // add to storage. Remember, the game ID will be overwritten, but it should remain 1, as this is the first.
            gameDAO.createGame(game);
            authDAO.createAuth(authData);

            JoinGameRequest request = new JoinGameRequest(authData.authToken(), playerColor, game.gameID());
            // join the game.
            GameService gameService = new GameService(authDAO, gameDAO);
            gameService.joinGame(request);
            GameData updatedGame = gameDAO.getGame(game.gameID());

            // all information in the game must be the same, except the white team username should be jim.
            assertEquals(game.gameID(), updatedGame.gameID());
            assertEquals(authData.username(), updatedGame.whiteUsername());
            assertEquals(game.blackUsername(), updatedGame.blackUsername());
            assertEquals(game.gameName(), updatedGame.gameName());
            assertEquals(game.game(), updatedGame.game());

        } catch (Exception ex) {
            fail("Exception thrown: " + ex.getMessage());
        }

        clearStorage();

    }

    @Test
    public void joinGameBlackTest() {

        // this test provides the template for other joinGame tests; others are modifications of this one.

        // create a game and an authorized user.
        GameData game = new GameData(1, null, null, "game1", new ChessGame());
        AuthData authData = new AuthData(Util.generateAuthToken(), "jim");
        ChessGame.TeamColor playerColor = ChessGame.TeamColor.BLACK;

        try {
            // add to storage. Remember, the game ID will be overwritten, but it should remain 1, as this is the first.
            gameDAO.createGame(game);
            authDAO.createAuth(authData);

            JoinGameRequest request = new JoinGameRequest(authData.authToken(), playerColor, game.gameID());
            // join the game.
            GameService gameService = new GameService(authDAO, gameDAO);
            gameService.joinGame(request);
            GameData updatedGame = gameDAO.getGame(game.gameID());

            // all information in the game must be the same, except the black team username should be jim.
            assertEquals(game.gameID(), updatedGame.gameID());
            assertEquals(game.whiteUsername(), updatedGame.whiteUsername());
            assertEquals(authData.username(), updatedGame.blackUsername());
            assertEquals(game.gameName(), updatedGame.gameName());
            assertEquals(game.game(), updatedGame.game());

        } catch (Exception ex) {
            fail("Exception thrown: " + ex.getMessage());
        }

        clearStorage();

    }

    /**
     * Invalid authorization.
     */
    @Test
    public void joinGameNoAuthTest() {

        // create a game
        GameData game = new GameData(1, null, null, "game1", new ChessGame());
        AuthData authData = new AuthData(Util.generateAuthToken(), "jim");
        ChessGame.TeamColor playerColor = ChessGame.TeamColor.WHITE;

        try {
            // add to storage. Do not add auth.
            gameDAO.createGame(game);

            JoinGameRequest request = new JoinGameRequest(authData.authToken(), playerColor, game.gameID());
            // join the game.
            GameService gameService = new GameService(authDAO, gameDAO);
            assertThrows(NonexistentAuthException.class, () -> gameService.joinGame(request));

        } catch (Exception ex) {
            fail("Incorrect exception thrown: " + ex.getMessage());
        }

        clearStorage();
    }

    @Test
    public void joinGameNoGameTest() {

        // create a game
        AuthData authData = new AuthData(Util.generateAuthToken(), "jim");
        ChessGame.TeamColor playerColor = ChessGame.TeamColor.WHITE;

        try {
            // No game added.
            authDAO.createAuth(authData);

            JoinGameRequest request = new JoinGameRequest(authData.authToken(), playerColor, 1);
            // join the game.
            GameService gameService = new GameService(authDAO, gameDAO);
            assertThrows(NonexistentGameException.class, () -> gameService.joinGame(request));

        } catch (Exception ex) {
            fail("Incorrect exception thrown: " + ex.getMessage());
        }

        clearStorage();
    }

    @Test
    public void joinGameTakenWhiteTest() {

        // create a game with a white player already, and an authorized user.
        GameData game = new GameData(1, "max", null, "game1", new ChessGame());
        AuthData authData = new AuthData(Util.generateAuthToken(), "jim");
        ChessGame.TeamColor playerColor = ChessGame.TeamColor.WHITE;

        try {
            // add to storage
            gameDAO.createGame(game);
            authDAO.createAuth(authData);

            testGameTaken(authData.authToken(), playerColor, game.gameID());
        } catch (Exception ex) {
            fail("Exception thrown: " + ex.getMessage());
        }
        clearStorage();
    }

    @Test
    public void joinGameTakenBlackTest() {

        // create a game with a black player already, and an authorized user.
        GameData game = new GameData(1, null, "max", "game1", new ChessGame());
        AuthData authData = new AuthData(Util.generateAuthToken(), "jim");
        ChessGame.TeamColor playerColor = ChessGame.TeamColor.BLACK;

        try {
            // add to storage
            gameDAO.createGame(game);
            authDAO.createAuth(authData);

            testGameTaken(authData.authToken(), playerColor, game.gameID());
        } catch (Exception ex) {
            fail("Exception thrown: " + ex.getMessage());
        }
        clearStorage();
    }

    private void testGameTaken(String authToken, ChessGame.TeamColor playerColor, int gameID) {
        JoinGameRequest request = new JoinGameRequest(authToken, playerColor, gameID);
        // join the game. It should throw a game taken exception.
        GameService gameService = new GameService(authDAO, gameDAO);
        assertThrows(GameTakenException.class, () -> gameService.joinGame(request));
    }

    private void clearStorage() {
        try {
            // Clear storage for next test.
            gameDAO.removeGames();
            authDAO.removeAllAuthData();
        } catch (Exception ex) {
            fail("Exception thrown when clearing storage");
        }
    }
}
