package requestresult;

import model.GameMetaData;

import java.util.Collection;

public record ListGamesResult(Collection<GameMetaData> gameList) {
}
