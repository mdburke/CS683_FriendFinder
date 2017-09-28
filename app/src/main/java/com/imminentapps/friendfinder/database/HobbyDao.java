package com.imminentapps.friendfinder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.imminentapps.friendfinder.domain.Hobby;

import java.util.List;

/**
 * Created by mburke on 9/26/17.
 */
@Dao
public interface HobbyDao {
    @Insert
    void insert(Hobby... hobbies);

    @Query("SELECT * FROM hobby WHERE profile_id = :profile_id")
    List<Hobby> getHobbyByProfileId(int profile_id);
}
