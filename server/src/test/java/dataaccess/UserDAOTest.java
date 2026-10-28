package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;

public class UserDAOTest {

    private static UserDAO userDAO;

    @BeforeAll
    public static void init() throws DataAccessException{
        DatabaseManager.createDatabase();
        userDAO = new SQLUserDAO();
    }

    @BeforeEach
    public void reset() throws DataAccessException{
        userDAO.clear();
    }

    @Test
    @DisplayName("Clear")
    public void clear() throws DataAccessException{
        UserData data = new UserData("nat", "12504", "ncannon@example.com");
        userDAO.createUser(data);
        userDAO.clear();
        Assertions.assertTrue(userDAO.isEmpty());
    }

    @Test
    @DisplayName("Create User")
    public void createUser() throws DataAccessException{
        UserData data = new UserData("nat", "12504", "ncannon@example.com");
        userDAO.createUser(data);
        Assertions.assertFalse(userDAO.isEmpty());
    }


}
