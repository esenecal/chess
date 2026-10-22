package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
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
