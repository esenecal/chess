package service;

import dataaccess.AlreadyTakenException;
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
        // meets 88% line coverage
        UserService userService = new UserService();

        // Create a user
        RegisterRequest request = new RegisterRequest("Jim", "1234", "jim@jim.com");

        try {
            // register the user.
            RegisterResult result = userService.register(request);
            assertEquals("Jim", result.username());
            assertNotNull(result.authToken());
            assertInstanceOf(String.class, result.authToken());
        } catch (AlreadyTakenException ex) {
            fail();
        }
    }

    /**
     * Negative register test. Attempt to do multiple registers.
     */
    @Test
    public void registerAlreadyTaken() throws AlreadyTakenException {
        // 100% line coverage
        UserService userService = new UserService();

        // Create a user
        RegisterRequest request = new RegisterRequest("Jim", "1234", "jim@jim.com");
        RegisterResult result = userService.register(request);

        // attempt to create another user with the same username.
        RegisterRequest request2 = new RegisterRequest("Jim", "0001", "notjim@jim.com");
        assertThrows(AlreadyTakenException.class, () -> userService.register(request));

    }
}
