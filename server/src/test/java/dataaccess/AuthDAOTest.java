package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;


public class AuthDAOTest {

    private static AuthDAO authDAO;

    @BeforeAll
    public static void init() throws DataAccessException {
        DatabaseManager.createDatabase();
        authDAO = new SQLAuthDAO();
    }

    @BeforeEach
    public void reset() throws DataAccessException{
        authDAO.clear();
    }

    @Test
    @DisplayName("Create")
    public void createAuth() throws DataAccessException {
        AuthData data = new AuthData("1", "nat");
        Assertions.assertTrue(authDAO.isEmpty());
        authDAO.createAuth(data);
        Assertions.assertFalse(authDAO.isEmpty());
    }

    @Test
    @DisplayName("Clear")
    public void clear() throws DataAccessException {
        AuthData data = new AuthData("1", "nat");
        authDAO.createAuth(data);
        Assertions.assertFalse(authDAO.isEmpty());
        authDAO.clear();
        Assertions.assertTrue(authDAO.isEmpty());
    }

}
