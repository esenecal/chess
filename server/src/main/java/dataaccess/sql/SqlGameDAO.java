package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;
import requestresult.GameMetaData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;

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
        // create connection. Copied and modified from sqluserdao.java

        // serialize the game for storage.
        String serializedGame = new Gson().toJson(gameData.game());

        try (var conn = DatabaseManager.getConnection()) {
            // game_id is auto created.
            // usernames are null
            var statement = """
                    INSERT INTO game (game_name, game) VALUES (?, ?);
                    """;
            // insert authToken, username
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, gameData.gameName());
                ps.setString(2, serializedGame);
                ps.executeUpdate();

                // get the generated game_id. See MasteryLS JDBC
                var rs = ps.getGeneratedKeys();
                int gameID = 0; // initialize at 0.
                // if this is false, we did not receive an auto increment. That should not occur.
                if (rs.next()) {
                    gameID = rs.getInt(1);
                    return gameID;
                } else {
                    // failed to create the game data. This is a SQL issue if it occurs;
                    // not a logic issue.
                    throw new DataAccessException("Error: issue adding new game.");
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException("Error: issue adding new game. " + ex.getMessage());
        }
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
