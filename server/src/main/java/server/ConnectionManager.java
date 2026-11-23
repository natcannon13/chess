package server;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    public final ConcurrentHashMap<String, Session> connections = new ConcurrentHashMap<>();

    public void add(String auth, Session session){
        connections.put(auth, session);
    }

    public void remove(String auth){
        connections.remove(auth);
    }

    public void broadcast(Session excludeSession, ServerMessage notification) throws IOException {
        String msg = notification.toString();
        for(Session c: connections.values()){
            if(c.isOpen()){
                if(!c.equals(excludeSession)){
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
