package service;

import dataaccess.DataAccessException;
import dataaccess.NonexistentAuthException;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import model.AuthData;
import model.GameData;
import requestresult.ListGamesRequest;
import requestresult.ListGamesResult;

import java.util.Collection;

public class GameService {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    /**
     * Get list of all GameData
     *
     * @param request ListGamesRequest containing authToken
     * @return ListGamesResult containing collection of Game Data.
     */
    public ListGamesResult listGames(ListGamesRequest request) throws DataAccessException {

        // verify authentication
        AuthData authData = authDAO.getAuth(request.authToken());
        // User not logged in; throw exception.
        if (authData == null) {
            throw new NonexistentAuthException("Error: unauthorized");
        }

        // get all keys associated with GameData, then place the collection in a result record.
        return new ListGamesResult(gameDAO.listGames());
    }
}
