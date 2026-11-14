package client;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameMetaData;
import records.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
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
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

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
//        System.out.println("Initialized Client ServerFacade at url " + urlString + ", port " + port);
    }

    /**
     * Function for clearing data. Used only in testing.
     *
     * @throws ClearErrorException any sort of exception
     */
    public void clearData() throws ClearErrorException {
        try {
            HttpResponse<String> response = buildRequest("/db", "DELETE", null);
            // if the status code is anything other than 200, throw an error.
            if (response.statusCode() != 200) {
                throw new Exception();
            }
        } catch (Exception ex) {
            throw new ClearErrorException("Error Clearing Database");
        }
    }

    /**
     * Register a user, using the username, password, and email.
     */
    public AuthData registerUser(RegisterData registerData) throws Exception {
        // use of data objects helps enforce and ensure that we get the right data from the UI. Also
        // helps with ensuring data conforms to a specific map.
        String body = new Gson().toJson(registerData);

        HttpResponse<String> response = buildRequest("/user", "POST", body);

        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode < 300) {        // 200 success code
            // we got a response with a username and authToken. convert it to an authData and return.
            return new Gson().fromJson(response.body(), AuthData.class);
        } else if (statusCode == 400) {
            // we got a bad request! Pass it up! Throw an exception. toString suggested by intelliJ context.
            throw new BadRequestException("The data submitted was nonexistent or otherwise invalid.");
        } else if (statusCode == 403) {
            throw new UsernameTakenException("This username is already taken. Please choose a different username.");
        } else {
            throw new Exception("There was an issue registering. Please try again or contact the dev team.");
        }

    }

    /**
     * logging in a user.
     */
    public AuthData loginUser(LoginData loginData) throws Exception {
        // serialize for body data.
        String body = new Gson().toJson(loginData);
        HttpResponse<String> response = buildRequest("/session", "POST", body);
        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode < 300) {        // 200 success code
            // we got a response with a username and authToken. convert it to an authData and return.
            return new Gson().fromJson(response.body(), AuthData.class);
        } else if (statusCode == 400) {
            throw new BadRequestException("The data submitted was nonexistent or otherwise invalid.");
        } else if (statusCode == 401) {
            throw new InvalidLoginException("Login credentials were invalid. Please try again.");
        } else {
            throw new Exception("There was an issue logging in. Please try again or contact the dev team.");
        }
    }

    /**
     * Logout user
     */
    public void logoutUser(String authToken) throws Exception {
        HttpResponse<String> response = buildRequest("/session", "DELETE", null, authToken);
        int statusCode = response.statusCode();
        if (statusCode == 401) {
            throw new InvalidAuthException("You are not logged in or otherwise have an invalid authentication");
        } else if (!(statusCode >= 200 && statusCode < 300)) {  // if it was otherwise not a success
            throw new Exception("There was an issue logging out. Please try again.");
        }
    }

    /**
     * Create a game
     * <p>
     * Returns a gameID int.
     */
    public int createGame(String gameName, String authToken) throws Exception {
        String body = new Gson().toJson(Map.of("gameName", gameName));
        HttpResponse<String> response = buildRequest("/game", "POST", body, authToken);

        int statusCode = response.statusCode();

        if (statusCode >= 200 && statusCode < 300) {        // 200 success code
            GameID gameID = new Gson().fromJson(response.body(), GameID.class);
            return gameID.gameID();
        } else if (statusCode == 400) {
            throw new BadRequestException("The data submitted was nonexistent or otherwise invalid.");
        } else if (statusCode == 401) {
            throw new InvalidAuthException("You are not logged in or otherwise have an invalid authentication");
        } else {
            throw new Exception("There was an issue creating a game. Please try again or contact the dev team.");
        }
    }

    /**
     * Get list of games.
     */
    public Collection<GameMetaData> listGames(String authToken) throws Exception {
        HttpResponse<String> response = buildRequest("/game", "GET", null, authToken);

        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode < 300) {        // 200 success code
            // get response and parse it. Use GameList as the structure.
            GameList gameList = new Gson().fromJson(response.body(), GameList.class);
            return gameList.games();
        } else if (statusCode == 401) {
            throw new InvalidAuthException("You are not logged in or otherwise have an invalid authentication");
        } else {
            throw new Exception("There was an issue getting the list of games. Please try again " +
                    "or contact the dev team.");
        }
    }

    /**
     * Join a game.
     */
    public void playGame(ChessGame.TeamColor team, Integer gameID, String authToken) throws Exception {

        String body = new Gson().toJson(new JoinGameRequest(team, gameID));
        HttpResponse<String> response = buildRequest("/game", "PUT", body, authToken);

        int statusCode = response.statusCode();
        if (statusCode >= 200 && statusCode < 300) {
            // successful add; don't do anything.
            return;
        } else if (statusCode == 400) {
            throw new BadRequestException("The data submitted was nonexistent or otherwise invalid, or the " +
                    "requested game does not exist.");
        } else if (statusCode == 401) {
            throw new InvalidAuthException("You are not logged in or otherwise have an invalid authentication");
        } else if (statusCode == 403) {
            throw new GameTakenException("There is already a player on this team.");
        } else {
            throw new Exception("There was an issue getting the list of games. Please try again " +
                    "or contact the dev team.");
        }
    }

    // implement observe game.

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
        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // overload of build request, now with a header
    private static HttpResponse<String> buildRequest(
            String path, String method, String body, String headerValue
    ) throws Exception {
        // Build an HTTP get request.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString + path))
                .method(method, requestBodyPublisher(body))
                .header("authorization", headerValue)
                .build();

        // Get the response and return it.
        return HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Create the body for the request. See MasteryLS Web Client, which basically provided this. this should take a
     * JSON string, which will have been converted before use. This was so simple, I felt that this was the most
     * graceful way to do it after seeing it.
     */
    private static HttpRequest.BodyPublisher requestBodyPublisher(String body) {
        // If there is no body, then return no body. If there is, return it as a body publisher.
        if (body == null) {
            return HttpRequest.BodyPublishers.noBody();
        } else {
            return HttpRequest.BodyPublishers.ofString(body);
        }
    }

}
