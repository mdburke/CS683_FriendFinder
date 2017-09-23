package com.imminentapps.friendfinder.database;

import com.imminentapps.friendfinder.utils.FriendFinderApplication;

/**
 * Created by mburke on 9/22/17.
 */

public class DBUtil {
    public static AppDatabase db = FriendFinderApplication.getDatabaseInstance();

    { populateWithTestData(); }

    private static void populateWithTestData() {

    }

    public static AppDatabase getDBInstance() { return db; }
}
