package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.fragments.SearchMenuBarFragment;
import com.imminentapps.friendfinder.fragments.SearchResultFragment;
import com.imminentapps.friendfinder.utils.UserUtil;

public class SearchScreen extends AppCompatActivity implements
        SearchResultFragment.OnListFragmentInteractionListener,
        SearchMenuBarFragment.SearchMenuBarListener {

    private final String TAG = this.getClass().getSimpleName();
    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);

        Intent intent = getIntent();
        currentUser = UserUtil.loadUser(intent.getCharSequenceExtra("currentUserEmail").toString());

        // TODO: Handle this case better
        if (currentUser == null) {
            throw new IllegalStateException("Edit Profile Screen was not able to locate the logged in user.");
        }
    }

    @Override
    public void onListFragmentInteraction(User user) {
        Log.i(TAG, "Navigating to View Profile page for user: " + user.getProfile().getUsername());
        Intent intent = new Intent(this, ViewProfileScreen.class);
        intent.putExtra("currentUserEmail", user.getEmail());
        intent.putExtra("selectedUserEmail", currentUser.getEmail());
        startActivity(intent);
    }

    @Override
    public void onSearchButtonClicked(String filter) {
        SearchResultFragment searchResultFragment =
                (SearchResultFragment) getSupportFragmentManager().findFragmentById(R.id.search_result_fragment);

        searchResultFragment.filterSearchResults(filter);
    }
}
