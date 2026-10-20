package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{

    private Map<String, AuthData> authDataMemory;

    public MemoryAuthDAO(){
        authDataMemory = new HashMap<>();
    }

    public void clear(){
        authDataMemory.clear();
    }

    public boolean isEmpty(){
        return authDataMemory.isEmpty();
    }

    @Override
    public void createAuth(AuthData data) {
        authDataMemory.put(data.authToken(), data);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authDataMemory.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authDataMemory.remove(authToken);
    }


}
