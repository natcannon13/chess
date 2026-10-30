package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import server.models.LoginResult;
import server.models.RegisterResult;
import service.AlreadyTakenException;
import service.BadArgumentException;
import service.UnauthorizedException;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private static final Handler HANDLER;

    static {
        try {
            HANDLER = new Handler();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", Server::registerUser);
        javalin.post("/session", Server::loginUser);
        javalin.delete("/session", Server::logoutUser);
        javalin.get("/game", Server::listGames);
        javalin.post("/game", Server::createGame);
        javalin.put("/game", Server::joinGame);
        javalin.delete("/db", Server::clearDb);
    }

    private static void registerUser(@NotNull Context ctx){
        var body = ctx.body();
        var json = new Gson();
        try{
            RegisterResult result = HANDLER.registerUser(body);
            ctx.json(json.toJson(result));
            ctx.status(200);

        } catch (AlreadyTakenException e) {
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(403);
        } catch (BadArgumentException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(400);
        } catch (DataAccessException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(500);
        }

    }

    private static void loginUser(@NotNull Context ctx){
        var body = ctx.body();
        var json = new Gson();
        try{
            LoginResult result = HANDLER.loginUser(body);
            ctx.json(json.toJson(result));
            ctx.status(200);
        } catch(BadArgumentException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(400);
        } catch (UnauthorizedException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(401);
        } catch (DataAccessException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(500);
        }
    }

    private static void logoutUser(@NotNull Context ctx){
        var header = ctx.headerMap();
        var json = new Gson();
        try {
            HANDLER.logoutUser(header.get("Authorization"));
            ctx.json(json.toJson(Map.of()));
            ctx.status(200);
        } catch(UnauthorizedException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(401);
        } catch (DataAccessException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(500);
        }
    }

    private static void listGames(@NotNull Context ctx){
        var header = ctx.headerMap();
        var json = new Gson();
        try{
            var games = HANDLER.listGames(header.get("Authorization"));
            ctx.json(json.toJson(Map.of("games", games)));
            ctx.status(200);
        } catch(UnauthorizedException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(401);
        } catch (DataAccessException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(500);
        }
    }

    private static void createGame(@NotNull Context ctx){
        var header = ctx.headerMap();
        var json = new Gson();
        var body = ctx.body();
        try{
            int id = HANDLER.createGame(header.get("Authorization"), body);
            ctx.json(json.toJson(Map.of("gameID", id)));
            ctx.status(200);
        } catch(BadArgumentException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(400);
        } catch (UnauthorizedException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(401);
        } catch (DataAccessException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(500);
        }
    }

    private static void joinGame(@NotNull Context ctx){
        var header = ctx.headerMap();
        var json = new Gson();
        var body = ctx.body();
        try{
            HANDLER.joinGame(header.get("Authorization"), body);
            ctx.json(json.toJson(Map.of()));
            ctx.status(200);
        } catch (AlreadyTakenException e) {
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(403);
        } catch (BadArgumentException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(400);
        } catch (UnauthorizedException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(401);
        } catch (DataAccessException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(500);
        }
    }

    private static void clearDb(@NotNull Context ctx) {
        var r = Map.of();
        var json = new Gson();
        try {
            HANDLER.clear();
            ctx.json(json.toJson(r));
            ctx.status(200);
        } catch (DataAccessException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(500);
        }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
