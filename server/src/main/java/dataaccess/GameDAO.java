package dataaccess;

import model.AuthData;
import model.GameData;
import requestresult.GameMetaData;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface GameDAO {

    /**
     * List all existing games.
     *
     * @return Collection of GameData
     * @throws DataAccessException throws DataAccessException
     */
    Collection<GameMetaData> listGames() throws DataAccessException;

    /**
     * Create a game. The whiteUsername and blackUsername are initialized as null.
     *
     * @param gameData GameData
     * @throws DataAccessException throws DataAccessException
     */
    int createGame(GameData gameData) throws DataAccessException;

    /**
     * Get a game by ID
     *
     * @param gameID int
     * @return GameData, null if no game.
     * @throws DataAccessException throws DataAccessException
     */
    GameData getGame(int gameID) throws DataAccessException;

    /**
     * Update game. Joining a game, making a move.
     *
     * @param gameData GameData
     * @throws DataAccessException throws DataAccessException
     */
    void updateGame(GameData gameData) throws DataAccessException;

    /**
     * Remove all Games from storage
     */
    void removeGames() throws DataAccessException;
}
