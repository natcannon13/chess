package service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import server.models.LoginRequest;
import server.models.LoginResult;
import server.models.RegisterRequest;
import server.models.RegisterResult;

public class UserServiceTest {

    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;
    private static AdminService adminService;
    private static UserService userService;
    private static final RegisterRequest REGISTER_REQUEST = new RegisterRequest("ncannon", "password", "n@example.com");

    @BeforeAll
    public static void init() throws DataAccessException {
        authDAO = new SQLAuthDAO();
        gameDAO = new MemoryGameDAO();
        userDAO = new SQLUserDAO();
        adminService = new AdminService(authDAO, gameDAO, userDAO);
        userService = new UserService(authDAO, userDAO);
    }

    @BeforeEach
    public void clearData() throws DataAccessException {
        adminService.clear();
    }

    @Test
    @DisplayName("Registration succeeds")
    public void registerSuccess() throws AlreadyTakenException, BadArgumentException, DataAccessException {
        RegisterResult result = userService.register(REGISTER_REQUEST);
        Assertions.assertEquals(REGISTER_REQUEST.username(), result.username());
        Assertions.assertEquals(36, result.authToken().length());
    }

    @Test
    @DisplayName("Re-registration fails")
    public void registerFailure() throws AlreadyTakenException, BadArgumentException, DataAccessException {
        RegisterResult result = userService.register(REGISTER_REQUEST);
        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            userService.register(REGISTER_REQUEST);
        });
    }

    @Test
    @DisplayName("Registration with no password fails")
    public void registerNoPassword(){
        Assertions.assertThrows(BadArgumentException.class, () -> {
            userService.register(new RegisterRequest("1", null, "@"));
        });
        Assertions.assertThrows(BadArgumentException.class, () -> {
            userService.register(new RegisterRequest("1", "", "@"));
        });
    }


    @Test
    @DisplayName("Login succeeds")
    public void loginSuccess() throws BadArgumentException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        userService.register(REGISTER_REQUEST);
        LoginResult result = userService.login(new LoginRequest("ncannon", "password"));
        Assertions.assertEquals("ncannon", result.username());
        Assertions.assertEquals(36, result.authToken().length());
    }

    @Test
    @DisplayName("Login fails where password isn't included")
    public void loginNoPassword(){
        Assertions.assertThrows(BadArgumentException.class, ()-> {
            userService.login(new LoginRequest("ncannon", null));
        });
        Assertions.assertThrows(BadArgumentException.class, ()-> {
            userService.login(new LoginRequest("ncannon", ""));
        });
    }

    @Test
    @DisplayName("Login fails where username isn't included")
    public void loginNoUsername(){
        Assertions.assertThrows(BadArgumentException.class, ()->{
            userService.login(new LoginRequest(null, "123"));
        });
        Assertions.assertThrows(BadArgumentException.class, ()->{
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
    public void loginIncorrectPassword() throws BadArgumentException, AlreadyTakenException, DataAccessException {
        userService.register(REGISTER_REQUEST);
        Assertions.assertThrows(UnauthorizedException.class, ()-> {
            LoginResult result = userService.login(new LoginRequest("ncannon", "password1"));
        });
    }

    @Test
    @DisplayName("Logout succeeds")
    public void logoutSuccess() throws UnauthorizedException, BadArgumentException, AlreadyTakenException, DataAccessException {
        RegisterResult result = userService.register(REGISTER_REQUEST);
        String authToken = result.authToken();
        userService.logout(authToken);
        Assertions.assertNull(authDAO.getAuth(authToken));
    }

    @Test
    @DisplayName("Logout fails with invalid token")
    public void logoutFails() throws BadArgumentException, AlreadyTakenException, DataAccessException {
        RegisterResult result = userService.register(REGISTER_REQUEST);
        String authToken = result.authToken().substring(0, 32) + "0000";
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            userService.logout(authToken);
        });
    }
}
