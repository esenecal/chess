package ui;

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
}
