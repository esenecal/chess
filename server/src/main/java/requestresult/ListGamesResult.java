package requestresult;

import model.GameData;

import java.util.Collection;

public record ListGamesResult(Collection<GameMetaData> gameList) {
}
