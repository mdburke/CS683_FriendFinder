package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.adapters.SearchForEventsAdapter;
import com.imminentapps.friendfinder.database.AppDatabase;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.interfaces.ActivityCommunication;
import com.imminentapps.friendfinder.utils.DBUtil;
import com.imminentapps.friendfinder.utils.UserUtil;

public class SearchForEventsScreen extends AppCompatActivity implements ActivityCommunication {
    private final String TAG = this.getClass().getSimpleName();
    private AppDatabase db;
    private RecyclerView recyclerView;
    private SearchForEventsAdapter searchForEventsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_events);
        db = DBUtil.getDBInstance();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.search_for_events_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        searchForEventsAdapter = new SearchForEventsAdapter(db.eventDao().getAll(), this);
        recyclerView.setAdapter(searchForEventsAdapter);

        // Setup user
        Intent intent = getIntent();
        currentUser = UserUtil.loadUser(intent.getCharSequenceExtra("currentUserEmail").toString());

        // TODO: Handle this case better
        if (currentUser == null) {
            throw new IllegalStateException("Edit Profile Screen was not able to locate the logged in user.");
        }
    }

    //***** ActivityCommunication interface methods ******//

    @Override
    public void userClicked(String eventId) {
        Log.i(TAG, "Navigating to View Event page for event: " + eventId);
        Intent intent = new Intent(this, ViewEventScreen.class);
        intent.putExtra("currentUserEmail", currentUser.getEmail());
        intent.putExtra("eventId", Integer.parseInt(eventId));
        startActivity(intent);
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }
}
