package server;

import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.WebSocketService;
import websocket.commands.UserGameCommand;
import websocket.commands.UserMoveCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.GameMessage;
import websocket.messages.Notification;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsCloseHandler, WsMessageHandler {

    private final ConnectionManager connections = new ConnectionManager();
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
            case MAKE_MOVE -> {
                UserMoveCommand moveCommand = json.fromJson(ctx.message(), UserMoveCommand.class);
                makeMove(moveCommand.getAuthToken(), moveCommand.getGameID(), ctx.session, moveCommand.move());
            }
            case LEAVE -> {leave(command.getAuthToken(), ctx.session);}
            case RESIGN -> {resign(command.getAuthToken(), command.getGameID(), ctx.session);}
        }
    }

    public void connect(String authToken, int gameID, Session session) throws DataAccessException, IOException {
        connections.add(authToken, gameID, session);
        try{
            GameMessage game = webSocketService.loadGame(authToken, gameID);
            Notification notification = webSocketService.joinNotification(authToken, gameID);
            connections.send(session, game);
            connections.broadcast(session, gameID, notification);
        }
        catch (Exception e){
            ErrorMessage message = new ErrorMessage(e.getMessage());
            connections.send(session, message);
        }
    }

    public void leave(String authToken, Session session) throws IOException {
        int id = connections.remove(authToken);
        try{
            Notification notification = webSocketService.leaveGame(authToken, id);
            connections.broadcast(session, id, notification);
            session.close();
        }
        catch (Exception e){
            ErrorMessage message = new ErrorMessage(e.getMessage());
            connections.add(authToken, id, session);
            connections.send(session, message);
        }
    }

    public void makeMove(String authToken, int id, Session session, ChessMove move) throws IOException {
        try{
            GameMessage message = webSocketService.makeMove(authToken, id, move);
            connections.broadcast(null, id, message);
            String notificationBase = "Move made: " + move.toString();
            String notificationString = webSocketService.getMoveNotificationString(authToken, id);
            connections.broadcast(session, id, new Notification(notificationBase));
            if(notificationString != null){
                connections.broadcast(null, id, new Notification(notificationString));
            }

        } catch (Exception e) {
            ErrorMessage message = new ErrorMessage(e.getMessage());
            connections.send(session, message);
        }
    }

    public void resign(String authToken, int id, Session session) throws IOException{
        try{
            Notification notification = webSocketService.resign(authToken, id);
            connections.broadcast(null, id, notification);
        }
        catch (Exception e){
            ErrorMessage message = new ErrorMessage(e.getMessage());
            connections.send(session, message);
        }
    }

}
