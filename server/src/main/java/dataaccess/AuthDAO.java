package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void clear();
    AuthData createAuth(AuthData authData);
    AuthData getAuth(String authToken);
    AuthData deleteAuth(String authToken);
}
