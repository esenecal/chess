package records;

import model.GameMetaData;

import java.util.Collection;

public record GameList(Collection<GameMetaData> gameList) {
}
