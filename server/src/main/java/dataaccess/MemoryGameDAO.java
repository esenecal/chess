package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class MemoryGameDAO implements GameDAO {

    // Map for game data. Mapped gameID: GameData
    private final HashMap<Integer, GameData> gameMap = new HashMap<>();

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return gameMap.values();
    }

    @Override
    public int createGame(GameData gameData) throws DataAccessException {
        gameMap.put(gameData.gameID(), gameData);
        return gameData.gameID();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return gameMap.get(gameID);
    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException {

    }

    @Override
    public void removeGames() throws DataAccessException {
        gameMap.clear();
    }
}
