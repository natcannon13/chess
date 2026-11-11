package client;

import org.junit.jupiter.api.*;
import server.Server;


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

}
