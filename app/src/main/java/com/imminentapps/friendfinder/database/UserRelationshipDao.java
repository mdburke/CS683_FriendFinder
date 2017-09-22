package com.imminentapps.friendfinder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.imminentapps.friendfinder.domain.User;

import static android.icu.text.MessagePattern.ArgType.SELECT;

/**
 * Created by mburke on 9/21/17.
 */
@Dao
public interface UserRelationshipDao {
    @Query("SELECT relationship_type " +
            "FROM user_relationship " +
            "WHERE :firstUserId = user_first_id" +
            "AND :secondUserId = user_second_id")
    int getRelationship(int firstUserId, int secondUserId);
}
