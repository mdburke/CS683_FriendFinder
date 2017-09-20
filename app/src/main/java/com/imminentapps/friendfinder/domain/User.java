package com.imminentapps.friendfinder.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * POJO to hold user information
 *
 * Created by mburke on 9/9/17.
 */
public class User implements Serializable {
    private String email;
    private String password;
    private List<String> friendsList;
    private Profile profile;
    private String userId;

    public User(String email, String password, Profile profile, String userId) {
        this.password = password;
        this.email = email;
        this.profile = (profile != null) ? profile : new Profile();
        this.friendsList = new ArrayList<>();
        this.userId = userId;
    }

    public User(String email, String password, String userId) {
        this(email, password, null, userId);
    }

    public User(String email, String password, Profile profile) {
        this.email = email;
        this.password = password;
        this.profile = (profile != null) ? profile : new Profile();
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

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<String> getFriendsList() {
        return friendsList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean addFriend(String email) {
        return friendsList.add(email);
    }

    public boolean isFriendsWith(String email) {
        return friendsList.contains(email);
    }

    public boolean removeFriend(String email) {
        return friendsList.remove(email);
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
