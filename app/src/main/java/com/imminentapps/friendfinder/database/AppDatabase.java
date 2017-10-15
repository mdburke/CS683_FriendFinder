package com.imminentapps.friendfinder.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.imminentapps.friendfinder.domain.Event;
import com.imminentapps.friendfinder.domain.Hobby;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.domain.UserRelationship;

/**
 * "Room" database class that holds the configuration of the db.
 *
 * Created by mburke on 9/21/17.
 */
@Database(entities = {User.class, Event.class, UserRelationship.class, //RelationshipType.class,
        Profile.class, Hobby.class}, version = 18)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    // Singleton instance
    private static AppDatabase INSTANCE;

    // Declare DAO objects
    public abstract UserDao userDao();
    public abstract ProfileDao profileDao();
//    public abstract RelationshipTypeDao relationshipTypeDao();
    public abstract UserRelationshipDao userRelationshipDao();
    public abstract HobbyDao hobbyDao();
    public abstract EventDao eventDao();

    // Singleton accessor
    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    "user-database")
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE.close();
        INSTANCE = null;
    }
}
