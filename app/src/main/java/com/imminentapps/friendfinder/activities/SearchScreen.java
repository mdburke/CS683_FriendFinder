package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.fragments.SearchResultFragment;
import com.imminentapps.friendfinder.mocks.MockUserDatabase;

public class SearchScreen extends AppCompatActivity implements
        SearchResultFragment.OnListFragmentInteractionListener {
    private static final MockUserDatabase userDatabase = MockUserDatabase.getDatabase();
    private ListView searchResultsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
    }

    @Override
    public void onListFragmentInteraction(User user) {
        Log.i("DefaultTag", "Navigating to View Profile page for user: " + user.getProfile().getUsername());
        Intent intent = new Intent(this, ViewProfileScreen.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}