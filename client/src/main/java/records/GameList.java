package records;

import model.GameMetaData;

import java.util.Collection;

/**
 * Record class for deserializing game list.
 *
 * @param games name corresponds to the JSON key games.
 */
public record GameList(Collection<GameMetaData> games) {
}
