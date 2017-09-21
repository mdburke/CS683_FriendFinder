package com.imminentapps.friendfinder.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mburke on 9/19/17.
 */

public class DBUtil extends SQLiteOpenHelper {
    public static final String USER_TABLE_NAME = "users";
    public static final String USER_ID_COLUMN = "userId";
    public static final String PROFILE_IMAGE_COLUMN = "profileImage";
    public static final String EMAIL_COLUMN = "email";
    public static final String PASSWORD_COLUMN = "password";
    public static final String NOT_NULL = "not null";
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "FriendFinder.db";
    public static final String USER_TABLE_SPECS =
            // 'CREATE TABLE users (userId INTEGER PRIMARY KEY,
            // profileImageUri TEXT, email TEXT not null, password TEXT not null, UNIQUE (email))
            "CREATE TABLE " + USER_TABLE_NAME + " (" + USER_ID_COLUMN + " INTEGER PRIMARY KEY, " +
                    PROFILE_IMAGE_COLUMN + " BLOB, " +
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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EMAIL_COLUMN, user.getEmail());
        values.put(PASSWORD_COLUMN, user.getPassword());
        values.put(PROFILE_IMAGE_COLUMN, user.getProfile().getProfileImage());

        long userId = db.insert(USER_TABLE_NAME, null, values);

        db.close();
        return userId;
    }

    public List<User> getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {"*"};

        Cursor cursor = db.query(USER_TABLE_NAME, projection, null, null, null, null, null);

        List<User> userList = new ArrayList<>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String userEmail = cursor.getString(cursor.getColumnIndex(EMAIL_COLUMN));
                byte[] profileImage = cursor.getBlob(cursor.getColumnIndex(PROFILE_IMAGE_COLUMN));
                String password = cursor.getString(cursor.getColumnIndex(PASSWORD_COLUMN));

                Profile profile = new Profile(null, null, null, null, null, profileImage);

                User newUser = new User(userEmail, password, profile);
                userList.add(newUser);
            }
            cursor.close();
        }

        db.close();

        return userList;
    }

    public User getUser(String email) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String[] projection = { USER_ID_COLUMN, PROFILE_IMAGE_COLUMN, EMAIL_COLUMN, PASSWORD_COLUMN };
//        String selection = EMAIL_COLUMN + " = ?";
//        String[] selectionArgs = { email };
//
//        Cursor cursor = db.query(USER_TABLE_NAME, projection, null, null, null, null, null);
////        Cursor cursor = db.rawQuery("SELECT * FROM users", null);
//
//
//        Cursor cursor2 = db.rawQuery("SELECT * FROM users", null);
//
        User newUser = null;
//        cursor.moveToFirst();

        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteQueryBuilder builder  = new SQLiteQueryBuilder();
        builder.setTables("users");
        Cursor cursor = builder.query(db, null, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                String userEmail = cursor.getString(cursor.getColumnIndex(EMAIL_COLUMN));
                byte[] profileImage = cursor.getBlob(cursor.getColumnIndex(PROFILE_IMAGE_COLUMN));
                String password = cursor.getString(cursor.getColumnIndex(PASSWORD_COLUMN));

                Profile profile = new Profile(null, null, null, null, null, profileImage);

                newUser = new User(userEmail, password, profile);
                cursor.close();
            }
        }

        db.close();

        return newUser;
    }
}
