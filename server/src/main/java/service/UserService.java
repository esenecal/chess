package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import requestresult.*;

import java.util.UUID;

/**
 * Contains logic for user-related requests.
 */
public class UserService {

    // the specific implementation of userDAO.
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    // I got this idea from an old Discord message on this topic.
    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        // the specific implementation is passed in at runtime, at instantiation.
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    /**
     * Register a user.
     *
     * @param request a request record containing a username, password, and email.
     * @return a RegisterResult containing a username and authToken.
     */
    public RegisterResult register(RegisterRequest request) throws DataAccessException {

        // Hash the password. This ensures that password hashing remains consistent,
        // no matter the database implementation.
        // we must do this service side, as we cannot expect the DAO to check the passwords for us
        // (that would be logic) and we cannot decrypt a hash.
        String passwordHash = BCrypt.hashpw(request.password(), BCrypt.gensalt());

        // create a UserData object using request.
        UserData userData = new UserData(request.username(), passwordHash, request.email());

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

        // create auth. We are fairly guaranteed that this will be a unique authData,
        // As we cannot create an authData without verifying there is no existing user.
        // And, when we log in, we check to ensure that the authData does not exist.
        // thus, we cannot have a nonexistent user, and existing authData. That would only happen
        // in testing.
        AuthData authData = new AuthData(generateAuthToken(), userData.username());
        authDAO.createAuth(authData);

        // return RegisterResult to Handler.
        return new RegisterResult(authData.username(), authData.authToken());
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {

        // query storage for user data with username
        UserData existingUser = userDAO.getUser(request.username());

        // If UserData is null, no such user exists. Functionally checking if the username is correct.
        // If the user does exist but the password does not match the hash, then also unauthorized.
        // This used to have two exceptions, but as a nonexistent username and an incorrect password are both
        // functionally unauthorized credentials, we are simplifying.
        // Removing distinction between an incorrect username vs. password also may increase security.
        // The server does not need to know WHAT failed, only that it failed.
        if (existingUser == null || !BCrypt.checkpw(request.password(), existingUser.password())) {
            throw new UnauthorizedCredentialsException("Error: unauthorized");
        }

        // create auth with existing user data
        AuthData authData = new AuthData(generateAuthToken(), existingUser.username());
        authDAO.createAuth(authData);

        return new LoginResult(authData.username(), authData.authToken());
    }

    // See Phase 3 directions for why I use void.
    public void logout(LogoutRequest request) throws DataAccessException {

        // Get authData associated with request authToken.
        AuthData authData = authDAO.getAuth(request.authToken());

        // if user is not authenticated, say so.
        if (authData == null) {
            throw new NonexistentAuthException("Error: unauthorized");
        }

        // Remove the authData to log out the user.
        authDAO.deleteAuth(authData);
    }

    // I originally moved this out in to its own file, and refactored with IntelliJ's refactoring tool. However,
    // That was not needed, so I moved it here. I refactored again completely by hand, I believe, since I
    // just pasted the method here. If IntelliJ did do something that time, it was small, like removing some imports.
    // See MasteryLS "Phase 3: Chess Web API"
    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

}
