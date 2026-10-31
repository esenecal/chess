package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class GameSQLTest {

    GameDAO gameDAO;

    public GameSQLTest() throws DataAccessException {
        this.gameDAO = new SqlGameDAO();
    }

    @Test
    public void createGameSuccessful() {

        try (var conn = DatabaseManager.getConnection()) {
            clearTable();

            GameData gameData = new GameData(
                    0,
                    null,
                    "person",
                    "gameName",
                    new ChessGame());

            // add game data
            int id = gameDAO.createGame(gameData);

            // retrieve data in game table.
            try (var ps = conn.prepareStatement("SELECT * FROM game")) {
                var rs = ps.executeQuery();
                if (rs.next()) {
                    // get retrieved data.
                    int gameID = rs.getInt("game_id");
                    String whiteUsername = rs.getString("white_username");
                    String blackUsername = rs.getString("black_username");
                    String gameName = rs.getString("game_name");
                    String gameJson = rs.getString("game");

                    // deserialize game.
                    ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);

                    // Verify
                    assertEquals(id, gameID);    // retrieved gameID should be one, as it is first in the table
                    assertNull(whiteUsername);
                    assertEquals("person", blackUsername);
                    assertEquals("gameName", gameName);
                    assertEquals(new ChessGame(), game);

                } else {
                    fail("Not created.");
                }
            }

            clearTable();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }

    }
    // no negative tests; throwing an exception would be a SQL side, exceptional issue.

    @Test
    public void getGameSuccessful() {
        try (var conn = DatabaseManager.getConnection()) {
            clearTable();

            // manually create games.
            String emptyGameJson = new Gson().toJson(new ChessGame());
            int game1 = manuallyCreateGame(conn,
                    "fred", "joe", "game1", emptyGameJson);
            int game2 = manuallyCreateGame(conn,
                    null, "bill", "game2", emptyGameJson);

            // get game 1.
            GameData gameDataOne = gameDAO.getGame(game1);
            assertEquals(game1, gameDataOne.gameID());
            assertEquals("fred", gameDataOne.whiteUsername());
            assertEquals("joe", gameDataOne.blackUsername());
            assertEquals("game1", gameDataOne.gameName());
            assertEquals(new ChessGame(), gameDataOne.game());

            // get game 2. Ensure that it is actually game 2.
            GameData gameDataTwo = gameDAO.getGame(game2);
            assertEquals(game2, gameDataTwo.gameID());
            assertNull(gameDataTwo.whiteUsername());
            assertEquals("bill", gameDataTwo.blackUsername());
            assertEquals("game2", gameDataTwo.gameName());
            assertEquals(new ChessGame(), gameDataTwo.game());

            clearTable();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void clearGameSuccessful() {
        try (var conn = DatabaseManager.getConnection()) {
            clearTable();

            // manually create some games.
            String emptyGameJson = new Gson().toJson(new ChessGame());

            int gameOne = manuallyCreateGame(conn,
                    "fred", "joe", "game1", emptyGameJson);
            int gameTwo = manuallyCreateGame(conn,
                    "fred", "bill", "game2", emptyGameJson);
            int gameThree = manuallyCreateGame(conn,
                    "jim", "bill", "game3", emptyGameJson);

            gameDAO.removeGames();

            // Select everything from table. See AuthSQLTest
            try (var ps = conn.prepareStatement("SELECT * FROM game")) {
                try (var rs = ps.executeQuery()) {
                    // verify table is empty.
                    assertFalse(rs.next());
                }
            }

            clearTable();

        } catch (Exception ex) {
            fail("Exception thrown: " + ex.getMessage());
        }
    }

    /*
    Return game_id.
     */
    private int manuallyCreateGame(
            Connection conn,
            String whiteUsername,
            String blackUsername,
            String game_name,
            String gameJson
    ) throws SQLException {
        var statement = """
                INSERT INTO game (white_username, black_username, game_name, game) VALUES (?, ?, ?, ?);
                """;
        try (var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, whiteUsername);
            preparedStatement.setString(2, blackUsername);
            preparedStatement.setString(3, game_name);
            preparedStatement.setString(4, gameJson);

            preparedStatement.executeUpdate();

            // get the generated game_id. See MasteryLS JDBC
            var resultSet = preparedStatement.getGeneratedKeys();
            int gameID = 0; // initialize at 0.

            if (resultSet.next()) {
                gameID = resultSet.getInt(1);
                return gameID;
            } else {
                // If we did not create anything, throw an exception.

                throw new SQLException("Did not create user.");
            }

        }
    }

    // see UserSQLTest
    private void clearTable() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            // empty table
            final String statement = "TRUNCATE TABLE game";

            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: issue removing game data.");
        }
    }
}
