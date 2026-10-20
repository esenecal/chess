package server;

import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.*;
import requestresult.*;
import service.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clearDB)
                .post("/user", this::registerUser)
                .post("/session", this::loginUser)
                .delete("/session", this::logoutUser)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame);

        // Register your endpoints and exception handlers here.

    }

    // https://masteryls.com/course/7b46ce1a-2b79-4455-b706-aea79f1b51ac/topic/def865eb-0fc9-4383-a2a2-05bb46528ab1
    // used as reference in implementing endpoints, method reference for lambda, etc.

    // HANDLERS **********************************************************************

    /**
     * Clear database; remove all users, games, authTokens
     *
     * @param context context data for HTTP request
     */
    private void clearDB(Context context) {

    }

    /**
     * Register new user. Handler.
     *
     * @param context context data for HTTP request
     */
    private void registerUser(Context context) {
        // We get a username and password from context.

        // Deserialize JSON into registerRequest object. UserData.class denotes how to repopulate.
        // masteryls.com/course/7b46ce1a-2b79-4455-b706-aea79f1b51ac/topic/38ed2906-754f-4b0e-a59a-274a422bffcf, and the WEB
        // API one.
        Gson serializer = new Gson();

        RegisterRequest request = serializer.fromJson(context.body(), RegisterRequest.class);

        // make a register service call.
        RegisterResult result = new UserService().register(request);

        // Serialize result to JSON and send it back
        String jsonData = serializer.toJson(result);
        context.json(jsonData);
    }

    /**
     * Log in existing user by creating a new authToken
     *
     * @param context context data for HTTP request
     */
    private void loginUser(Context context) {

    }

    /**
     * Logout user that is logged in
     *
     * @param context context data for HTTP request
     */
    private void logoutUser(Context context) {

    }

    /**
     * Get list of all games
     *
     * @param context data for HTTP request
     */
    private void listGames(Context context) {

    }

    /**
     * Create a new game
     *
     * @param context data for HTTP request
     */
    private void createGame(Context context) {

    }

    /**
     * Verify that a game exists, adds caller as requested color to game
     *
     * @param context data for HTTP request
     */
    private void joinGame(Context context) {

    }
    // HANDLERS END *****************************************************************

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
