package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import org.junit.jupiter.api.*;

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

}
