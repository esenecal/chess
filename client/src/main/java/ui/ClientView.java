package ui;

/**
 * Functions for actually displaying various things.
 */
public class ClientView {
    public static void displayText(String text) {
        System.out.println(text);
    }

    public static void displayError(String message) {
        System.err.println(message);
    }
}
