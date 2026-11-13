package ui;

/**
 * The current state the client is in.
 */
// See MasteryLS Phase 5
public enum ClientState {
    QUIT,           // end state; used to kill the program
    LOGGED_IN,      // user is authenticated and logged in
    LOGGED_OUT,     // user is no longer authenticated. Default state
}
