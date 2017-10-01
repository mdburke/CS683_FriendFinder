package com.imminentapps.friendfinder.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Room Date converter class described in Android docs:
 * https://developer.android.com/topic/libraries/architecture/room.html
 * 
 * Created by mburke on 10/1/17.
 */
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
