package service;

import java.util.UUID;

public class util {
    // See MasteryLS "Phase 3: Chess Web API"
    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
