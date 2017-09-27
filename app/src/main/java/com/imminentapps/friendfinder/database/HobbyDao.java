package com.imminentapps.friendfinder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import com.imminentapps.friendfinder.domain.Hobby;

/**
 * Created by mburke on 9/26/17.
 */
@Dao
public interface HobbyDao {
    @Insert
    void insert(Hobby... hobbies);
}
