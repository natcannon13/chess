package dataaccess;

import model.AuthData;

public interface AuthDAO {

    void clear() throws DataAccessException;
    boolean isEmpty() throws DataAccessException;
    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
}
