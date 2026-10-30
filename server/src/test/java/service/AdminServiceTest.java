package service;

import dataaccess.*;
import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

public class AdminServiceTest{

    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;
    private static AdminService adminService;

    @BeforeAll
    public static void init() throws DataAccessException {
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        userDAO = new SQLUserDAO();
        adminService = new AdminService(authDAO, gameDAO, userDAO);
    }

    @Test
    @DisplayName("Clear")
    public void clearSuccess() throws DataAccessException {
        var auth = new AuthData("a", "b");
        var game = new GameData(1, "a", "b", "c", null);
        var user = new UserData("a", "b", "c");
        authDAO.createAuth(auth);
        gameDAO.createGame(game);
        userDAO.createUser(user);
        Assertions.assertFalse(authDAO.isEmpty());
        Assertions.assertFalse(gameDAO.isEmpty());
        Assertions.assertFalse(userDAO.isEmpty());
        adminService.clear();
        Assertions.assertTrue(authDAO.isEmpty());
        Assertions.assertTrue(gameDAO.isEmpty());
        Assertions.assertTrue(userDAO.isEmpty());
    }




}