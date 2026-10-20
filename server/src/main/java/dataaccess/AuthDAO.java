package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void clear();
    boolean isEmpty();
    void createAuth(AuthData authData);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
}
