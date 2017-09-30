package com.imminentapps.friendfinder.domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import com.imminentapps.friendfinder.database.AppDatabase;
import com.imminentapps.friendfinder.utils.Constants;

import java.io.Serializable;

/**
 * POJO to hold user information
 * Created by mburke on 9/9/17.
 */
@Entity
public class User implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @Ignore
    private Profile profile;

    private String email;
    private String password;

    public User() {}

    @Ignore
    public User(String email, String password, Profile profile, int id) {
        this.password = password;
        this.email = email;
        this.profile = (profile != null) ? profile : new Profile();
        this.id = id;
    }

    @Ignore
    public User(String email, String password, int id) {
        this(email, password, null, id);
    }

    @Ignore
    public User(String email, String password, Profile profile) {
        this.email = email;
        this.password = password;
        this.profile = (profile != null) ? profile : new Profile();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /******** Access Methods ********/
    // TODO: Figure out the right place for these and implementation. Probably should not be grabbing the database here.
    public boolean isFriendsWith(int friendId, Context context) {
        if (this.id == friendId) { return false; }


        int firstId;
        int secondId;

        if (friendId < this.id) {
            firstId = friendId;
            secondId = this.id;
        } else {
            firstId = this.id;
            secondId = friendId;
        }

        return AppDatabase.getAppDatabase(context).userRelationshipDao().getRelationship(firstId, secondId)
                == Constants.RELATIONSHIP_TYPE_FRIENDS;
    }

    public void addFriend(int friendId, Context context) {
        int firstId;
        int secondId;

        if (friendId < this.id) {
            firstId = friendId;
            secondId = this.id;
        } else {
            firstId = this.id;
            secondId = friendId;
        }

        UserRelationship relationship = new UserRelationship(firstId, secondId, Constants.RELATIONSHIP_TYPE_FRIENDS);

        AppDatabase.getAppDatabase(context).userRelationshipDao().insertRelationship(relationship);
    }

    public void removeFriend(int friendId, Context context) {
        int firstId;
        int secondId;

        if (friendId < this.id) {
            firstId = friendId;
            secondId = this.id;
        } else {
            firstId = this.id;
            secondId = friendId;
        }

        AppDatabase.getAppDatabase(context).userRelationshipDao().delete(firstId, secondId);
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
