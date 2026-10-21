package dataaccess;

import model.AuthData;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {

    /**
     * List all existing games.
     *
     * @return Collection of GameData
     * @throws DataAccessException throws DataAccessException
     */
    Collection<GameData> listGames() throws DataAccessException;

    /**
     * Create a game and associate it with authData
     *
     * @param authData AuthData
     * @param gameData GameData
     * @throws DataAccessException throws DataAccessException
     */
    void createGame(AuthData authData, GameData gameData) throws DataAccessException;

    /**
     * Get a game by ID
     *
     * @param gameID int
     * @return GameData
     * @throws DataAccessException throws DataAccessException
     */
    GameData getGame(int gameID) throws DataAccessException;

    /**
     * Update game. Joining a game, making a move.
     *
     * @param gameID   int
     * @param gameData GameData
     * @throws DataAccessException throws DataAccessException
     */
    void updateGame(int gameID, GameData gameData) throws DataAccessException;
}
