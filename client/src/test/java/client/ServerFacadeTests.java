package client;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.ArrayList;
import java.util.List;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void clearAll() throws Exception {
        facade.clear();
    }

    @Test
    @DisplayName("Register succeeds with valid parameters")
    public void registerSuccess() throws Exception {
        String name = facade.register("nat", "password", "nat@example.com");
        Assertions.assertEquals("nat", name);
    }

    @Test
    @DisplayName("Register fails with bad password")
    public void registerBadPassword(){
        Assertions.assertThrows(Exception.class, () -> {
           facade.register("nat", null, "nat@example.com");
        });
    }

    @Test
    @DisplayName("Register fails when name already exists")
    public void registerDuplicateAccount() throws Exception {
        facade.register("nat", "password", "nat@example.com");
        Assertions.assertThrows(Exception.class, () -> {
            facade.register("nat", "password", "nat@example.com");
        });
    }

    @Test
    @DisplayName("Logout")
    public void logout() throws Exception{
        facade.register("nat", "password", "nat@example.com");
        Assertions.assertDoesNotThrow( () -> {
            facade.logout();
        });
    }

    @Test
    @DisplayName("Logout fails when not logged in")
    public void logoutTwice() throws Exception{
        facade.register("nat", "password", "nat@example.com");
        facade.logout();
        Assertions.assertThrows(Exception.class, () -> {
            facade.logout();
        });
    }

    @Test
    @DisplayName("Logout")
    public void login() throws Exception{
        facade.register("nat", "password", "nat@example.com");
        facade.logout();
        String username = facade.login("nat", "password");
        Assertions.assertEquals("nat", username);
    }

    @Test
    @DisplayName("Login fails when bad password")
    public void loginBadPassword() throws Exception{
        facade.register("nat", "password", "nat@example.com");
        facade.logout();
        Assertions.assertThrows(Exception.class, () -> {
            facade.login("nat", "password2");
        });
    }

    @Test
    @DisplayName("Create game")
    public void createGame() throws Exception{
        facade.register("nat", "password", "nat@example.com");
        int id = facade.createGame("game");
        Assertions.assertEquals(1, id);
    }

    @Test
    @DisplayName("Create game fails when game name invalid")
    public void createGameFail() throws Exception{
        facade.register("nat", "password", "nat@example.com");
        Assertions.assertThrows(Exception.class, () -> {
            facade.createGame("");
        });
    }

    @Test
    @DisplayName("Game list")
    public void listGames() throws Exception{
        facade.register("nat", "password", "nat@example.com");
        facade.createGame("game");
        facade.createGame("game2");
        ArrayList<GameData> games = (ArrayList<GameData>) facade.listGames();
        Assertions.assertEquals(2, games.size());
        Assertions.assertEquals("game", games.get(0).gameName());
        Assertions.assertEquals("game2", games.get(1).gameName());
    }

    @Test
    @DisplayName("Join game")
    public void joinGame() throws Exception {
        facade.register("nat", "password", "nat@example.com");
        int id = facade.createGame("game");
        facade.join(ChessGame.TeamColor.WHITE, id);
        List<GameData> games = facade.listGames();
        Assertions.assertEquals("nat", games.get(0).whiteUsername());
    }

    @Test
    @DisplayName("Join full game doesn't work")
    public void joinFullGame() throws Exception{
        facade.register("nat", "password", "nat@example.com");
        int id = facade.createGame("game");
        facade.join(ChessGame.TeamColor.WHITE, id);
        Assertions.assertThrows(Exception.class, () -> {
            facade.join(ChessGame.TeamColor.WHITE, id);
        });
    }

}
