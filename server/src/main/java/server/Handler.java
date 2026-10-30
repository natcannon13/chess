package server;

import com.google.gson.Gson;
import dataaccess.*;
import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import model.GameData;
import server.models.*;
import service.*;

import java.util.List;

public class Handler {

    private final AdminService adminService;
    private final UserService userService;
    private final GameService gameService;

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public Handler() throws DataAccessException{
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        authDAO = new SQLAuthDAO();
        adminService = new AdminService(authDAO, gameDAO, userDAO);
        userService = new UserService(authDAO, userDAO);
        gameService = new GameService(authDAO, gameDAO);
    }

    public void clear() throws DataAccessException {
        adminService.clear();
    }

    public RegisterResult registerUser(String userInfo) throws AlreadyTakenException, BadArgumentException, DataAccessException {
        var json = new Gson();
        RegisterRequest request = json.fromJson(userInfo, RegisterRequest.class);
        return userService.register(request);
    }

    public LoginResult loginUser(String loginInfo) throws BadArgumentException, UnauthorizedException, DataAccessException {
        var json = new Gson();
        LoginRequest request = json.fromJson(loginInfo, LoginRequest.class);
        return userService.login(request);
    }

    public void logoutUser(String authToken) throws UnauthorizedException, DataAccessException {
        userService.logout(authToken);
    }

    public List<GameData> listGames (String authToken) throws UnauthorizedException, DataAccessException {
        return gameService.listGames(authToken);
    }

    public int createGame(String authToken, String gameInfo) throws UnauthorizedException, BadArgumentException, DataAccessException {
        var json = new Gson();
        GameName name = json.fromJson(gameInfo, GameName.class);
        return gameService.createGame(authToken, name.gameName());
    }

    public void joinGame(String authToken, String joinInfo) throws UnauthorizedException, BadArgumentException,
            AlreadyTakenException, DataAccessException {
        var json = new Gson();
        GameJoinRequest request = json.fromJson(joinInfo, GameJoinRequest.class);
        gameService.joinGame(authToken, request);
    }

    private record GameName(String gameName){}
}
