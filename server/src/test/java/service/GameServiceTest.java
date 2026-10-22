package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import requestresult.CreateGameRequest;
import requestresult.CreateGameResult;
import requestresult.ListGamesRequest;
import requestresult.ListGamesResult;

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
            // add games to storage
            gameDAO.createGame(game1);
            gameDAO.createGame(game2);

            // Log user in.
            authDAO.createAuth(authData);

            // Get all games
            GameService gameService = new GameService(authDAO, gameDAO);
            ListGamesResult result = gameService.listGames(new ListGamesRequest(authData.authToken()));

            // Verify.
            assertTrue(result.gameList().contains(game1));
            assertTrue(result.gameList().contains(game2));

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
