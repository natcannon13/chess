package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import server.models.LoginRequest;
import server.models.LoginResult;
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
    @DisplayName("Re-registration fails")
    public void registerFailure() throws AlreadyTakenException, MissingArgumentException {
        RegisterResult result = userService.register(registerRequest);
        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            userService.register(registerRequest);
        });
    }

    @Test
    @DisplayName("Registration with no password fails")
    public void registerNoPassword(){
        Assertions.assertThrows(MissingArgumentException.class, () -> {
            userService.register(new RegisterRequest("1", null, "@"));
        });
        Assertions.assertThrows(MissingArgumentException.class, () -> {
            userService.register(new RegisterRequest("1", "", "@"));
        });
    }


    @Test
    @DisplayName("Login succeeds")
    public void loginSuccess() throws MissingArgumentException, UnauthorizedException, AlreadyTakenException {
        userService.register(registerRequest);
        LoginResult result = userService.login(new LoginRequest("ncannon", "password"));
        Assertions.assertEquals("ncannon", result.username());
        Assertions.assertEquals(36, result.authToken().length());
    }

    @Test
    @DisplayName("Login fails where password isn't included")
    public void loginNoPassword(){
        Assertions.assertThrows(MissingArgumentException.class, ()-> {
            userService.login(new LoginRequest("ncannon", null));
        });
        Assertions.assertThrows(MissingArgumentException.class, ()-> {
            userService.login(new LoginRequest("ncannon", ""));
        });
    }

    @Test
    @DisplayName("Login fails where username isn't included")
    public void loginNoUsername(){
        Assertions.assertThrows(MissingArgumentException.class, ()->{
            userService.login(new LoginRequest(null, "123"));
        });
        Assertions.assertThrows(MissingArgumentException.class, ()->{
            userService.login(new LoginRequest("", "123"));
        });
    }


    @Test
    @DisplayName("Login fails where user isn't registered")
    public void loginNotRegistered(){
        Assertions.assertThrows(UnauthorizedException.class, () -> {
        userService.login(new LoginRequest("ncannon", "12504"));});
    }

    @Test
    @DisplayName("Login fails where password is wrong")
    public void loginIncorrectPassword() throws MissingArgumentException, AlreadyTakenException {
        userService.register(registerRequest);
        Assertions.assertThrows(UnauthorizedException.class, ()-> {
            LoginResult result = userService.login(new LoginRequest("ncannon", "password1"));
        });
    }

}
