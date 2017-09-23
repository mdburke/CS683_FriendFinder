package com.imminentapps.friendfinder.utils;

import android.app.Application;

import com.imminentapps.friendfinder.database.AppDatabase;

/**
 * Created by mburke on 9/22/17.
 */

public class FriendFinderApplication extends Application {
    private static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        db = AppDatabase.getAppDatabase(getApplicationContext());
    }



    @Override
    public void onTerminate() {
        db.close();
        AppDatabase.destroyInstance();
        super.onTerminate();
    }

    public static AppDatabase getDatabaseInstance() { return db; }

}
