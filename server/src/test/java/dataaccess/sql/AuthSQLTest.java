package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import org.junit.jupiter.api.Test;
import service.UserService;

import javax.xml.crypto.Data;
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
