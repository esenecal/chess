package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {

    // map for storing authentication data. stored authToken: AuthData
    private final HashMap<String, AuthData> authMap = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) {
        authMap.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authMap.get(authToken);
    }

    @Override
    public void deleteAuth(AuthData authData) {
        authMap.remove(authData.authToken());
    }

    @Override
    public void removeAllAuthData() {
        authMap.clear();
    }
}
