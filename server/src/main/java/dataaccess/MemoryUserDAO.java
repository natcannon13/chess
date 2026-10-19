package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    private Map<String, UserData> userDataMemory;

    public MemoryUserDAO(){
        userDataMemory = new HashMap<>();
    }

    @Override
    public void clear() {
        userDataMemory.clear();
    }

    public boolean isEmpty(){
        return userDataMemory.isEmpty();
    }

    @Override
    public UserData createUser(UserData userData) {
        return userDataMemory.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) {
        return userDataMemory.get(username);
    }
}
