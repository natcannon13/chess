package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void clear();
    boolean isEmpty();
    AuthData createAuth(AuthData authData);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
}
