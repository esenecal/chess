package ui;

import client.ClientState;

/**
 * Functions for actually displaying various things.
 */
public class ClientView {
    public static void displayText(String text, boolean newline) {
        if (!(text == null)) {
            System.out.print(text);
        }
        if (newline) {
            System.out.println();
        }
    }

    public static void displayError(String message) {
        System.err.println(message);
    }

    public static void displayHelp(ClientState state) {
        if (state == ClientState.LOGGED_OUT) {
            displayText(
                    // bless intelliJ for teaching me that this could be a text block.
                    """
                            Commands:
                            register - register account with username, password, and email.
                            login - login with username and password, and start playing chess!
                            help - display valid commands. Call this at any point.
                            quit - stop the application.
                            
                            Written in 2026 for BYU CS 240. Go Cougs!
                            """,
                    true
            );
        } else {
            displayText("""
                            Commands:
                            create - create a game with name.
                            list - list all games that are in the server.
                            play - join a game as white or black.
                            observe - watch a game without playing.
                            help - display valid commands. Call this at any point.
                            logout - log out and return to home screen.
                            """,
                    true);
        }
    }
}
