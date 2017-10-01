package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.database.AppDatabase;
import com.imminentapps.friendfinder.domain.Event;
import com.imminentapps.friendfinder.utils.DBUtil;

public class ViewEventScreen extends AppCompatActivity {
    private TextView eventTitle;
    private TextView eventDescription;
    private TextView eventLocation;
    private TextView eventDate;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_screen);
        db = DBUtil.getDBInstance();

        // Initialize views
        eventTitle = findViewById(R.id.viewEvent_eventTitle);
        eventDescription = findViewById(R.id.viewEvent_eventDescription);
        eventLocation = findViewById(R.id.viewEvent_eventLocation);
        eventDate = findViewById(R.id.viewEvent_eventDate);

        Intent intent = getIntent();
        int eventId = intent.getIntExtra("eventId", -1);

        if (eventId == -1) { throw new IllegalStateException("Event Id not passed correctly"); }

        Event currentEvent = db.eventDao().get(eventId);

        eventTitle.setText(currentEvent.getTitle());
        eventDate.setText(currentEvent.getDate().toString());
        eventDescription.setText(currentEvent.getDescription());
        eventLocation.setText(currentEvent.getLocation());
    }
}
