package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.SQLException;

public class SqlAuthDAO implements AuthDAO {

    public SqlAuthDAO() throws DataAccessException {
        configureTable();
    }

    // auto-implemented with intellij
    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        // create connection. Copied and modified from sqluserdao.java
        try (var conn = DatabaseManager.getConnection()) {
            // create a prepared statement
            var statement = "INSERT INTO auth (auth_token, username) VALUES (?, ?)";
            // insert authToken, username
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authData.authToken());
                preparedStatement.setString(2, authData.username());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException ex) {
            throw new DataAccessException("Error: issue adding new authentication.");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        // create connection.
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT auth_token, username FROM auth WHERE auth_token=?";
            // preparedStatement. Insert authToken.
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    // there should only be one!
                    if (rs.next()) {
                        String rsAuthToken = rs.getString("auth_token");
                        String rsUsername = rs.getString("username");

                        return new AuthData(rsAuthToken, rsUsername);
                    } else {
                        // no existing auth data.
                        return null;
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error: issue getting authentication");
        }
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public void removeAllAuthData() throws DataAccessException {

    }

    // copied over and modified from SqlUserDAO.java
    private void configureTable() throws DataAccessException {
        DatabaseManager.createDatabase();   // Create database if needed. We shouldn't.
        // connect to database.
        try (var conn = DatabaseManager.getConnection()) {

            // syntax from pet shop mysqldataaccess.java
            final String createTableStatement = """
                    CREATE TABLE IF NOT EXISTS auth (
                        auth_token VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        PRIMARY KEY (auth_token)
                    );
                    """;

            // create a prepared statement using the statement. Execute it.
            try (var preparedStatement = conn.prepareStatement(createTableStatement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {  // throw a DataAccessException instead of the SQL exception.
            throw new DataAccessException("Unable to configure database.");
        }
    }
}
