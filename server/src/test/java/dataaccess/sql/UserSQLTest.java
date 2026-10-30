package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserSQLTest {

    UserDAO userDAO = new SqlUserDAO();

    // Created by intelliJ to stop the error from throwing.
    public UserSQLTest() throws DataAccessException {
    }

    @Test
    public void addUserSuccess() {
        
    }

}
