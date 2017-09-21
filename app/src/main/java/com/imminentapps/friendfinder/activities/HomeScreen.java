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
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.utils.DBUtil;

public class HomeScreen extends AppCompatActivity {
    private static final String DEFAULT_TAG = "DefaultTag";
    private DBUtil dbUtil;
    private TextView welcomeMessageTextView;
    private User currentUser;

    //************* Lifecycle Methods ************//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbUtil = new DBUtil(getApplicationContext());

        // Initialize the welcomeMessageTextView field
        welcomeMessageTextView = (TextView) findViewById(R.id.textView);

        // Grab the user information from the database based on the email passed in
        Intent intent = getIntent();
        currentUser = dbUtil.getUser(intent.getCharSequenceExtra("email").toString());
        if (currentUser == null) {
            throw new IllegalStateException("HomeScreen was not able to locate" +
                " the logged in user.");
        }

        Log.i("email", currentUser.getEmail());

        // TODO: Figure out how to internationalize by using vars in strings.xml
        // Set welcome message to user name
        welcomeMessageTextView.setText(getString(R.string.home_title) + " " +
                currentUser.getProfile().getUsername());

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
        Button searchForFriendsButton = (Button) findViewById(R.id.buttonSearchForFriends);
        searchForFriendsButton.setOnClickListener((view) -> {
            Log.i(DEFAULT_TAG, "Navigating to Search Page");
            Intent intent = new Intent(this, SearchScreen.class);
            intent.putExtra("email", currentUser.getEmail());
            startActivity(intent);
        });

        // Initialize Edit Profile button
        Button editProfileButton = (Button) findViewById(R.id.buttonEditProfile);
        editProfileButton.setOnClickListener((view) -> {
            Log.i(DEFAULT_TAG, "Navigating to Edit Profile page");
            Intent intent = new Intent(this, EditProfileScreen.class);
            intent.putExtra("email", currentUser.getEmail());
            startActivity(intent);
        });

        // Initialize Edit Account Settings button
        Button editAccountSettingsButton = (Button) findViewById(R.id.buttonEditAccountSettings);
        editAccountSettingsButton.setOnClickListener((view) -> {
            Log.i(DEFAULT_TAG, "Navigating to Edit Account Settings page");
            Intent intent = new Intent(this, EditAccountSettingsScreen.class);
            intent.putExtra("email", currentUser.getEmail());
            startActivity(intent);
        });
    }
}
