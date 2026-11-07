package client;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;
import java.util.Map;

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
    private static String urlString;

    /**
     * Initialize a facade
     *
     * @param host Host name
     * @param port port number
     */
    public ServerFacade(String host, int port) {
        // on initialization,
        urlString = String.format(Locale.getDefault(), "http://%s:%d", host, port);
        System.out.println("Initialized Client ServerFacade at url " + urlString + ", port " + port);
    }

    public void clearData() {

    }

    /**
     * Register a user, using the username, password, and email.
     */
    public AuthData registerUser(UserData userData) throws Exception {
        String body = new Gson().toJson(userData);

        try {
            HttpResponse<String> response = buildRequest("/user", "POST", body);

            int statusCode = response.statusCode();
            if (statusCode >= 200 && statusCode < 300) {        // 200 success code
                // we got a response with a username and authToken. convert it to an authData and return.
                return new Gson().fromJson(response.body(), AuthData.class);
            } else if (statusCode == 400) {
                // we got a bad request! Pass it up! Throw an exception. toString suggested by intelliJ context.
                var errorMessage = new Gson().fromJson(response.body(), Map.class);
                throw new BadRequestException(errorMessage.get("message").toString());
            } else if (statusCode == 403) {
                var errorMessage = new Gson().fromJson(response.body(), Map.class);
                throw new UsernameTakenException(errorMessage.get("message").toString());
            } else {
                var errorMessage = new Gson().fromJson(response.body(), Map.class);
                throw new Exception(errorMessage.get("message").toString());
            }

        } catch (Exception ex) {    // try something different later.
            throw new Exception("Unable to register user. Please try again.");
        }
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
     * Removes repetition in the calls to the server.
     *
     * @param path   the path for the endpoint for the request
     * @param method the HTTP request method to be used.
     * @param body   the body to be included in the request. Null if not applicable.
     */
    private static HttpResponse<String> buildRequest(String path, String method, String body) throws Exception {
        // Build an HTTP get request.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString + path))
                .method(method, requestBodyPublisher(body))
                .build();

        // Get the response and return it.
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // overload of build request, now with a header
    private static HttpResponse<String> buildRequest(
            String path, String method, String body, String headerName, String headerValue
    ) throws Exception {
        // Build an HTTP get request.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString + path))
                .method(method, requestBodyPublisher(body))
                .header(headerName, headerValue)
                .build();

        // Get the response and return it.
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Create the body for the request. See MasteryLS Web Client, which basically provided this. this should take a
     * JSON string, which will have been converted before use.
     */
    private static HttpRequest.BodyPublisher requestBodyPublisher(String body) {
        // If there is no body, then return no body. If there is, return it as a body publisher.
        if (body == null) {
            return HttpRequest.BodyPublishers.noBody();
        } else {
            return HttpRequest.BodyPublishers.ofString(body);
        }
    }

    /**
     * Receive and interpret an HTTP response. See MasteryLS Web Client, which provided reference.
     */
    private static String receiveResponse(HttpResponse<String> response) {
        // Get status code.
        int statusCode = response.statusCode();
        // Get response body.
        var responseBody = new Gson().fromJson(response.body(), Map.class);

        // depending on the status code, return something different.
        if (statusCode >= 200 && statusCode < 300) {            // 200 success codes
            return "";
        } else if (statusCode >= 300 && statusCode < 400) {     // 300 error codes
            return "";
        } else if (statusCode >= 400 && statusCode < 500) {     // 400 error codes
            return "";
        } else if (statusCode >= 500 && statusCode < 600) {     // 500 error codes
            return "";
        } else {
            return "";
        }
    }
}
