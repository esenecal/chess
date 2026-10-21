package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import requestresult.*;

import javax.xml.crypto.Data;

/**
 * Contains logic for user-related requests
 */
public class UserService {

    // the specific implementation of userDAO.
    private final UserDAO userDAO;

    // I got this idea from an old Discord message on this topic.
    public UserService(UserDAO userDAO) {
        // the specific implementation is passed in at runtime, at instantiation.
        this.userDAO = userDAO;
    }

    /**
     * Register a user.
     *
     * @param request a request record containing a username, password, and email.
     * @return a RegisterResult containing a username and authToken.
     */
    public RegisterResult register(RegisterRequest request) throws DataAccessException {

        // create a UserData object using request.
        UserData userData = new UserData(request.username(), request.password(), request.email());

        // attempt to get userData associated with userName.
        UserData existingUser = userDAO.getUser(userData.username());

        // If there is an existing user, then this user is already taken.
        // These are thrown here to avoid check logic in DAOs, which would interfere with proper logic here.
        // AlreadyTakenException and its siblings are still children of DataAccessException as they represent
        // That part.
        if (existingUser != null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        // otherwise, null.

        // create user.
        userDAO.createUser(userData);

        // create auth
        AuthData authData = new AuthData(Util.generateAuthToken(), userData.username());
        userDAO.createAuth(authData);

        // return RegisterResult to Handler.
        return new RegisterResult(authData.username(), authData.authToken());
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {

        // query storage for user data with username
        UserData existingUser = userDAO.getUser(request.username());

        // If UserData is null, no such user exists. Functionally checking if the username is correct.
        // If the user does exist but the passwords do not match, then also unauthorized.
        // This used to have two exceptions, but as a nonexistent username and an incorrect password are both
        // functionally unauthorized credentials, we are simplifying.
        // Removing distinction between an incorrect username vs. password also may increase security.
        // The server does not need to know WHAT failed, only that it failed.
        if (existingUser == null || !existingUser.password().equals(request.password())) {
            throw new UnauthorizedCredentialsException("Error: unauthorized");
        }

        // create auth with existing user data
        AuthData authData = new AuthData(Util.generateAuthToken(), existingUser.username());
        userDAO.createAuth(authData);

        return new LoginResult(authData.username(), authData.authToken());
    }

    // See Phase 3 directions for why I use void.
    public void logout(LogoutRequest request) throws DataAccessException {

        // Get authData associated with request authToken.
        AuthData authData = userDAO.getAuth(request.authToken());

        // if user is not authenticated, say so.
        if (authData == null) {
            throw new NonexistentAuthException("Error: unauthorized");
        }

        // Remove the authData to log out the user.
        userDAO.deleteAuth(authData);
    }
    
}
