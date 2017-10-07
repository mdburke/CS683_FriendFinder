package com.imminentapps.friendfinder.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.adapters.SearchForEventsAdapter;
import com.imminentapps.friendfinder.database.DatabaseTask;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.interfaces.ActivityCommunication;
import com.imminentapps.friendfinder.utils.UserUtil;

import static com.imminentapps.friendfinder.utils.DBUtil.db;

public class SearchForEventsScreen extends AppCompatActivity implements ActivityCommunication {
    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView recyclerView;
    private SearchForEventsAdapter searchForEventsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private User currentUser;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_events);
        this.context = this;

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.search_for_events_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        initializeAdapter();
        initializeUserdata();
    }

    private void initializeAdapter() {
        DatabaseTask<Void, User> task = new DatabaseTask<>(new DatabaseTask.DatabaseTaskListener<SearchForEventsAdapter>() {
            @Override
            public void onFinished(SearchForEventsAdapter adapter) {
                searchForEventsAdapter = adapter;
                recyclerView.setAdapter(searchForEventsAdapter);
            }
        }, new DatabaseTask.DatabaseTaskQuery<Void, SearchForEventsAdapter>() {
            @Override
            public SearchForEventsAdapter execute(Void... params) {
                return new SearchForEventsAdapter(db.eventDao().getAll(), context);
            }
        });

        task.execute();
    }

    private void initializeUserdata() {
        // Grab the user information from the database based on the email passed in
        Intent intent = getIntent();
        String email = intent.getCharSequenceExtra("currentUserEmail").toString();

        DatabaseTask<String, User> task = new DatabaseTask<>(new DatabaseTask.DatabaseTaskListener<User>() {
            @Override
            public void onFinished(User user) {
                // TODO: Handle this case better
                if (user == null) {
                    throw new IllegalStateException("HomeScreen was not able to locate the logged in user.");
                }
                currentUser = user;
            }
        }, new DatabaseTask.DatabaseTaskQuery<String, User>() {
            @Override
            public User execute(String... emails) {
                return UserUtil.loadUser(emails[0]);
            }
        });

        task.execute(email);
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
