package client;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;
import model.GameMetaData;
import ui.ClientView;

import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Locale;
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
            displayText(state + " > ", false);
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
                quitCommand();
                break;
            case "help":
                displayHelp(LOGGED_OUT);
                break;
            case "register":
                try {
                    registerCommand(scan);
                    break;
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                    break;
                }
            case "login":
                try {
                    loginCommand(scan);
                    break;
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                    break;
                }
            default:
                break;
        }

    }

    private void quitCommand() {
        displayText("Thanks for playing!", true);
        // log out user.
        state = QUIT;
    }

    private void registerCommand(Scanner scan) throws Exception {
        displayText("Enter Username: ", false);
        String username = scanInput(scan);
        if (!username.matches(usernameRegex)) {
            displayError("""
                    Error: Usernames cannot have whitespace and must consist of letters, numbers, or - or _.
                    Exiting to menu.
                    """);
            return;
        }
        displayText("Enter Password: ", false);
        String password = scanInput(scan);
        if (!password.matches(passwordRegex)) {
            displayError("""
                    Error: passwords cannot have whitespace.
                    Exiting to menu.
                    """);
            return;
        }
        displayText("Enter Email: ", false);
        String email = scanInput(scan);
        if (email == null || email.isEmpty()) {
            displayError("""
                    Error: Please enter valid text. Email cannot be left blank.
                    Exiting to menu.
                    """);
            return;
        }
        clientLogic.register(username, password, email);
        displayText("Registered and logged in!", true);
        // due to the error being thrown, this will not change if the register fails.
        state = LOGGED_IN;
    }

    private void loginCommand(Scanner scan) throws Exception {
        displayText("Enter Username: ", false);
        String loginUsername = scanInput(scan);
        if (!loginUsername.matches(usernameRegex)) {
            displayError("""
                    Error: Usernames cannot have whitespace and must consist of letters, numbers, or - or _.
                    Exiting to menu.
                    """);
            return;
        }
        displayText("Enter Password: ", false);
        String loginPassword = scanInput(scan);
        if (!loginPassword.matches(passwordRegex)) {
            displayError("""
                    Error: passwords cannot have whitespace.
                    Exiting to menu.
                    """);
            return;
        }
        clientLogic.login(loginUsername, loginPassword);
        state = LOGGED_IN;
        displayText("Logged in.", true);
    }

    private void loggedInEval(String input, Scanner scan) {
        switch (input.toLowerCase()) {
            case "help":
                displayHelp(LOGGED_IN);
                break;
            case "logout":
                try {
                    logoutCommand();
                    break;
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                    break;
                }
            case "create":
                try {
                    createCommand(scan);
                    break;
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                    break;
                }
            case "join":
                try {
                    joinCommand(scan);
                    break;
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                    break;
                }
            case "list":
                try {
                    listCommand();
                    break;
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                    break;
                }
            case "observe":
                try {
                    observeCommand(scan);
                    break;
                } catch (Exception ex) {
                    displayError(ex.getMessage());
                    break;
                }
            default:
                break;
        }

    }

    private void logoutCommand() throws Exception {
        clientLogic.logout();
        displayText("Logged out.", true);
        state = LOGGED_OUT;
    }

    private void createCommand(Scanner scan) throws Exception {
        displayText("Game Name: ", false);
        String gameName = scanInput(scan);
        int gameID = clientLogic.createGame(gameName);
        displayText("Game created with ID of " + gameID + ".", true);
    }

    private void observeCommand(Scanner scan) throws Exception {
        // will be fully implemented in phase 6.
        displayText("Enter Game ID: ", true);
        int gameID = scanIntInput(scan);
        GameData gameData = getGame(gameID);
        String observeUsername = clientLogic.observeGame(gameID);
        ClientView.renderBoard(gameData, observeUsername);
    }

    private void joinCommand(Scanner scan) throws Exception {
        // get ID and team. Quit if keywords are stated.
        displayText("Game ID (enter 0 to quit to menu): ", false);
        int gameID = scanIntInput(scan);
        if (gameID == 0) {
            displayText("Returning to menu.", true);
            return;
        }
        displayText("Team (type WHITE or BLACK, quit to got to menu): ", false);
        String team = scanInput(scan);
        if (team.equalsIgnoreCase("quit")) {
            displayText("Returning to menu.", true);
            return;
        }
        if (!team.equals("WHITE") && !team.equals("BLACK")) {
            displayError("Please enter WHITE or BLACK. Exiting to menu.");
            return;
        }
        String joinUsername = clientLogic.joinGame(gameID, team);
        displayText("Joined game " + gameID + " as the " + team + " team.", true);
        // render board. For now, this just renders a blank board.
        GameData game = getGame(gameID);
        renderBoard(game, joinUsername);
    }

    private void listCommand() throws Exception {
        Collection<GameMetaData> gameList = clientLogic.listGames();
        displayText("[gameID] - game name | <WHITE TEAM> | <BLACKTEAM>", true);
        for (GameMetaData game : gameList) {
            displayText(formatGameMetaData(game), true);
        }
    }

    /**
     * An inefficient stand in for getting a specific game.
     */
    private GameData getGame(int gameID) throws Exception {
        GameMetaData game = null;
        Collection<GameMetaData> gameMetaData = clientLogic.listGames();
        for (GameMetaData g : gameMetaData) {
            if (g.gameID() == gameID) {
                game = g;
            }
        }
        if (game == null) {
            throw new BadRequestException("Please enter an existing game ID. Exiting to menu.");
        }
        return new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(),
                game.gameName(), new ChessGame());
    }

    /**
     * Handles display from file input.
     *
     * @return String read from scanner in.
     */
    private String scanInput(Scanner scan) {
        return scan.nextLine();
    }

    private int scanIntInput(Scanner scan) throws InputMismatchException {
        try {
            int num = scan.nextInt();
            scan.nextLine();    // advance for the next line of input
            return num;
        } catch (InputMismatchException ex) {
            scan.nextLine();    // advance just in case.
            throw new InputMismatchException("Error: a number must be entered. Exiting to menu.");
        }
    }

}
