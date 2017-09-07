package com.imminentapps.friendfinder;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {
    private static final String DEFAULT_TAG = "DefaultTag";

    /******** Lifecycle Methods *********/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeButtonListeners();
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

    /***** Private Helper Methods *******/
    private void initializeButtonListeners() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((view) -> Snackbar
                .make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        TextView searchForFriendsButton = (TextView) findViewById(R.id.buttonSearchForFriends);
        searchForFriendsButton.setOnClickListener((view) ->
                Log.i(DEFAULT_TAG, "Go to SearchForFriends page here."));

        TextView editProfileButton = (TextView) findViewById(R.id.buttonEditProfile);
        editProfileButton.setOnClickListener((view) ->
                Log.i(DEFAULT_TAG, "Go to EditProfile page here."));

        TextView editAccountSettingsButton = (TextView) findViewById(R.id.buttonEditAccountSettings);
        editAccountSettingsButton.setOnClickListener((view) ->
                Log.i(DEFAULT_TAG, "Go to EditAccountSettings page here."));
    }
}
