package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;

public class StorageService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public StorageService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void clear() throws DataAccessException {
        userDAO.removeUsers();
        authDAO.removeAllAuthData();
        gameDAO.removeGames();
    }
}
