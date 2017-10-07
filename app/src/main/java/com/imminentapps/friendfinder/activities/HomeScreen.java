package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.database.AppDatabase;
import com.imminentapps.friendfinder.database.DatabaseTask;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.utils.DBUtil;
import com.imminentapps.friendfinder.utils.UserUtil;

public class HomeScreen extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private TextView welcomeMessageTextView;
    private User currentUser;
    private AppDatabase db;
    private static final int ACTION_FOR_INTENT_CALLBACK = 1;

    ProgressBar progressBar;

    //************* Lifecycle Methods ************//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = DBUtil.getDBInstance();

        // Initialize on click listeners for buttons
        initializeOnClickListeners();

        // Initialize the welcomeMessageTextView field
        welcomeMessageTextView = findViewById(R.id.textView);

        initializeUserData();
    }

    private void initializeUserData() {
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
                // TODO: Figure out how to internationalize by using vars in strings.xml
                // Set welcome message to user name
                welcomeMessageTextView.setText(getString(R.string.home_title) + " " + currentUser.getProfile().getUsername());
            }
        }, new DatabaseTask.DatabaseTaskQuery<String, User>() {
            @Override
            public User execute(String... emails) {
                return UserUtil.loadUser(emails[0]);
            }
        });

        task.execute(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //************ Private Helper Methods *************//

    /**
     * Helper method that initializes the screen's buttons to have onClickListeners.
     */
    private void initializeOnClickListeners() {
        // Initialize Search For Friends button
        Button searchForFriendsButton = findViewById(R.id.buttonSearchForFriends);
        searchForFriendsButton.setOnClickListener((view) -> {
            Log.i(TAG, "Navigating to Search Page");
            Intent intent = new Intent(this, SearchForFriendsScreen.class);
            intent.putExtra("currentUserEmail", currentUser.getEmail());
            startActivity(intent);
        });

        // Initialize Edit Profile button
        Button editProfileButton = findViewById(R.id.buttonEditProfile);
        editProfileButton.setOnClickListener((view) -> {
            Log.i(TAG, "Navigating to Edit Profile page");
            Intent intent = new Intent(this, EditProfileScreen.class);
            intent.putExtra("currentUserEmail", currentUser.getEmail());
            startActivity(intent);
        });

        // Initialize Edit Account Settings button
        Button editAccountSettingsButton = findViewById(R.id.buttonEditAccountSettings);
        editAccountSettingsButton.setOnClickListener((view) -> {
            Log.i(TAG, "Navigating to Edit Account Settings page");
            Intent intent = new Intent(this, EditAccountSettingsScreen.class);
            intent.putExtra("currentUserEmail", currentUser.getEmail());
            startActivity(intent);
        });

        // Initialize Create Event button
        Button createEventButton = findViewById(R.id.buttonCreateEvent);
        createEventButton.setOnClickListener((view) -> {
            Log.i(TAG, "Navigating to Create Event page");
            Intent intent = new Intent(this, CreateEventScreen.class);
            intent.putExtra("currentUserEmail", currentUser.getEmail());
            startActivity(intent);
        });

        // Initialize Search for Events button
        Button searchForEventsButton = findViewById(R.id.buttonSearchForEvents);
        searchForEventsButton.setOnClickListener((view) -> {
            Log.i(TAG, "Navigating to Search For Events page");
            Intent intent = new Intent(this, SearchForEventsScreen.class);
            intent.putExtra("currentUserEmail", currentUser.getEmail());
            startActivity(intent);
        });
    }
}
