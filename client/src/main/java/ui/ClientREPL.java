package ui;

import client.ClientLogic;

import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.ClientState.*;

/**
 * CLI read-eval-print loop.
 */
public class ClientREPL {
    // provides functions for reading, evaluating, and printing.
    // calls functions in client logic that actually handle logging in, out, etc.

    private ClientState state;
    private ClientLogic clientLogic;
    private String authToken;

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
        System.out.println("Welcome to the CS 240 Chess Program!");
        System.out.println("Type help for commands.");

        Scanner scan = new Scanner(System.in);
        // See console ui MasteryLS
        // See Phase 5 REPL loop example in Phase 5 Architectural Patterns, MasteryLS
        while (!state.equals(QUIT)) {
            // READ
            String input = scanInput(scan);

            // EVAL
            // depending on if we are logged out or logged in.
            if (state.equals(LOGGED_OUT)) {
                loggedOutEval(input, scan);
            } else {
                loggedInEval(input);
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
                System.out.println("Thanks for playing!");
                state = QUIT;
                break;
            case "help":
                System.out.print(
                        // bless intelliJ for teaching me that this could be a text block.
                        """
                                Commands:
                                register - register account with username, password, and email.
                                login - login with username and password, and start playing chess!
                                help - display valid commands. Call this at any point.
                                quit - stop the application.
                                
                                Written in 2026 for BYU CS 240. Go Cougs!
                                """
                );
                break;
            case "register":
                String username;
                String password;
                String email;

                System.out.print("Enter Username: ");
                username = scanInput(scan);
                System.out.println();

                System.out.print("Enter Username: ");
                password = scanInput(scan);
                System.out.println();

                System.out.print("Enter Username: ");
                email = scanInput(scan);
                System.out.println();

                try {
                    authToken = clientLogic.register(username, password, email);

                } catch (Exception ex) {
                    ClientView.displayError(ex.getMessage());
                }
            default:
                System.out.println("What was that?");
                break;

        }
    }

    private void loggedInEval(String input) {

    }

    /**
     * Handles display from file input.
     *
     * @return String read from scanner in.
     */
    private String scanInput(Scanner scan) {
        System.out.print("> ");
        return scan.nextLine();
    }

}
