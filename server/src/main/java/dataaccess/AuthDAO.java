package dataaccess;

import model.AuthData;

public interface AuthDAO {
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

    /**
     * Remove all authTokens in storage
     *
     * @throws DataAccessException throws DataAccessException
     */
    void removeAllAuthData() throws DataAccessException;
}
