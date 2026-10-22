package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StorageServiceTest {

    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();

    @Test
    public void clearPositiveTest() {
        // 100% line coverage for StorageService.

        // create a user
        UserData userData = new UserData("Jim", "1234", "jim@jim.com");
        // create an auth
        AuthData authData = new AuthData(Util.generateAuthToken(), userData.username());
        // create a game
        GameData gameData = new GameData(0, "Jim", "Fred", "game0", new ChessGame());

        try {
            userDAO.createUser(userData);
            authDAO.createAuth(authData);
            gameDAO.createGame(gameData);
        } catch (DataAccessException ex) {
            fail("Exception thrown");
        }

        // clear
        StorageService storageService = new StorageService(userDAO, authDAO, gameDAO);

        try {
            storageService.clear();

            // query and ensure everything is null
            assertNull(userDAO.getUser(userData.username()));
            assertNull(authDAO.getAuth(authData.authToken()));
            assertNull(gameDAO.getGame(gameData.gameID()));
        } catch (Exception ex) {
            fail("Exception thrown");
        }

    }
}
