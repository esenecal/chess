package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StorageServiceTest {

    @Test
    public void clearPositiveTest() {

        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        // create a user
        UserData userData = new UserData("Jim", "1234", "jim@jim.com");
        // create an auth
        AuthData authData = new AuthData(Util.generateAuthToken(), userData.username());
        // create a game
        GameData gameData = new GameData(0, "Jim", "Fred", "game0", new ChessGame());

        try {
            userDAO.createUser(userData);
            userDAO.createAuth(authData);
            gameDAO.createGame(gameData);
        } catch (DataAccessException ex) {
            fail("Exception thrown");
        }

        
    }
}
