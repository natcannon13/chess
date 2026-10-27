package dataaccess;

import model.UserData;

public interface UserDAO {

    void clear() throws DataAccessException;
    boolean isEmpty() throws DataAccessException;
    UserData createUser(UserData userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;

}
