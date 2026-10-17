package server;

import io.javalin.*;
import io.javalin.http.Context;

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

    private void clearDB(Context context) {

    }

    private void registerUser(Context context) {
    }

    private void loginUser(Context context) {

    }

    private void logoutUser(Context context) {

    }

    private void listGames(Context context) {

    }

    private void createGame(Context context) {

    }

    private void joinGame(Context context) {
        
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
