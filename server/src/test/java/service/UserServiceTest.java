package service;

import dataaccess.*;
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

        try {
            // register the user.
            RegisterResult result = userService.register(request);
            assertEquals("Jim", result.username());
            assertNotNull(result.authToken());
            assertInstanceOf(String.class, result.authToken());
        } catch (AlreadyTakenException ex) {
            fail("AlreadyTakenException");
        } catch (Exception ex) {
            fail("Exception");
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
            // Register in the user.
            userService.register(request);
        } catch (AlreadyTakenException ex) {
            fail("Register failed");
        } catch (Exception ex) {
            fail("Other exception");
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
}
