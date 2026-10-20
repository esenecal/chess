package server;

import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.*;
import requestresult.*;
import service.*;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    // declaration of the userDAO. can be changed easily to switch the specific backend implementation.
    // This must be declared here; otherwise, it will not persist.
    UserDAO userDAO = new MemoryUserDAO();

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

        // ensure all the proper components of the request exist. If not, it is a bad request. Also
        // ensure they are not empty.
        if (request.username() == null || request.password() == null || request.email() == null ||
                request.username().isEmpty() || request.password().isEmpty() || request.email().isEmpty()) {
            context.status(400);
            var message = Map.of("message", "Error: bad request");
            context.json(serializer.toJson(message));
            return;
        }

        // make a register service call.
        try {
            // pass in userDAO.
            RegisterResult result = new UserService(userDAO).register(request);

            // Serialize result to JSON and send it back
            String jsonData = serializer.toJson(result);
            context.status(200);
            context.json(jsonData);

        } catch (AlreadyTakenException ex) {
            // send back the error message
            context.status(403);
            // see MasteryLS, "JSON and Serialization"
            var message = Map.of("message", ex.getMessage());
            context.json(serializer.toJson(message));
        } catch (Exception ex) {
            // General exceptions, including other DataAccessExceptions.
            context.status(500);
            var message = Map.of("message", ex.getMessage());
            context.json(serializer.toJson(message));
        }
    }

    /**
     * Log in existing user by creating a new authToken
     *
     * @param context context data for HTTP request
     */
    private void loginUser(Context context) {
        Gson serializer = new Gson();

        // Deserialize username and password into LoginRequest.
        LoginRequest request = serializer.fromJson(context.body(), LoginRequest.class);

        // ensure we have a good request.
        if (request.username() == null || request.password() == null ||
                request.username().isEmpty() || request.password().isEmpty()) {
            context.status(400);
            var message = Map.of("message", "Error: bad request");
            context.json(serializer.toJson(message));
            return;
        }

        // Make the request to UserService
        try {
            LoginResult result = new UserService(userDAO).login(request);
            // serialize the result and send it to the front end.
            context.status(200);
            context.json(serializer.toJson(result));
        } catch (UnauthorizedCredentialsException ex) {
            context.status(401);
            var message = Map.of("message", ex.getMessage());
            context.json(serializer.toJson(message));
        } catch (Exception ex) {
            context.status(500);
            var message = Map.of("message", ex.getMessage());
            context.json(serializer.toJson(message));
        }
    }

    /**
     * Logout user that is logged in
     *
     * @param context context data for HTTP request
     */
    private void logoutUser(Context context) {
        Gson serializer = new Gson();

        // Serialize header authToken to logout request.
        LogoutRequest request = serializer.fromJson(context.header("authToken"), LogoutRequest.class);

        try {
            // make logout request.
            new UserService(userDAO).logout(request);
            context.status(200);
            // do not return anything.
        } catch (NonexistentAuthException ex) {
            // authToken did not exist, nothing removed.
            context.status(401);
            var message = Map.of("message", ex.getMessage());
            context.json(serializer.toJson(message));

        } catch (Exception ex) {
            // other exceptions.
            context.status(500);
            var message = Map.of("message", ex.getMessage());
            context.json(serializer.toJson(message));
        }

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
