package com.imminentapps.friendfinder.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;

import java.util.Calendar;
import java.util.Date;

public class CreateEventScreen extends AppCompatActivity {
    private TextView eventTitle;
    private TextView eventLocation;
    private DatePicker eventDate;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_screen);

        // Initialize views
        eventTitle = findViewById(R.id.eventName_EditText);
        eventLocation = findViewById(R.id.eventLocation_EditText);
        eventDate = findViewById(R.id.eventDatePicker);
        saveButton = findViewById(R.id.createEvent_Savebutton);
        saveButton.setOnClickListener(view -> saveButtonClicked());
    }

    private void saveButtonClicked() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(eventDate.getYear(), eventDate.getMonth(), eventDate.getDayOfMonth());
        Date date = calendar.getTime();
        Log.i("tag", date.toString());
    }
}
