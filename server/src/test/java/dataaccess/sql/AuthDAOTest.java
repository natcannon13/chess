package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
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
        Assertions.assertEquals(data, authDAO.getAuth(data.authToken()));
    }

    @Test
    @DisplayName("Clear")
    public void clear() throws DataAccessException {
        AuthData data = new AuthData("1", "nat");
        authDAO.createAuth(data);
        authDAO.clear();
        Assertions.assertTrue(authDAO.isEmpty());
    }

    @Test
    @DisplayName("Delete")
    public void delete() throws DataAccessException{
        AuthData data = new AuthData("1", "nat");
        authDAO.deleteAuth("1");
        Assertions.assertNull(authDAO.getAuth("1"));
    }

    @Test
    @DisplayName("Get nothing when doesn't exist")
    public void getNothing() throws DataAccessException{
        authDAO.getAuth("4");
        Assertions.assertNull(authDAO.getAuth("4"));
    }

}
