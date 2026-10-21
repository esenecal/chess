package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    // https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html
    // map for storing user data. stored username: UserData.
    private final HashMap<String, UserData> userMap = new HashMap<>();
    
    @Override
    public UserData getUser(String username) {
        // return UserData associated with username.
        return userMap.get(username);
    }

    @Override
    public void createUser(UserData userData) {
        userMap.put(userData.username(), userData);
    }

    @Override
    public void removeUsers() {
        userMap.clear();
    }
}
