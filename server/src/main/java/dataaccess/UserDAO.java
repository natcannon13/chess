package dataaccess;

import model.UserData;

public interface UserDAO {

    void clear();
    boolean isEmpty();
    UserData createUser(UserData userData);
    UserData getUser(String username);

}
