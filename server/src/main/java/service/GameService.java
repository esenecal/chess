package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.NonexistentAuthException;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import model.AuthData;
import model.GameData;
import requestresult.CreateGameRequest;
import requestresult.CreateGameResult;
import requestresult.ListGamesRequest;
import requestresult.ListGamesResult;

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

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException {

        // verify authentication. see listGames
        AuthData authData = authDAO.getAuth(request.authToken());
        if (authData == null) {
            throw new NonexistentAuthException("Error: unauthorized");
        }

        // Set the game ID to 0 as a placeholder. The DAO or database will assign the proper ID that works
        // with the implemented storage system; that is an concern that cannot be well addressed here,
        // as GameService does not know anything about the storage implementation, and different storage
        // implementations may have different schema for determining identification. So, we define something
        // arbitrary and let the DAO handle it.
        GameData game = new GameData(0, null, null, request.gameName(), new ChessGame());

        int gameID = gameDAO.createGame(game);

        return new CreateGameResult(gameID);
    }

}
