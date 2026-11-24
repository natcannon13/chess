package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import service.exceptions.BadArgumentException;
import service.exceptions.UnauthorizedException;
import websocket.messages.GameMessage;
import websocket.messages.Notification;

import java.util.Objects;

public class WebSocketService {

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public WebSocketService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Notification leaveGame(String authToken, int gameID) throws DataAccessException, UnauthorizedException, BadArgumentException{
        AuthData authData = authDAO.getAuth(authToken);
        if(authData == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        String username = authData.username();
        GameData game = gameDAO.getGame(gameID);
        if(game == null){
            throw new BadArgumentException("Error: bad request");
        }
        GameData newInfo;
        if(Objects.equals(game.whiteUsername(), username)){
            newInfo = new GameData(gameID, null, game.blackUsername(), game.gameName(), game.game());
        }
        else if(Objects.equals(game.blackUsername(), username)){
            newInfo = new GameData(gameID, game.whiteUsername(), null, game.gameName(), game.game());
        }
        else{
            return new Notification("Observer " + username + " has left the game.");
        }
        gameDAO.updateGame(newInfo);
        return new Notification("Player " + username + "has left the game.");
    }

    public Notification joinNotification(String authToken, int gameID) throws DataAccessException, BadArgumentException {
        AuthData authData = authDAO.getAuth(authToken);
        if(authData == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        String username = authData.username();
        GameData game = gameDAO.getGame(gameID);
        if(game == null){
            throw new BadArgumentException("Error: bad request");
        }
        String color = "observer";
        if(Objects.equals(game.whiteUsername(), username)){
            color = "white";
        }
        if(Objects.equals(game.blackUsername(), username)){
            color = "black";
        }
        return new Notification("Player " + username + " joined game as " + color);
    }

    public GameMessage loadGame(String authToken, int gameID) throws DataAccessException, UnauthorizedException, BadArgumentException {
        AuthData authData = authDAO.getAuth(authToken);
        if(authData == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData game = gameDAO.getGame(gameID);
        if(game == null){
            throw new BadArgumentException("Error: bad request");
        }
        return new GameMessage(game);
    }

    public GameMessage makeMove(String authToken, int gameID, ChessMove move) throws BadArgumentException, DataAccessException, InvalidMoveException {
        AuthData authData = authDAO.getAuth(authToken);
        if(authData == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData game = gameDAO.getGame(gameID);
        if(game == null){
            throw new BadArgumentException("Error: bad request");
        }
        ChessGame chessGame = game.game();
        chessGame.makeMove(move);
        gameDAO.updateGame(new GameData(gameID, game.whiteUsername(), game.blackUsername(), game.gameName(), chessGame));
        return new GameMessage(game);
    }


}
