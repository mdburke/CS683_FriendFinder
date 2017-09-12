package com.imminentapps.friendfinder.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;

public class HomeScreen extends AppCompatActivity {
    private static final String DEFAULT_TAG = "DefaultTag";
    private TextView outputTextView;
    private TextView welcomeMessageTextView;

    //************* Lifecycle Methods ************//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the outputTextView and welcomeMessageTextView fields
        outputTextView = (TextView) findViewById(R.id.outputText);
        welcomeMessageTextView = (TextView) findViewById(R.id.textView);

        // Grab the email from the loginScreen
        Intent intent = getIntent();
        CharSequence email = intent.getCharSequenceExtra("email");
        Log.i("email", email.toString());

        // TODO: Figure out how to internationalize by using vars in strings.xml
        welcomeMessageTextView.setText(getString(R.string.home_title) + " " + email.toString());

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
        outputTextView.setText(savedInstanceState.getCharSequence("OutputText"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("OutputText", outputTextView.getText());
    }

    //************ Private Helper Methods *************//

    /**
     * Helper method that initializes the screen's buttons to have onClickListeners.
     * Buttons will log that they have been clicked for now, in place
     * of transitioning to other activities.
     */
    @SuppressLint("SetTextI18n")    // The offending strings are for tests only.
    private void initializeOnClickListeners() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((view) -> Snackbar
                .make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        // TODO: Make the following buttons transition to other activities

        Button searchForFriendsButton = (Button) findViewById(R.id.buttonSearchForFriends);
        searchForFriendsButton.setOnClickListener((view) -> {
            Log.i(DEFAULT_TAG, "Go to SearchForFriends page here.");
            outputTextView.setText("SearchForFriends last clicked.");
        });

        Button editProfileButton = (Button) findViewById(R.id.buttonEditProfile);
        editProfileButton.setOnClickListener((view) -> {
            Log.i(DEFAULT_TAG, "Go to EditProfile page here.");
            outputTextView.setText("EditProfile last clicked.");
        });

        Button editAccountSettingsButton = (Button) findViewById(R.id.buttonEditAccountSettings);
        editAccountSettingsButton.setOnClickListener((view) -> {
            Log.i(DEFAULT_TAG, "Go to EditAccountSettings page here.");
            outputTextView.setText("EditAccountSettings last clicked.");
        });
    }
}
