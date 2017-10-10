package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.database.DatabaseTask;
import com.imminentapps.friendfinder.domain.Event;
import com.imminentapps.friendfinder.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

import static com.imminentapps.friendfinder.utils.DBUtil.db;

public class ViewEventScreen extends AppCompatActivity {
    private TextView eventTitle;
    private TextView eventDescription;
    private TextView eventLocation;
    private TextView eventDate;
    private Button addToCalendarButton;
    private Button showOnMapButton;
    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_screen);

        // Initialize views
        eventTitle = findViewById(R.id.viewEvent_eventTitle);
        eventDescription = findViewById(R.id.viewEvent_eventDescription);
        eventLocation = findViewById(R.id.viewEvent_eventLocation);
        eventDate = findViewById(R.id.viewEvent_eventDate);
        addToCalendarButton = findViewById(R.id.viewEvent_addToCal_button);
        addToCalendarButton.setOnClickListener(view -> addToCalendarClicked());
        showOnMapButton = findViewById(R.id.viewEvent_showOnMap_button);
        showOnMapButton.setOnClickListener(view -> showOnMapClicked());

        Intent intent = getIntent();
        int eventId = intent.getIntExtra("eventId", -1);

        if (eventId == -1) { throw new IllegalStateException("Event Id not passed correctly"); }

        initializeEventData(eventId);
    }

    /**
     * Initialize the event data using an async DatabaseTask
     * @param eventId
     */
    private void initializeEventData(int eventId) {
        DatabaseTask<Integer, Event> task = new DatabaseTask<>(new DatabaseTask.DatabaseTaskListener<Event>() {
            @Override
            public void onFinished(Event event) {
                currentEvent = event;
                eventTitle.setText(currentEvent.getTitle());
                eventDate.setText(currentEvent.getDate().toString());
                eventDescription.setText(currentEvent.getDescription());
                eventLocation.setText(currentEvent.getLocation());
            }
        }, new DatabaseTask.DatabaseTaskQuery<Integer, Event>() {
            @Override
            public Event execute(Integer... eventIDs) {
                return db.eventDao().get(eventIDs[0]);
            }
        });
        task.execute(eventId);
    }

    private void addToCalendarClicked() {
        Date startTime = currentEvent.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.HOUR, 1);
        Date endTime = calendar.getTime();

        Intent intent = new Intent()
                .setAction(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, currentEvent.getTitle())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, currentEvent.getLocation())
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, currentEvent.getDate().getTime())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTime())
                .putExtra(CalendarContract.Events.DESCRIPTION, currentEvent.getDescription());

        startActivity(intent);
    }

    private void showOnMapClicked() {
        String location = null;
        try {
            // URL encode the address as required.
            location = URLEncoder.encode(eventLocation.getText().toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Turn the String into a Uri as required
        Uri geolocation = Uri.parse(Constants.GEO_PREFIX + Uri.encode(location));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geolocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
