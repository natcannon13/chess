package server;

import com.google.gson.Gson;
import dataaccess.*;
import server.models.LoginRequest;
import server.models.LoginResult;
import server.models.RegisterRequest;
import server.models.RegisterResult;
import service.*;

public class Handler {

    private final AdminService adminService;
    private final UserService userService;
    //private final GameService gameService;

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public Handler(){
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        adminService = new AdminService(authDAO, gameDAO, userDAO);
        userService = new UserService(authDAO, userDAO);
    }

    public void clear(){
        adminService.clear();
    }

    public RegisterResult registerUser(String userInfo) throws AlreadyTakenException, MissingArgumentException {
        var json = new Gson();
        RegisterRequest request = json.fromJson(userInfo, RegisterRequest.class);
        return userService.register(request);
    }

    public LoginResult loginUser(String loginInfo) throws MissingArgumentException, UnauthorizedException{
        var json = new Gson();
        LoginRequest request = json.fromJson(loginInfo, LoginRequest.class);
        return userService.login(request);
    }
}
