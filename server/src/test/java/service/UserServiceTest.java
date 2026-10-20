package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import server.models.RegisterRequest;
import server.models.RegisterResult;

import java.util.Objects;

public class UserServiceTest {

    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;
    private static AdminService adminService;
    private static UserService userService;
    private static final RegisterRequest registerRequest = new RegisterRequest("ncannon", "password", "n@example.com");

    @BeforeAll
    public static void init(){
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        userDAO = new MemoryUserDAO();
        adminService = new AdminService(authDAO, gameDAO, userDAO);
        userService = new UserService(authDAO, userDAO);
    }

    @BeforeEach
    public void clearData(){
        adminService.clear();
    }

    @Test
    @DisplayName("Registration succeeds")
    public void registerSuccess() throws AlreadyTakenException, MissingArgumentException {
        RegisterResult result = userService.register(registerRequest);
        Assertions.assertEquals(registerRequest.username(), result.username());
        Assertions.assertEquals(36, result.authToken().length());
    }

    @Test
    @DisplayName("Registration fails")
    public void registerFailure() throws AlreadyTakenException, MissingArgumentException {
        RegisterResult result = userService.register(registerRequest);
        Assertions.assertThrows(MissingArgumentException.class, () -> {
            userService.register(new RegisterRequest("1", null, "@"));
        });
        Assertions.assertThrows(MissingArgumentException.class, () -> {
            userService.register(new RegisterRequest("1", "", "@"));
        });
        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            userService.register(registerRequest);
        });
    }

}
