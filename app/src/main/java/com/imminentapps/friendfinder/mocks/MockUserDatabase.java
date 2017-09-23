package com.imminentapps.friendfinder.mocks;

import com.imminentapps.friendfinder.domain.User;

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

    public boolean containsEmail(String email) {
        for (String user: users.keySet()) {
            if (user.equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsUsername(String username) {
        for (User user: users.values()) {
            if (user.getProfile().getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean addUser(User user) {
        // Guard Clause;
        if (containsEmail(user.getEmail())) { return false; }

        users.put(user.getEmail(), user);
        return true;
    }

    // Populates the list with fake data
    private void initializeMockDatabase() {
//        Hobby soccer =
//
//        List<Hobby> userOneHobbies = Arrays.asList("Soccer", "Golf", "Chess", "Reading");
//        List<Hobby> userTwoHobbies = Arrays.asList("Cooking", "Football");
//        List<Hobby> userThreeHobbies = Arrays.asList("Netflix", "Chess", "Reading");
//        List<Hobby> userFourHobbies = Arrays.asList("Football", "Cooking", "Netflix", "Golf");
//
//        users = new HashMap<>();
//        users.put("user1@test.com", new User(
//                "user1@test.com", "Password1", new Profile(userOneHobbies, "User1", "My name is User1 and I need friends.")));
//        users.put("user2@test.com", new User(
//                "user2@test.com", "Password2", new Profile(userTwoHobbies, "User2", "My name is User2 and I'm real cool.")));
//        users.put("user3@test.com", new User(
//                "user3@test.com", "Password3", new Profile(userThreeHobbies, "User3", "My name is User3 and here is about me.")));
//        users.put("user4@test.com", new User(
//                "user4@test.com", "Password4", new Profile(userFourHobbies, "User4", "My name is User4 and I like to hit the dance floor!")));
    }
}
