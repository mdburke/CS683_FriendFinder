package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.fragments.SearchResultFragment;
import com.imminentapps.friendfinder.utils.DBUtil;

public class SearchScreen extends AppCompatActivity implements SearchResultFragment.OnListFragmentInteractionListener {
    private DBUtil dbUtil;
    private ListView searchResultsView;
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        dbUtil = new DBUtil(getApplicationContext());

        Intent intent = getIntent();
        try {
            loggedInUser = dbUtil.getUser(intent.getCharSequenceExtra("email").toString());
        } catch (ClassCastException e) {
            throw new IllegalStateException("Activity was not passed a valid user object.");
        }
    }

    @Override
    public void onListFragmentInteraction(User user) {
        Log.i("DefaultTag", "Navigating to View Profile page for user: " + user.getProfile().getUsername());
        Intent intent = new Intent(this, ViewProfileScreen.class);
        intent.putExtra("viewedUser", user.getEmail());
        intent.putExtra("email", loggedInUser.getEmail());
        startActivity(intent);
    }
}
