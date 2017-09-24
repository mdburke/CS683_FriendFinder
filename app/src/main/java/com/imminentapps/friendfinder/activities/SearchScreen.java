package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.fragments.SearchResultFragment;

public class SearchScreen extends AppCompatActivity implements
        SearchResultFragment.OnListFragmentInteractionListener {
    private final String TAG = this.getClass().getSimpleName();
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);

        Intent intent = getIntent();
        try {
            loggedInUser = (User) intent.getSerializableExtra("loggedInUser");
        } catch (ClassCastException e) {
            throw new IllegalStateException("Activity was not passed a valid user object.");
        }
    }

    @Override
    public void onListFragmentInteraction(User user) {
        Log.i(TAG, "Navigating to View Profile page for user: " + user.getProfile().getUsername());
        Intent intent = new Intent(this, ViewProfileScreen.class);
        intent.putExtra("viewedUser", user);
        intent.putExtra("loggedInUser", loggedInUser);
        startActivity(intent);
    }
}
