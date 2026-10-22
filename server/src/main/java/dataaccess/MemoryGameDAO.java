package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import requestresult.GameMetaData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class MemoryGameDAO implements GameDAO {

    // Map for game data. Mapped gameID: GameData
    private final HashMap<Integer, GameData> gameMap = new HashMap<>();

    // used to create the gameID. As all of our data disappears (due to being in memory),
    // We can simply start at 1 each time and increment as we create games.
    int gameIDIncrement = 1;

    @Override
    public Collection<GameMetaData> listGames() throws DataAccessException {
        Collection<GameData> gameList = gameMap.values();
        // iterate through each gameData to get the required metadata
        ArrayList<GameMetaData> metaData = new ArrayList<>();
        for (GameData game : gameList) {
            metaData.add(new GameMetaData(
                    game.gameID(),
                    game.whiteUsername(),
                    game.blackUsername(),
                    game.gameName()
            ));
        }
        return metaData;
    }

    @Override
    public int createGame(GameData gameData) throws DataAccessException {

        // create a new GameData with the correct gameID
        GameData newGameData = new GameData(gameIDIncrement, gameData.whiteUsername(),
                gameData.blackUsername(), gameData.gameName(), gameData.game());

        // increment gameIDIncrement for the next game.
        gameIDIncrement++;

        gameMap.put(newGameData.gameID(), newGameData);

        return newGameData.gameID();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return gameMap.get(gameID);
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        // replace the gameData with an update.
        gameMap.put(gameData.gameID(), gameData);
    }

    @Override
    public void removeGames() throws DataAccessException {
        gameMap.clear();
    }
}
