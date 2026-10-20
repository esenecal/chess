package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import service.UserService.*;

import requestresult.*;

// See MasteryLS "Unit Testing"
public class UserServiceTest {
    /**
     * Positive register test. Successfully register a user.
     * Verifies the correct username, existence of a String authToken.
     */
    @Test
    public void registerCorrectResult() {
        UserDAO userDAO = new MemoryUserDAO();
        // meets 88% line coverage
        UserService userService = new UserService(userDAO);

        // Create a user
        RegisterRequest request = new RegisterRequest("Jim", "1234", "jim@jim.com");


        // register the user.
        try {
            RegisterResult result = userService.register(request);
            assertEquals("Jim", result.username());
            assertNotNull(result.authToken());
            assertInstanceOf(String.class, result.authToken());
        } catch (DataAccessException ex) {
            // if we reach here, assert that this does not throw an exception. if it does, it fails.
            assertAll(() -> userService.register(request));
        }

    }

    /**
     * Negative register test. Attempt to do multiple registers.
     */
    @Test
    public void registerAlreadyTaken() {
        // 100% line coverage

        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(userDAO);

        // Create a user
        RegisterRequest request = new RegisterRequest("Jim", "1234", "jim@jim.com");

        try {
            // Register the user.
            RegisterResult result = userService.register(request);
        } catch (DataAccessException ex) {
            // if we reach here, assert that this does not throw an exception. if it does, it fails.
            assertAll(() -> userService.register(request));
        }

        // attempt to create another user with the same username.
        RegisterRequest request2 = new RegisterRequest("Jim", "0001", "notjim@jim.com");
        assertThrows(AlreadyTakenException.class, () -> userService.register(request2));

    }

    /**
     * Positive login test.
     */
    @Test
    public void loginSuccessfulTest() {
        // 88% coverage.

        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(userDAO);

        // Create a user.
        RegisterRequest regRequest = new RegisterRequest("Jim", "1234", "jim@jim.com");

        try {
            // Register in the user.
            userService.register(regRequest);
        } catch (AlreadyTakenException ex) {
            fail("Register failed");
        } catch (Exception ex) {
            fail("Other exception");
        }

        // Log in the user.
        LoginRequest loginRequest = new LoginRequest("Jim", "1234");

        try {
            LoginResult loginResult = userService.login(loginRequest);
            // Determine we received a response with the correct username and an authToken.
            assertEquals("Jim", loginResult.username());
            assertNotNull(loginResult.authToken());
            assertInstanceOf(String.class, loginResult.authToken());
        } catch (UnauthorizedCredentialsException ex) {
            fail("Login failed");
        } catch (Exception ex) {
            fail("Other exception");
        }

    }

    @Test
    public void loginBadUsernameTest() {
        UserDAO userDAO = new MemoryUserDAO();
        UserService userService = new UserService(userDAO);

        // Create a user.
        RegisterRequest regRequest = new RegisterRequest("Jim", "1234", "jim@jim.com");

        try {
            // Register in the user.
            RegisterResult regResult = userService.register(regRequest);
        } catch (AlreadyTakenException ex) {
            fail("Register failed");
        } catch (Exception ex) {
            fail("Other exception");
        }

        // Log in the user.
        LoginRequest loginRequest = new LoginRequest("Joe", "1234");

        try {
            LoginResult loginResult = userService.login(loginRequest);
            // Determine we received a response with the correct username and an authToken.
            fail("Login succeeded but should not have.");
        } catch (UnauthorizedCredentialsException ex) {
            assertThrows(ex.getClass(), () -> userService.login(loginRequest));
        } catch (Exception ex) {
            fail("Other exception");
        }
    }

    @Test
    public void loginBadPasswordTest() {
        UserDAO userDAO = new MemoryUserDAO();

        // Create user.
        try {
            userDAO.createUser(new UserData("Jim", "1234", "jim@jim.com"));
        } catch (DataAccessException ex) {
            fail("DataAccessException thrown.");
        }

        UserService userService = new UserService(userDAO);

        // Log in the user.
        LoginRequest loginRequest = new LoginRequest("Joe", "1234");
        assertThrows(UnauthorizedCredentialsException.class, () -> userService.login(loginRequest));
    }
}
