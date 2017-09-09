package com.imminentapps.friendfinder.mocks;

import java.util.ArrayList;

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
    private ArrayList<User> users;

    // Private constructor
    private MockUserDatabase() {
        initializeMockDatabase();
    }

    // Singleton accessor
    public static MockUserDatabase getDatabase() { return database; }

    public ArrayList<User> getUsers() { return users; }

    // Populates the list with fake data
    private void initializeMockDatabase() {
        users = new ArrayList<>();
        users.add(new User("user1@test.com", "Password1"));
        users.add(new User("user2@test.com", "Password2"));
    }
}
