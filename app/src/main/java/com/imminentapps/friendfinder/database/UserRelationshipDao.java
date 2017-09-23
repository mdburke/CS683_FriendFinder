package com.imminentapps.friendfinder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.imminentapps.friendfinder.domain.UserRelationship;

/**
 * Created by mburke on 9/21/17.
 */
@Dao
public interface UserRelationshipDao {
    @Query("SELECT relationship_type " +
            "FROM user_relationship " +
            "WHERE user_first_id = :firstUserId " +
            "AND user_second_id = :secondUserId ")
    int getRelationship(int firstUserId, int secondUserId);

    @Insert
    void insertRelationship(UserRelationship... relationships);

    @Query("DELETE FROM user_relationship " +
            "WHERE user_first_id = :firstUserId " +
            "AND user_second_id = :secondUserId ")
    void delete(int firstUserId, int secondUserId);
}
