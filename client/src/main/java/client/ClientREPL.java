package client;

import ui.ClientView;

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
                displayText("Enter Password: ", false);
                String password = scanInput(scan);
                displayText("Enter Email: ", false);
                String email = scanInput(scan);

                try {
                    clientLogic.register(username, password, email);
                    displayText("Registered and logged in!", true);
                    // due to the error being thrown, this will not change if the register fails.
                    state = LOGGED_IN;
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                }
                break;
            case "login":
                displayText("Enter Username: ", false);
                String loginUsername = scanInput(scan);
                displayText("Enter Password: ", false);
                String loginPassword = scanInput(scan);

                try {
                    clientLogic.login(loginUsername, loginPassword);
                    state = LOGGED_IN;
                    displayText("Logged in.", true);
                } catch (Exception ex) {
                    displayError(ex.getMessage());
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
                } catch (Exception ex) {
                    displayError(ex.getMessage());
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
