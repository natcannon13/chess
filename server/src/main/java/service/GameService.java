package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import server.models.GameJoinRequest;

import java.util.*;

public class GameService {

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public int createGame(String authToken, String gameName) throws BadArgumentException, DataAccessException {
        if(gameName == null || gameName.isBlank()){
            throw new BadArgumentException("Error: bad request");
        }
        if(authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        Random random = new Random();
        int gameID = (random.nextInt(9000) + 1000);
        gameDAO.createGame(new GameData(gameID, null, null, gameName, null));
        return gameID;
    }

    public void joinGame(String authToken, GameJoinRequest request) throws BadArgumentException, UnauthorizedException,
            AlreadyTakenException, DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if(authData == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData gameData = gameDAO.getGame(request.gameID());
        if(gameData == null){
            throw new BadArgumentException("Error: bad request");
        }
        if(!Objects.equals(request.playerColor(), "WHITE") && !Objects.equals(request.playerColor(), "BLACK")){
            throw new BadArgumentException("Error: bad request");
        }
        if(gameData.isFull(request.playerColor())){
            throw new AlreadyTakenException("Error: already taken");
        }
        GameData newInfo;
        if(request.playerColor().equals("WHITE")){
            newInfo = new GameData(gameData.gameID(), authData.username(), gameData.blackUsername(), gameData.gameName(), gameData.game());
        }
        else{
            newInfo = new GameData(gameData.gameID(), gameData.whiteUsername(), authData.username(), gameData.gameName(), gameData.game());
        }
        gameDAO.updateGame(newInfo);
    }

    public List<GameData> listGames(String authToken) throws UnauthorizedException, DataAccessException {
        AuthData authData = authDAO.getAuth(authToken);
        if(authData == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        return gameDAO.listGames();
    }
}
