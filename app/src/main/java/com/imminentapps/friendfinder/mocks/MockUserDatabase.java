package com.imminentapps.friendfinder.mocks;

import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores user information for use in application.
 * TODO: Remove this in favor of a real cloud service backend
 *
 * Created by mburke on 9/8/17.
 */
public class MockUserDatabase {
    // Singleton instance
    private static final MockUserDatabase database = new MockUserDatabase();

    // List to hold users
    private Map<String, User> users;

    // Private constructor
    private MockUserDatabase() {
        initializeMockDatabase();
    }

    // Singleton accessor
    public static MockUserDatabase getDatabase() { return database; }

    public Map<String, User> getUsers() { return users; }

    // Populates the list with fake data
    private void initializeMockDatabase() {
        users = new HashMap<>();
        users.put("user1@test.com", new User(
                "user1@test.com", "Password1", new Profile(null, "User1", "")));
        users.put("user2@test.com", new User(
                "user2@test.com", "Password2", new Profile(null, "User2", "")));
        users.put("user3@test.com", new User(
                "user3@test.com", "Password3", new Profile(null, "User3", "")));
        users.put("user4@test.com", new User(
                "user4@test.com", "Password4", new Profile(null, "User3", "")));
    }
}
