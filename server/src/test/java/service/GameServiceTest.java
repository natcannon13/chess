package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.models.GameJoinRequest;
import server.models.RegisterRequest;

public class GameServiceTest {

    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;
    private static AdminService adminService;
    private static GameService gameService;
    private static final RegisterRequest REGISTER_REQUEST = new RegisterRequest("ncannon", "password", "n@example.com");

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
    public void createGameSuccess() throws BadArgumentException {
        int gameID = gameService.createGame("1", "game");
        Assertions.assertEquals(4, String.valueOf(gameID).length());
    }

    @Test
    @DisplayName("Create Game fails with no game name")
    public void createGameNoName(){
        Assertions.assertThrows(BadArgumentException.class, () -> {
            gameService.createGame("1", null);
        });
        Assertions.assertThrows(BadArgumentException.class, () -> {
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

    @Test
    @DisplayName("Join Game succeeds")
    public void joinGameSuccess() throws BadArgumentException, AlreadyTakenException {
        int id = gameService.createGame("1", "testGame");
        gameService.joinGame("1", new GameJoinRequest("WHITE", id));
        GameData data = gameDAO.getGame(id);
        Assertions.assertEquals("aaaaa", data.whiteUsername());
    }

    @Test
    @DisplayName("Join Game fails with invalid auth")
    public void joinGameBadAuth(){
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            gameService.joinGame("2", new GameJoinRequest("WHITE", 2));
        });
    }

    @Test
    @DisplayName("Join Game fails with bad color")
    public void joinGameBadColor() throws BadArgumentException {
        int id = gameService.createGame("1", "testGame");
        Assertions.assertThrows(BadArgumentException.class, () -> {
            gameService.joinGame("1", new GameJoinRequest("GREEN", id));
        });
    }

    @Test
    @DisplayName("Join Game fails when no game found")
    public void joinGameBadID() throws BadArgumentException {
        int id = gameService.createGame("1", "testGame");
        Assertions.assertThrows(BadArgumentException.class, () -> {
            gameService.joinGame("1", new GameJoinRequest("WHITE", id + 1));
        });
    }

    @Test
    @DisplayName("Join Game fails when spot is taken")
    public void joinGameAlreadyTaken() throws BadArgumentException, AlreadyTakenException {
        authDAO.createAuth(new AuthData("2", "bbbbb"));
        int id = gameService.createGame("1", "testGame");
        gameService.joinGame("1", new GameJoinRequest("WHITE", id));
        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            gameService.joinGame("2", new GameJoinRequest("WHITE", id));
        });
    }

}