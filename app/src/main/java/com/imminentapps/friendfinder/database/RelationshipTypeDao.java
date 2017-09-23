package com.imminentapps.friendfinder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.imminentapps.friendfinder.domain.RelationshipType;

import java.util.List;

/**
 * Created by mburke on 9/21/17.
 */
@Dao
public interface RelationshipTypeDao {
    @Query("SELECT * FROM relationship_type")
    List<RelationshipType> getAllTypes();

    @Query("SELECT type FROM relationship_type WHERE id = :id")
    String getType(int id);

    @Insert
    void addTypes(RelationshipType... types);
}
