package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Random;

public class GameService {

    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public int createGame(String authToken, String gameName) throws MissingArgumentException {
        if(gameName == null || gameName.isBlank()){
            throw new MissingArgumentException("Error: bad request");
        }
        if(authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        Random random = new Random();
        int gameID = (random.nextInt(9000) + 1000);
        gameDAO.createGame(new GameData(gameID, null, null, gameName, null));
        return gameID;
    }

}
