package com.imminentapps.friendfinder.database;

import com.imminentapps.friendfinder.utils.FriendFinderApplication;

/**
 * Method to access DB indirectly. Not currently using this for as much as possible.
 * Should use this instead of holding references to db in the activities.
 *
 * Created by mburke on 9/22/17.
 */
public class DBUtil {
    public static AppDatabase db = FriendFinderApplication.getDatabaseInstance();

    private static void populateWithTestData() {}

    public static AppDatabase getDBInstance() { return db; }
}
