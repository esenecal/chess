package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.Collection;

public class MemoryGameDAO implements GameDAO {

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void createGame(AuthData authData, GameData gameData) throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException {
        
    }
}
