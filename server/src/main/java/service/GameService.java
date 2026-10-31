package service;

import chess.ChessGame;

import java.util.ArrayList;
import java.util.Collection;

import dataaccess.DataAccessException;
import dataaccess.NonexistentAuthException;
import dataaccess.NonexistentGameException;
import dataaccess.GameTakenException;

import dataaccess.GameDAO;
import dataaccess.AuthDAO;

import model.AuthData;
import model.GameData;
import requestresult.*;

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

        // get all game metadata. The DAO handles returning the appropriate amount of data.
        // then place the collection in a result record.
        return new ListGamesResult(gameDAO.listGames());
    }

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException {

        // verify authentication. see listGames
        AuthData authData = authDAO.getAuth(request.authToken());
        if (authData == null) {
            throw new NonexistentAuthException("Error: unauthorized");
        }

        // Set the game ID to 0 as a placeholder. The DAO or database will assign the proper ID that works
        // with the implemented storage system; that is a concern that cannot be well addressed here,
        // as GameService does not know anything about the storage implementation, and different storage
        // implementations may have different schema for determining identification. So, we define something
        // arbitrary and let the DAO handle it.
        GameData game = new GameData(0, null, null, request.gameName(), new ChessGame());

        int gameID = gameDAO.createGame(game);

        return new CreateGameResult(gameID);
    }

    public void joinGame(JoinGameRequest request) throws DataAccessException {

        // first, get auth token. verify authentication. from createGame.
        AuthData authData = authDAO.getAuth(request.authToken());
        if (authData == null) {
            throw new NonexistentAuthException("Error: unauthorized");
        }

        // Check if game exists.
        GameData game = gameDAO.getGame(request.gameID());
        if (game == null) {
            throw new NonexistentGameException("Error: already taken");
        }

        GameData updatedGame;

        // construct updated game state
        if (request.playerColor() == ChessGame.TeamColor.WHITE) {
            // if the white team is already taken
            if (game.whiteUsername() != null) {
                throw new GameTakenException("Error: already taken");
            } else {
                updatedGame = new GameData(
                        game.gameID(),          // game ID stays the same.
                        authData.username(),    // replace white team username with user associated with auth data.
                        game.blackUsername(),
                        game.gameName(),
                        game.game()
                );
            }
        } else {
            // if the black team is already taken
            if (game.blackUsername() != null) {
                throw new GameTakenException("Error: already taken");
            } else {
                updatedGame = new GameData(
                        game.gameID(),
                        game.whiteUsername(),
                        authData.username(),    // replace black team username with user associated with auth data.
                        game.gameName(),
                        game.game()
                );
            }
        }

        gameDAO.updateGame(updatedGame);

    }

}
