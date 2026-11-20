package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.GameMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

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
            case CONNECT -> {connect(command.getAuthToken(), command.getGameID(), ctx.session);}
            case MAKE_MOVE -> {break;}
            case LEAVE -> {break;}
            case RESIGN -> {break;}
        }
    }



    public void connect(String authToken, int gameID, Session session) throws DataAccessException, IOException {
        GameData game = handler.listGames(authToken).get(gameID);
        CONNECTIONS.send(session, new GameMessage(game));
        //NOTIFICATION to other clients
    }

}
