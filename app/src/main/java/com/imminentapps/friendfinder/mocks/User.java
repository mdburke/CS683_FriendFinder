package com.imminentapps.friendfinder.mocks;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO to hold user information for mock database
 *
 * Created by mburke on 9/9/17.
 */
public class User {
    private String email;
    private String password;
    private List<User> friendsList;

    public User(String email, String password) {
        this.password = password;
        this.email = email;
        this.friendsList = new ArrayList<>();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<User> getFriendsList() {
        return friendsList;
    }

    public boolean addFriend(User user) {
        return friendsList.add(user);
    }

    public boolean isFriendsWith(User user) {
        return friendsList.contains(user);
    }

    public boolean removeFriend(User user) {
        return friendsList.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return password != null ? password.equals(user.password) : user.password == null;
    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
