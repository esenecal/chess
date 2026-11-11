package dataaccess;

import model.GameData;
import model.GameMetaData;

import java.util.Collection;

public interface GameDAO {

    /**
     * List all existing games.
     *
     * @return Collection of GameData, empty if no Game exist.
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
     * Update game. Joining a game, making a move. Effectively replaces the data in storage.
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
