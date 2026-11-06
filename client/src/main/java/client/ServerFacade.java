package client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

/**
 * Interface to hide Server API from client. Contains all communication logic.
 */
public class ServerFacade {
    // See MasteryLS "Web Client". Used as template.

    // responsible for opening connection, serializing request body, setting correct http method,
    // parsing response, mapping http error codes.

    // HttpClient for making requests. Opens the connection.
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    // URL string for making requests.
    private final String urlString;
    private final int timeout;

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
     * logging in a user.
     */
    public void login() {

    }

    /**
     * Get request.
     *
     * @throws Exception for any exceptions.
     */
    private void get(String path) throws Exception {
        // Build an HTTP get request.
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString + path))
                .timeout(java.time.Duration.ofMillis(timeout))
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            System.out.println(httpResponse.body());
        } else {
            System.out.println("Error: " + httpResponse.statusCode());
        }
    }
}
