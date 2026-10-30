package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserSQLTest {

    UserDAO userDAO = new SqlUserDAO();

    // Created by intelliJ to stop the error from throwing.
    public UserSQLTest() throws DataAccessException {
    }

    @Test
    public void addUserSuccess() {

    }

    @Test
    public void removeUsersSuccess() {

        // add several users.
        try (var conn = DatabaseManager.getConnection()) {
            clearTable("user");

            // create a prepared statement
            var statement = """
                    INSERT INTO user (username, password, email) VALUES ('joe', 1234, 'joe@email.com');
                    """;
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
            statement = """
                    INSERT INTO user (username, password, email) VALUES ('fred', 0001, 'fred@email.com')
                    """;
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }

            // clear table
            userDAO.removeUsers();

            // get everything from table
            statement = """
                    SELECT * FROM user;
                    """;
            try (var ps = conn.prepareStatement(statement)) {
                try (var data = ps.executeQuery()) {
                    boolean isEmpty = data.next();
                    // if isEmpty is false, we have reached the end of the results at the beginning, meaning
                    // we have nothing in the table.
                    assertFalse(isEmpty);
                }
            }

        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }

    }

    // clear table. Essentially a duplicate of removeUsers
    public void clearTable(String table) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            // empty table
            final String statement = "TRUNCATE TABLE user";

            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: issue removing users.");
        }
    }

}

