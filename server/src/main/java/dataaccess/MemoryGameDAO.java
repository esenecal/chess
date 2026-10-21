package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {

    // Map for game data. Mapped gameID: GameData
    private final HashMap<Integer, GameData> gameMap = new HashMap<>();

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

    @Override
    public void removeGames() throws DataAccessException {
        gameMap.clear();
    }
}
