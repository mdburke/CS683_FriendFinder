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
 * Created by mburke on 9/21/17.
 */
@Database(entities = {User.class, UserRelationship.class, RelationshipType.class,
        Profile.class, Hobby.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ProfileDao profileDao();
    public abstract RelationshipTypeDao relationshipTypeDao();
    public abstract UserRelationshipDao userRelationshipDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    "user-database")
                    .allowMainThreadQueries() // TODO: Remove this and use worker threads
                    .build();
//            populateRelationshipTypes();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE.close();
        INSTANCE = null;
    }

    private static void populateRelationshipTypes() {
        RelationshipType[] relationshipTypes = new RelationshipType[3];
        relationshipTypes[0] = new RelationshipType(1, "pending_first_second");
        relationshipTypes[0] = new RelationshipType(2, "pending_second_first");
        relationshipTypes[0] = new RelationshipType(3, "friends");

        INSTANCE.relationshipTypeDao().addTypes(relationshipTypes);
    }
}