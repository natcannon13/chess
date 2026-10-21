package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.models.LoginRequest;
import server.models.LoginResult;
import server.models.RegisterRequest;
import server.models.RegisterResult;

import java.util.Objects;
import java.util.UUID;

public class GameServiceTest {

    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;
    private static AdminService adminService;
    private static GameService gameService;
    private static final RegisterRequest registerRequest = new RegisterRequest("ncannon", "password", "n@example.com");

    @BeforeAll
    public static void init() {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        userDAO = new MemoryUserDAO();
        adminService = new AdminService(authDAO, gameDAO, userDAO);
        gameService = new GameService(authDAO, gameDAO);
    }

    @BeforeEach
    public void clearData() {
        adminService.clear();
        authDAO.createAuth(new AuthData("1", "aaaaa"));
    }

    @Test
    @DisplayName("Create Game Succeeds")
    public void createGameSuccess() throws MissingArgumentException {
        int gameID = gameService.createGame("1", "game");
        Assertions.assertEquals(4, String.valueOf(gameID).length());
    }

    @Test
    @DisplayName("Create Game fails with no game name")
    public void createGameNoName(){
        Assertions.assertThrows(MissingArgumentException.class, () -> {
            gameService.createGame("1", null);
        });
        Assertions.assertThrows(MissingArgumentException.class, () -> {
            gameService.createGame("1", "");
        });
    }

    @Test
    @DisplayName("Create Game fails with invalid auth")
    public void createGameBadAuth(){
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            gameService.createGame("2", "game");
        });
    }

}