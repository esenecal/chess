package service;

import dataaccess.AlreadyTakenException;
import model.AuthData;
import model.UserData;
import requestresult.*;
import dataaccess.UserDAO;

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
    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException {

        // create a UserData object using request.
        UserData userData = new UserData(request.username(), request.password(), request.email());

        // attempt to get userData associated with userName.
        UserData existingUser = userDAO.getUser(userData.username());

        // If there is an existing user, then this user is already taken.
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

    public LoginResult login(LoginRequest request) {
        return null;
    }
}
