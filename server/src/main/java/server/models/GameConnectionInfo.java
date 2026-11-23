package server.models;

import org.eclipse.jetty.websocket.api.Session;

public record GameConnectionInfo(int gameID, Session session){
}
