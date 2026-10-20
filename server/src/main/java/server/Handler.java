package server;

import dataaccess.*;
import service.AdminService;
import service.GameService;
import service.UserService;

public class Handler {

    private final AdminService adminService;
    //private final UserService userService;
    //private final GameService gameService;

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public Handler(){
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        adminService = new AdminService(authDAO, gameDAO, userDAO);
    }

    public void clear(){
        adminService.clear();
    }
}
