package dataaccess;

import model.UserData;
import model.AuthData;

/**
 * Interface for classes that handle data access for user related requests
 */
public interface UserDAO {
    /**
     * Get UserData associated with username
     *
     * @return UserData
     */
    UserData getUser(String username) throws DataAccessException;

    /**
     * Create a user in memory using userData. Associate username: UserData
     *
     * @param userData username, password, email
     */
    void createUser(UserData userData) throws DataAccessException;

    /**
     * Remove all users in storage
     *
     * @throws DataAccessException throws DataAccessException
     */
    void removeUsers() throws DataAccessException;
}
