package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SqlAuthDAO implements AuthDAO {

    public SqlAuthDAO() throws DataAccessException {
        configureTable();
    }

    // auto-implemented with intellij
    @Override
    public void createAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
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
