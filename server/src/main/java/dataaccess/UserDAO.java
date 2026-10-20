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
     * Add AuthData in memory. Associate authToken: AuthData
     *
     * @param authData authToken, username
     */
    void createAuth(AuthData authData) throws DataAccessException;

    /**
     * return authData associated with an authToken
     *
     * @param authToken an authToken used for authenticating a user's session
     * @return AuthData
     */
    AuthData getAuth(String authToken) throws DataAccessException;

    /**
     * Remove auth data from storage.
     *
     * @param authData username, authToken.
     */
    void deleteAuth(AuthData authData) throws DataAccessException;
}
