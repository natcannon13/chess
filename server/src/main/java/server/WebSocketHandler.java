package server;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;

public class WebSocketHandler implements WsConnectHandler, WsCloseHandler, WsMessageHandler {

    private final ConnectionManager CONNECTIONS = new ConnectionManager();
    private Handler handler;

    public WebSocketHandler(Handler handler){
        this.handler = handler;
    }


    @Override
    public void handleClose(@NotNull WsCloseContext ctx) throws Exception {
        System.out.println("Websocket closed");
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) throws Exception {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        var json = new Gson();
        UserGameCommand command = json.fromJson(ctx.message(), UserGameCommand.class);
        switch(command.getCommandType()){
            case CONNECT -> {break;}
            case MAKE_MOVE -> {break;}
            case LEAVE -> {break;}
            case RESIGN -> {break;}
        }
    }

    public void connect(String authToken, int gameID){
        //LOAD_GAME to client
        //NOTIFICATION to other clients
    }

}
