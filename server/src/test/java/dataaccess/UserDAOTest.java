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
        Assertions.assertEquals(data, userDAO.getUser("nat"));
    }

    @Test
    @DisplayName("Create Multiple Users")
    public void createMultipleUsers() throws DataAccessException{
        UserData data = new UserData("nat", "12504", "ncannon@example.com");
        UserData data2 = new UserData("nat2", "12504", "ncannon@example.com");
        userDAO.createUser(data);
        userDAO.createUser(data2);
        Assertions.assertFalse(userDAO.isEmpty());
        Assertions.assertEquals(data, userDAO.getUser("nat"));
        Assertions.assertEquals(data2, userDAO.getUser("nat2"));
    }

    @Test
    @DisplayName("Get User")
    public void getUser() throws DataAccessException{
        UserData data = new UserData("nat", "12504", "ncannon@example.com");
        UserData data2 = new UserData("nat2", "12504", "ncannon@example.com");
        userDAO.createUser(data);
        userDAO.createUser(data2);
        var userData2 = userDAO.getUser("nat2");
        Assertions.assertEquals("nat2", userData2.username());
        Assertions.assertEquals("12504", userData2.password());
        Assertions.assertEquals("ncannon@example.com", userData2.email());
    }

}
