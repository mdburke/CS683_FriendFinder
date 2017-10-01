package com.imminentapps.friendfinder.utils;

import com.imminentapps.friendfinder.domain.Event;

import static com.imminentapps.friendfinder.utils.DBUtil.db;

/**
 * Created by mburke on 10/1/17.
 */

public class EventUtil {
    public static Event loadEvent(int eventId) {
        // Get User
        Event event = db.eventDao().get(eventId);

        return event;
    }
}
