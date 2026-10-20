package model;

/**
 * Class for data object containing user data.
 *
 * @param username user's name for the application
 * @param password user's associated password for authentication
 * @param email    user's email
 */
public record UserData(String username, String password, String email) {
}
