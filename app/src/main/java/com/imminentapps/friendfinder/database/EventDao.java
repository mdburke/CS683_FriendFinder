package com.imminentapps.friendfinder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.imminentapps.friendfinder.domain.Event;

import java.util.List;

/**
 * Created by mburke on 10/1/17.
 */
@Dao
public interface EventDao {
    @Insert
    void insert(Event... events);

    @Query("SELECT * FROM event")
    List<Event> getAll();

    @Query("SELECT * FROM event WHERE event_id = :eventId")
    Event get(int eventId);
}
