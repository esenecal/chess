package model;

/**
 * Contains data used for authenticating a user
 *
 * @param authToken a unique identifier indicating a user has been authenticated
 * @param username  the user the authToken is associated with
 */
public record AuthData(String authToken, String username) {
}
