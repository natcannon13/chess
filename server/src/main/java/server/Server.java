package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import service.AlreadyTakenException;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private static final Handler handler = new Handler();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", Server::registerUser);
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
            handler.registerUser(body);
        } catch (AlreadyTakenException e) {

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
