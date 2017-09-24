package com.imminentapps.friendfinder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.imminentapps.friendfinder.domain.User;

import java.util.List;

/**
 * Database Access Object for the User class
 * Created by mburke on 9/21/17.
 */
@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE email = :email")
    User findByEmail(String email);

    @Query("SELECT COUNT(*) from user")
    int countUsers();

    @Update
    void updateUsers(User... users);

    @Insert
    void insertUsers(User... users);

    @Delete
    void delete(User user);
}
