package com.imminentapps.friendfinder.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.imminentapps.friendfinder.domain.User;

/**
 * Created by mburke on 9/19/17.
 */

public class DBUtil extends SQLiteOpenHelper {
    public static final String USER_TABLE_NAME = "users";
    public static final String USER_ID_COLUMN = "userId";
    public static final String PROFILE_IMAGE_URI_COLUMN = "profileImageUri";
    public static final String EMAIL_COLUMN = "email";
    public static final String PASSWORD_COLUMN = "password";
    public static final String NOT_NULL = "not null";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FriendFinder.db";
    public static final String USER_TABLE_SPECS =
            // 'CREATE TABLE users (userId INTEGER PRIMARY KEY,
            // profileImageUri TEXT, email TEXT not null, password TEXT not null, UNIQUE (email))
            "CREATE TABLE " + USER_TABLE_NAME + " (" + USER_ID_COLUMN + " INTEGER PRIMARY KEY, " +
                    PROFILE_IMAGE_URI_COLUMN + " TEXT, " +
                    EMAIL_COLUMN + " TEXT " + NOT_NULL + ", " +
                    PASSWORD_COLUMN + " TEXT " + NOT_NULL + ", " +
                    "UNIQUE (" + EMAIL_COLUMN + "))";


    public DBUtil(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create user table
        sqLiteDatabase.execSQL(USER_TABLE_SPECS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EMAIL_COLUMN, user.getEmail());
        values.put(PASSWORD_COLUMN,user.getPassword());
        values.put(PROFILE_IMAGE_URI_COLUMN, user.getProfile().getProfileImageUri());

        long userId = db.insert(USER_TABLE_NAME, null, values);

        db.close();
        return userId;
    }
}
