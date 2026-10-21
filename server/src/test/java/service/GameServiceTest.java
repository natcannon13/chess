package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import server.models.GameJoinRequest;
import server.models.RegisterRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    @DisplayName("Join Game succeeds for white player")
    public void joinGameSuccessWhite() throws BadArgumentException, AlreadyTakenException {
        int id = gameService.createGame("1", "testGame");
        gameService.joinGame("1", new GameJoinRequest("WHITE", id));
        GameData data = gameDAO.getGame(id);
        Assertions.assertEquals("aaaaa", data.whiteUsername());
    }

    @Test
    @DisplayName("Join Game succeeds for black player")
    public void joinGameSuccessBlack() throws BadArgumentException, AlreadyTakenException {
        int id = gameService.createGame("1", "testGame");
        gameService.joinGame("1", new GameJoinRequest("BLACK", id));
        GameData data = gameDAO.getGame(id);
        Assertions.assertEquals("aaaaa", data.blackUsername());
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

    @Test
    @DisplayName("List Game succeeds with one game")
    public void listGameSuccessOne() throws BadArgumentException {
        int id = gameService.createGame("1", "testGame");
        GameData data1 = gameDAO.getGame(id);
        List<GameData> games = gameService.listGames("1");
        Assertions.assertEquals(1, games.size());
        Assertions.assertTrue(games.contains(data1));
    }

    @Test
    @DisplayName("List Game succeeds with multiple games")
    public void listGameSuccessMultiple() throws BadArgumentException {
        int id = gameService.createGame("1", "testGame");
        int id2 = gameService.createGame("1", "testGame2");
        GameData data1 = gameDAO.getGame(id);
        GameData data2 = gameDAO.getGame(id2);
        List<GameData> games = gameService.listGames("1");
        Assertions.assertEquals(2, games.size());
        Assertions.assertTrue(games.contains(data1));
        Assertions.assertTrue(games.contains(data2));
    }

    @Test
    @DisplayName("List Game succeeds with no games")
    public void listGameSuccessNone(){
        List<GameData> games = gameService.listGames("1");
        Assertions.assertEquals(0, games.size());
    }

    @Test
    @DisplayName("List Game fails with bad auth")
    public void listGameBadAuth(){
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            List<GameData> games = gameService.listGames("2");
        });
    }

}