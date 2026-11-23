package server;

import org.eclipse.jetty.websocket.api.Session;
import server.models.GameConnectionInfo;
import websocket.messages.ServerMessage;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<String, GameConnectionInfo> connections = new ConcurrentHashMap<>();

    public void add(String auth, int id, Session session){
        connections.put(auth, new GameConnectionInfo(id, session));
    }

    public int remove(String auth){
        int id = connections.get(auth).gameID();
        connections.remove(auth);
        return id;
    }

    public void broadcast(Session excludeSession, int id, ServerMessage notification) throws IOException {
        String msg = notification.toString();
        for(GameConnectionInfo g: connections.values()){
            Session c = g.session();
            if(c.isOpen()){
                if(!c.equals(excludeSession) && g.gameID() == id){
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

    public void send(Session session, ServerMessage message) throws IOException{
        String msg = message.toString();
        if(session.isOpen()){
            session.getRemote().sendString(msg);
        }
    }

}
