package client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

// Facade's job:
// open server connection
// serialize request to JSON
// set correct http method
// parse response
// map http error codes to client-side exceptions.

/**
 * Interface to hide Server API from client. Contains all communication logic.
 */
public class ServerFacade {
    // See MasteryLS "Web Client". Used as template.

    // HttpClient for making requests. Opens the connection.
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    // URL string for making requests.
    private final String urlString;
    private final int timeout;

    /**
     * Initialize a facade with a default timeout value
     *
     * @param host host url
     * @param port port number
     */
    public ServerFacade(String host, int port) {
        // https://stackoverflow.com/questions/997482/does-java-support-default-parameter-values
        this.urlString = String.format(Locale.getDefault(), "http://%s:%d", host, port);
        this.timeout = 5000;
    }

    /**
     * Initialize a facade
     *
     * @param host    Host name
     * @param port    port number
     * @param timeout Length for a timeout, in milliseconds.
     */
    public ServerFacade(String host, int port, int timeout) {
        // on initialization,
        this.urlString = String.format(Locale.getDefault(), "http://%s:%d", host, port);
        this.timeout = timeout;
    }

    /**
     * Register a user
     */
    public void registerUser() {

    }

    /**
     * logging in a user.
     */
    public void loginUser() {

    }

    /**
     * Logout user
     */
    public void logoutUser() {

    }

    /**
     * Create a game
     */
    public void createGame() {

    }

    /**
     * Get list of games.
     */
    public void listGames() {

    }

    /**
     * Get a game's data
     */
    public void getGame() {

    }

    /**
     * Update a game
     */
    public void updateGame() {

    }

    /**
     * Build a request. See Pet Shop ServerFacade, which was used as a reference, and MasteryLS "Web Client".
     *
     */
//    private static HttpResponse<String> buildRequest(String path, String method) throws Exception {
//        // Build an HTTP get request.
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(new URI(urlString + path))
//                .method(method)
//
//        // Get the response and return it.
//        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//    }
//
//    /**
//     * Create the body for the request. See MasteryLS Web Client, which provided the template for this.
//     */
//    private static HttpRequest.BodyPublisher requestBodyPublisher(String body) {
//
//    }

    /**
     * Receive and interpret an HTTP response.
     */
    private static void receiveResponse() {

    }
}
