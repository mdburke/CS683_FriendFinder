package com.imminentapps.friendfinder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.imminentapps.friendfinder.domain.Profile;

/**
 * Database Access Object for the Profile class
 * Created by mburke on 9/22/17.
 */
@Dao
public interface ProfileDao {
    @Query("SELECT * FROM profile WHERE username = :username")
    Profile findByUsername(String username);

    @Query("SELECT * FROM profile WHERE profile_id = :id")
    Profile findById(int id);

    @Insert
    void insert(Profile... profiles);

    @Update
    void update(Profile... profiles);
}
