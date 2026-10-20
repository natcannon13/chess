package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import server.models.LoginResult;
import server.models.RegisterResult;
import service.AlreadyTakenException;
import service.MissingArgumentException;
import service.UnauthorizedException;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private static final Handler handler = new Handler();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", Server::registerUser);
        javalin.post("/session", Server::loginUser);
        javalin.delete("/session", Server::logoutUser);
        javalin.delete("/db", Server::clearDb);
    }

    private static void clearDb(@NotNull Context ctx) {
        var r = Map.of();
        var json = new Gson();
        handler.clear();
        ctx.json(json.toJson(r));
        ctx.status(200);
    }

    private static void registerUser(@NotNull Context ctx){
        var body = ctx.body();
        var json = new Gson();
        try{
            RegisterResult result = handler.registerUser(body);
            ctx.json(json.toJson(result));
            ctx.status(200);

        } catch (AlreadyTakenException e) {
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(403);
        } catch (MissingArgumentException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(400);
        }
    }

    private static void loginUser(@NotNull Context ctx){
        var body = ctx.body();
        var json = new Gson();
        try{
            LoginResult result = handler.loginUser(body);
            ctx.json(json.toJson(result));
            ctx.status(200);
        } catch(MissingArgumentException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(400);
        } catch (UnauthorizedException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(401);
        }
    }

    private static void logoutUser(@NotNull Context ctx){
        var header = ctx.headerMap();
        var json = new Gson();
        try {
            handler.logoutUser(header.get("Authorization"));
            ctx.json(json.toJson(Map.of()));
            ctx.status(200);
        } catch(UnauthorizedException e){
            ctx.json(json.toJson(Map.of("message", e.getMessage())));
            ctx.status(401);
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
