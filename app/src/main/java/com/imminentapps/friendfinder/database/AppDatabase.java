package com.imminentapps.friendfinder.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.imminentapps.friendfinder.domain.Hobby;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.RelationshipType;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.domain.UserRelationship;

/**
 * "Room" database class that holds the configuration of the db.
 *
 * Created by mburke on 9/21/17.
 */
@Database(entities = {User.class, UserRelationship.class, RelationshipType.class,
        Profile.class, Hobby.class}, version = 11)
public abstract class AppDatabase extends RoomDatabase {
    // Singleton instance
    private static AppDatabase INSTANCE;

    // Declare DAO objects
    public abstract UserDao userDao();
    public abstract ProfileDao profileDao();
    public abstract RelationshipTypeDao relationshipTypeDao();
    public abstract UserRelationshipDao userRelationshipDao();
    public abstract HobbyDao hobbyDao();

    // Singleton accessor
    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    "user-database")
                    .allowMainThreadQueries() // TODO: Remove this and use worker threads
                    .build();

            // Hack the relationship types in
            // TODO: Figure out the proper way to do this.
            RelationshipType[] relationshipTypes = new RelationshipType[3];
            relationshipTypes[0] = new RelationshipType(1, "pending_first_second");
            relationshipTypes[1] = new RelationshipType(2, "pending_second_first");
            relationshipTypes[2] = new RelationshipType(3, "friends");

            INSTANCE.relationshipTypeDao().deleteAll();
            INSTANCE.relationshipTypeDao().addTypes(relationshipTypes);

        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE.close();
        INSTANCE = null;
    }



}
