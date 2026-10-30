package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;


public class GameDAOTest {

    private static SQLGameDAO gameDAO;

    @BeforeAll
    public static void init() throws DataAccessException {
        DatabaseManager.createDatabase();
        gameDAO = new SQLGameDAO();
    }

    @BeforeEach
    public void reset() throws DataAccessException{
        gameDAO.clear();
    }

    @Test
    @DisplayName("Serialize")
    public void serialize(){
        ChessGame game = new ChessGame();
        String serialized = gameDAO.serialize(game);
        Assertions.assertEquals("{\"board\":{", serialized.substring(0, 10));
    }

    @Test
    @DisplayName("Deserialize")
    public void deserialize(){
        ChessGame game = new ChessGame();
        String serialized = gameDAO.serialize(game);
        ChessGame deserialized = gameDAO.deserialize(serialized);
        System.out.println(serialized);
        Assertions.assertEquals(game, deserialized);
    }

    @Test
    @DisplayName("isEmpty returns true when no games exist")
    public void isEmptyWhenEmpty() throws DataAccessException {
        Assertions.assertTrue(gameDAO.isEmpty());
    }

    @Test
    @DisplayName("isEmpty returns false when games exist")
    public void isNotEmptyWhenNotEmpty() throws DataAccessException{
        gameDAO.createGame(new GameData(1, "nat", "nat2", "game", new ChessGame()));
        Assertions.assertFalse(gameDAO.isEmpty());
    }

    @Test
    @DisplayName("Clear works")
    public void clear() throws DataAccessException{
        gameDAO.createGame(new GameData(1, "nat", "nat2", "game", new ChessGame()));
        gameDAO.clear();
        Assertions.assertTrue(gameDAO.isEmpty());
    }

    @Test
    @DisplayName("Get existing game")
    public void getExistingGame() throws DataAccessException{
        GameData gameData = new GameData(1, "nat", "nat2", "game", new ChessGame());
        gameDAO.createGame(gameData);
        GameData game = gameDAO.getGame(1);
        Assertions.assertEquals(gameData, game);
    }

    @Test
    @DisplayName("Fail to get game that doesn't exist")
    public void getInvalidGame() throws DataAccessException{
        Assertions.assertNull(gameDAO.getGame(1));
    }

}
