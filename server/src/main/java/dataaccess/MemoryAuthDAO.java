package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{

    private Map<String, AuthData> authDataMemory;

    public MemoryAuthDAO(){
        authDataMemory = new HashMap<String, AuthData>();
    }

    public void clear(){
        authDataMemory.clear();
    }

    @Override
    public AuthData createAuth(AuthData data) {
        return authDataMemory.put(data.authToken(), data);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authDataMemory.get(authToken);
    }

    @Override
    public AuthData deleteAuth(String authToken) {
        return authDataMemory.remove(authToken);
    }


}
