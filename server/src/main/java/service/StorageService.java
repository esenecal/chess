package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class StorageService {

    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public StorageService(UserDAO userDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    void clear() throws DataAccessException {
        userDAO.removeUsers();
        userDAO.removeAllAuthData();
        gameDAO.removeGames();
    }
}
