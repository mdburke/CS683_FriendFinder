package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.database.AppDatabase;
import com.imminentapps.friendfinder.utils.DBUtil;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;

public class HomeScreen extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private TextView welcomeMessageTextView;
    private User currentUser;
    private AppDatabase db;

    //************* Lifecycle Methods ************//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = DBUtil.getDBInstance();

        // Initialize the welcomeMessageTextView field
        welcomeMessageTextView = findViewById(R.id.textView);

        // Grab the user information from the database based on the email passed in
        Intent intent = getIntent();
        currentUser = db.userDao().findByEmail((intent.getCharSequenceExtra("currentUserEmail").toString()));

        // TODO: Handle this case better
        if (currentUser == null) {
            throw new IllegalStateException("HomeScreen was not able to locate the logged in user.");
        }

        // TODO: Figure out how to get Room to pull the Profile info in with the previous Query
        Profile userProfile = db.profileDao().findById(currentUser.getId());
        currentUser.setProfile(userProfile);

        // TODO: Figure out how to internationalize by using vars in strings.xml
        // Set welcome message to user name
        welcomeMessageTextView.setText(getString(R.string.home_title) + " " + currentUser.getProfile().getUsername());

        // Initialize on click listeners for buttons
        initializeOnClickListeners();
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
            Intent intent = new Intent(this, SearchScreen.class);
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
    }
}
