package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class GameSQLTest {

    @Test
    public void clearGameSuccessful() {
        
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
