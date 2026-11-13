package client;

import model.GameMetaData;
import ui.ClientView;

import java.util.Collection;
import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.ClientView.*;
import static client.ClientState.*;

/**
 * CLI read-eval-print loop.
 */
public class ClientREPL {
    // provides functions for reading, evaluating, and printing.
    // calls functions in client logic that actually handle logging in, out, etc.

    private ClientState state;
    private ClientLogic clientLogic;
    private final String usernameRegex = "^(\\d|[a-zA-z]|-|_|)+$";
    private final String passwordRegex = "^(\\S)+$";

    /**
     * Called by ClientMain to run the CLI. Considering placing this elsewhere.
     */
    // See MasteryLS Phase 5
    public void run() {
        clientLogic = new ClientLogic();
        state = LOGGED_OUT;     // start logged out.
        // clear screen.
        System.out.print(ERASE_SCREEN);
        System.out.flush();
        displayText("Welcome to the CS 240 Chess Program!", true);
        displayText("Type help for commands.", true);

        Scanner scan = new Scanner(System.in);
        // See console ui MasteryLS
        // See Phase 5 REPL loop example in Phase 5 Architectural Patterns, MasteryLS
        while (!state.equals(QUIT)) {
            // READ
            System.out.print("> ");
            String input = scanInput(scan);

            // EVAL
            // depending on if we are logged out or logged in.
            if (state.equals(LOGGED_OUT)) {
                loggedOutEval(input, scan);
            } else {
                loggedInEval(input, scan);
            }
        }
        // just to be nice.
        scan.close();
    }

    private void loggedOutEval(String input, Scanner scan) {
        // EVAL INPUT
        // evaluate with lower case.
        // https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
        switch (input.toLowerCase()) {
            case "quit":
                // exit
                displayText("Thanks for playing!", true);
                state = QUIT;
                break;
            case "help":
                displayHelp(LOGGED_OUT);
                break;
            case "register":

                displayText("Enter Username: ", false);
                String username = scanInput(scan);
                if (!username.matches(usernameRegex)) {
                    displayError("""
                            Error: Usernames cannot have whitespace and must consist of letters, numbers, or - or _.
                            """);
                    break;
                }
                displayText("Enter Password: ", false);
                String password = scanInput(scan);
                if (!password.matches(passwordRegex)) {
                    displayError("""
                            Error: passwords cannot have whitespace.""");
                    break;
                }
                displayText("Enter Email: ", false);
                String email = scanInput(scan);
                if (email == null || email.isEmpty()) {
                    displayError("""
                            Error: Please enter valid text. Email cannot be left blank.
                            """);
                    break;
                }

                try {
                    clientLogic.register(username, password, email);
                    displayText("Registered and logged in!", true);
                    // due to the error being thrown, this will not change if the register fails.
                    state = LOGGED_IN;
                    break;
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                    break;
                }
            case "login":
                displayText("Enter Username: ", false);
                String loginUsername = scanInput(scan);
                if (!loginUsername.matches(usernameRegex)) {
                    displayError("""
                            Error: Usernames cannot have whitespace and must consist of letters, numbers, or - or _.
                            """);
                    break;
                }
                displayText("Enter Password: ", false);
                String loginPassword = scanInput(scan);
                if (!loginPassword.matches(passwordRegex)) {
                    displayError("""
                            Error: passwords cannot have whitespace.""");
                    break;
                }

                try {
                    clientLogic.login(loginUsername, loginPassword);
                    state = LOGGED_IN;
                    displayText("Logged in.", true);
                    break;
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                    break;
                }

            default:
                break;
        }

    }

    private void loggedInEval(String input, Scanner scan) {
        switch (input.toLowerCase()) {
            case "help":
                displayHelp(LOGGED_IN);
                break;
            case "logout":
                try {
                    clientLogic.logout();
                    displayText("Logged out.", true);
                    state = LOGGED_OUT;
                    break;
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                    break;
                }

            case "create":
                try {
                    displayText("Game Name: ", false);
                    String gameName = scanInput(scan);
                    int gameID = clientLogic.createGame(gameName);
                    displayText("Game created with ID of " + gameID + ".", true);
                    break;
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                    break;
                }
            case "join":
                displayText("Game ID: ", false);
                int gameID = scan.nextInt();
                scan.nextLine();
                displayText("Team (type WHITE or BLACK): ", false);
                String team = scanInput(scan);
                if (!team.equals("WHITE") && !team.equals("BLACK")) {
                    displayError("Please enter WHITE or BLACK.");
                    break;
                }
                try {
                    clientLogic.joinGame(gameID, team);
                    displayText("Joined game " + gameID + " as the " + team + " team.", true);
                    break;
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                    break;
                }
            case "list":
                try {
                    Collection<GameMetaData> gameList = clientLogic.listGames();
                    displayText("[gameID] - game name | <WHITE TEAM> | <BLACKTEAM>", true);
                    for (GameMetaData game : gameList) {
                        displayText(formatGameMetaData(game), true);
                    }
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                    break;
                }
            default:
                break;
        }

    }

    /**
     * Handles display from file input.
     *
     * @return String read from scanner in.
     */
    private String scanInput(Scanner scan) {
        return scan.nextLine();
    }

}
