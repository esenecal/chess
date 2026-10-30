package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import org.junit.jupiter.api.Test;
import service.UserService;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthSQLTest {

    AuthDAO authDAO;

    public AuthSQLTest() throws DataAccessException {
        this.authDAO = new SqlAuthDAO();
    }

    @Test
    public void createAuthSuccess() {

        String authToken = UserService.generateAuthToken();
        String username = "jim";
        AuthData authData = new AuthData(authToken, username);

        try (var conn = DatabaseManager.getConnection()) {
            clearTable();
            // create auth
            authDAO.createAuth(authData);
            String rsAuthToken = null;
            String rsUsername = null;

            // get auth Data.
            try (var ps = conn.prepareStatement("SELECT * FROM auth")) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        rsAuthToken = rs.getString("auth_token");
                        rsUsername = rs.getString("username");
                    }
                }
            }

            // verify equal
            assertEquals(authToken, rsAuthToken);
            assertEquals(username, rsUsername);
            clearTable();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void createAuthDuplicate() {
        String authToken = UserService.generateAuthToken();
        String username = "jim";
        AuthData authData = new AuthData(authToken, username);

        try (var conn = DatabaseManager.getConnection()) {
            clearTable();
            // create auth
            authDAO.createAuth(authData);

            // Creating a duplicate will throw an error due to a duplicate primary key.
            assertThrows(DataAccessException.class, () -> authDAO.createAuth(authData));

            clearTable();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void getAuthSuccess() {
        try (var conn = DatabaseManager.getConnection()) {
            clearTable();

            // add authData.
            manuallyAddAuth(conn, "1234", "joe");

            // get auth
            AuthData authData = authDAO.getAuth("1234");
            assertEquals("1234", authData.authToken());
            assertEquals("joe", authData.username());

            clearTable();
        } catch (Exception ex) {
            fail("Exception thrown.");
        }
    }

    @Test
    public void getAuthNonExistent() {
        try (var conn = DatabaseManager.getConnection()) {
            clearTable();

            // get a nonexistent Auth.
            AuthData authData = authDAO.getAuth("1234");
            assertNull(authData);

            clearTable();
        } catch (Exception ex) {
            fail("Exception thrown.");
        }
    }

    @Test
    public void deleteAuthSuccess() {
        try (var conn = DatabaseManager.getConnection()) {
            clearTable();

            // add authData.
            AuthData authData = new AuthData("1234", "joe");
            manuallyAddAuth(conn, authData.authToken(), authData.username());

            // delete auth
            authDAO.deleteAuth(authData);
            // check to ensure everything was deleted. Taken and modified from UserSQLTest
            try (var ps = conn.prepareStatement("SELECT * FROM auth")) {
                try (var rs = ps.executeQuery()) {
                    // if this is false, it is empty. If it is true, then no delete occurred.
                    assertFalse(rs.next());
                }
            }

            clearTable();
        } catch (Exception ex) {
            fail("Exception thrown.");
        }
    }

    @Test
    public void deleteNoneAuth() {
        try (var conn = DatabaseManager.getConnection()) {
            clearTable();

            // add authData.
            AuthData authData = new AuthData("1234", "joe");
            manuallyAddAuth(conn, authData.authToken(), authData.username());

            // delete auth
            authDAO.deleteAuth(new AuthData("0", "bill"));
            // check to ensure everything was deleted. Taken and modified from UserSQLTest
            try (var ps = conn.prepareStatement("SELECT * FROM auth")) {
                try (var rs = ps.executeQuery()) {
                    // There is still something there; nothing was deleted.
                    assertTrue(rs.next());
                }
            }

            clearTable();
        } catch (Exception ex) {
            fail("Exception thrown.");
        }
    }

    // no negative test, as a failure of deleteAuth would be an exceptional case, as the query
    // itself would be problematic.

    private void manuallyAddAuth(
            Connection conn, String authToken, String username
    ) throws SQLException {
        var statement = """
                INSERT INTO auth (auth_token, username) VALUES (?, ?);
                """;
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        }
    }

    // see UserSQLTest
    private void clearTable() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            // empty table
            final String statement = "TRUNCATE TABLE auth";

            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: issue removing users.");
        }
    }
}
