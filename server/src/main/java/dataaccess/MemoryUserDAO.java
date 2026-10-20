package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap

public class MemoryUserDAO implements UserDAO {

    // map for storing user data. stored username: UserData.
    private HashMap<String, UserData> userMap = new HashMap<>();

    @Override
    public UserData getUser(String username) {
        // return UserData associated with username.
        return userMap.get(username);
    }

    @Override
    public void createUser(UserData userData) {

    }

    @Override
    public void createAuth(AuthData authData) {

    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) {

    }
}
