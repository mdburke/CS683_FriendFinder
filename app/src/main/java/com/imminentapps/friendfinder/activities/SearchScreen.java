package com.imminentapps.friendfinder.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    }
}
