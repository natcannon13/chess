package server;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.WebSocketService;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.GameMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsCloseHandler, WsMessageHandler {

    private final ConnectionManager CONNECTIONS = new ConnectionManager();
    private Handler handler;
    private WebSocketService webSocketService;

    public WebSocketHandler(Handler handler, AuthDAO authDAO, GameDAO gameDAO){
        this.handler = handler;
        webSocketService = new WebSocketService(authDAO, gameDAO);
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
        CONNECTIONS.add(authToken, session);
        try{
            GameMessage game = webSocketService.loadGame(authToken, gameID);
            Notification notification = webSocketService.joinNotification(authToken, gameID);
            CONNECTIONS.send(session, game);
            CONNECTIONS.broadcast(session, notification);
        }
        catch (Exception e){
            ErrorMessage message = new ErrorMessage(e.getMessage());
            CONNECTIONS.send(session, message);
        }
    }

}
