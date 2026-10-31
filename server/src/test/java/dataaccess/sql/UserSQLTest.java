package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class UserSQLTest {

    UserDAO userDAO = new SqlUserDAO();

    // Created by intelliJ to stop the error from throwing.
    public UserSQLTest() throws DataAccessException {
    }

    /**
     *
     * @param statement sql statement
     * @return an arraylist of schema, where 0: fields, 1: type, 2: isNull, 3: key.
     * @throws DataAccessException self explanatory.
     */
    public static ArrayList<ArrayList<String>> getSchema(String statement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            // check field, type, if something can be null, and key.
            ArrayList<String> fields = new ArrayList<>();
            ArrayList<String> types = new ArrayList<>();
            ArrayList<String> isNull = new ArrayList<>();
            ArrayList<String> primaryKey = new ArrayList<>();

            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        fields.add(rs.getString("field"));
                        types.add(rs.getString("type"));
                        isNull.add(rs.getString("null"));
                        primaryKey.add(rs.getString("key"));
                    }
                }
            }

            // I can't tell if this helped, so I'm just leaving it here
            // https://stackoverflow.com/questions/53983355/difference-between-using
            // -angle-and-square-bracket-methods-in-creation-of-java-ar
            return new ArrayList<>(Arrays.asList(fields, types, isNull, primaryKey));

        } catch (SQLException ex) {
            throw new DataAccessException("Issue with getting schema.");
        }
    }

    @Test
    public void checkSchema() {

        try {
            // DAO has been initialized. We now clear it, and just check the schema.
            clearTable();

            // get everything from table
            String statement = """
                    DESCRIBE user;
                    """;

            ArrayList<ArrayList<String>> schema = getSchema(statement);

            // https://stackoverflow.com/questions/21696784/how-to-declare-an-arraylist-with-values
            assertEquals(new ArrayList<>(Arrays.asList("username", "password", "email")), schema.get(0));
            assertEquals(new ArrayList<>(Arrays.asList("varchar(255)", "varchar(255)", "varchar(255)")), schema.get(1));
            assertEquals(new ArrayList<>(Arrays.asList("NO", "NO", "NO")), schema.get(2));
            assertEquals(new ArrayList<>(Arrays.asList("PRI", "", "")), schema.get(3));

            clearTable();
        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    public void manuallyAddUser(
            Connection conn, String username, String passwordHash, String email
    ) throws SQLException {
        var statement = """
                INSERT INTO user (username, password, email) VALUES (?, ?, ?);
                """;
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, passwordHash);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
        }
    }

    @Test
    public void getUserSuccess() {

        try (var conn = DatabaseManager.getConnection()) {
            clearTable();

            String username = "joe";
            String passwordHash = BCrypt.hashpw("1234", BCrypt.gensalt());
            String email = "joe@gmail.com";
            manuallyAddUser(conn, username, passwordHash, email);

            // get user
            UserData userData = userDAO.getUser("joe");
            // Assert that we got the right user.
            assertEquals(username, userData.username());
            assertEquals(passwordHash, userData.password());
            assertEquals(email, userData.email());

            clearTable();

        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    // many exceptions thrown by the DAO are for exceptional, rare cases; thus, they are not able to be
    // effectively tested here.

    @Test
    public void getNonexistentUser() {
        try (var conn = DatabaseManager.getConnection()) {
            clearTable();

            // create a user
            String username = "joe";
            String passwordHash = BCrypt.hashpw("1234", BCrypt.gensalt());
            String email = "joe@gmail.com";
            manuallyAddUser(conn, username, passwordHash, email);

            // get nonexistent user.
            UserData userData = userDAO.getUser("jim");
            // Assert that we got the right user.
            assertNull(userData);

            clearTable();

        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void addUserSuccess() {

        try (var conn = DatabaseManager.getConnection()) {
            clearTable();
            // clear table

            // add a user.
            String username = "jim";
            String password = "1234";
            String email = "jim@gmail.com";
            UserData user = new UserData(username, password, email);
            userDAO.createUser(user);

            // get data
            String dataUsername = "";
            String dataPassword = "";
            String dataEmail = "";

            // we can select everything, as there should only be one thing in the database.
            try (var ps = conn.prepareStatement("SELECT * FROM user")) {
                try (var data = ps.executeQuery()) {
                    // see MasteryLS "JDBC"
                    while (data.next()) {
                        dataUsername = data.getString("username");
                        dataPassword = data.getString("password");
                        dataEmail = data.getString("email");
                    }
                }
            }

            assertEquals(username, dataUsername);
            // Check that the received password hash is true.
            assertEquals(password, dataPassword);
            assertEquals(email, dataEmail);

            clearTable();

        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void addDuplicateUserFail() {

        try {
            clearTable();
            // clear table

            // add two users with the same username.
            String username = "jim";
            String password = "1234";
            String email = "jim@gmail.com";
            UserData user = new UserData(username, password, email);
            userDAO.createUser(user);

            assertThrows(DataAccessException.class, () -> userDAO.createUser(user));

            clearTable();

        } catch (Exception ex) {
            fail("Exception thrown. " + ex.getMessage());
        }
    }

    @Test
    public void removeUsersSuccess() {

        // add several users.
        try (var conn = DatabaseManager.getConnection()) {
            clearTable();

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
    private void clearTable() throws DataAccessException {
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

