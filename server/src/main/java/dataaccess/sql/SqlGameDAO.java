package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;
import requestresult.GameMetaData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class SqlGameDAO implements GameDAO {

    public SqlGameDAO() throws DataAccessException {
        configureDatabase();
    }

    // Methods auto added by intelliJ
    @Override
    public Collection<GameMetaData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public int createGame(GameData gameData) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public void removeGames() throws DataAccessException {

    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        // connect to db
        try (var conn = DatabaseManager.getConnection()) {

            // create the game table. white and black username can both be null.
            final String statement = """
                    CREATE TABLE IF NOT EXISTS game (
                    game_id INT NOT NULL AUTO_INCREMENT,
                    white_username VARCHAR(255),
                    black_username VARCHAR(255),
                    game_name VARCHAR(255) NOT NULL,
                    game text NOT NULL,
                    PRIMARY KEY (game_id)
                    );
                    """;

            // create prepared statement, execute statement.
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure game storage.");
        }
    }
}
