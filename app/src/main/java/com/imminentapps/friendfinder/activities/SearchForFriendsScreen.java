package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.adapters.SearchAdapter;
import com.imminentapps.friendfinder.database.DatabaseTask;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.interfaces.ActivityCommunication;
import com.imminentapps.friendfinder.utils.Constants;
import com.imminentapps.friendfinder.utils.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.imminentapps.friendfinder.utils.DBUtil.db;

/**
 *  Adapted from http://blog.inapptext.com/recyclerview-creating-dynamic-lists-and-grids-in-android-1/
 */
public class SearchForFriendsScreen extends AppCompatActivity implements ActivityCommunication {
    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private List<User> allUsers;
    private List<User> friends;
    private List<User> notFriends;
    private RecyclerView.LayoutManager layoutManager;
    private Spinner friendSpinner;
    private Button filterButton;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_friends_screen);

        // Initialize views
        recyclerView = findViewById(R.id.recycler_view);
        filterButton = findViewById(R.id.search_filterbutton);
        filterButton.setOnClickListener(view -> filterButtonClicked());
        friendSpinner = findViewById(R.id.search_spinner);

        // Setup user
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

                // Setup user lists
                initializeUserLists();
            }
        }, new DatabaseTask.DatabaseTaskQuery<String, User>() {
            @Override
            public User execute(String... emails) {
                return UserUtil.loadUser(emails[0]);
            }
        });

        task.execute(email);
    }

    // This logic needs a lot of work and is very inefficient
    // TODO: Fix this filtering logic
    private void filterButtonClicked() {
        String filter = friendSpinner.getSelectedItem().toString();

        switch (filter) {
            case Constants.SEARCH_FILTER_ALL_USERS:
                searchAdapter = new SearchAdapter(this, allUsers);
                recyclerView.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
                break;
            case Constants.SEARCH_FILTER_FRIENDS:
                searchAdapter = new SearchAdapter(this, friends);
                recyclerView.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
                break;
            case Constants.SEARCH_FILTER_NOT_FRIENDS:
                searchAdapter = new SearchAdapter(this, notFriends);
                recyclerView.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * Sets up the RecyclerView
     */
    private void initializeRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        searchAdapter = new SearchAdapter(this, allUsers);
        recyclerView.setAdapter(searchAdapter);
    }

    /**
     * Sets up the spinner
     */
    private void initializeSpinner() {
        List<String> choiceList = new ArrayList<>();
        choiceList.add(Constants.SEARCH_FILTER_ALL_USERS);
        choiceList.add(Constants.SEARCH_FILTER_FRIENDS);
        choiceList.add(Constants.SEARCH_FILTER_NOT_FRIENDS);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choiceList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        friendSpinner.setAdapter(adapter);
    }

    /**
     * Initializes the user lists that are used in place of a true "search" algorithm
     * This definitely needs to be fixed eventually.
     * TODO: Create a true search/filter algorithm
     */
    private void initializeUserLists() {

        DatabaseTask<Void, Void> task = new DatabaseTask<>(new DatabaseTask.DatabaseTaskListener() {
            @Override
            public void onFinished(Object result) {
                // Setup spinner
                initializeSpinner();

                // Setup RecyclerView
                initializeRecyclerView();
            }
        }, new DatabaseTask.DatabaseTaskQuery() {
            @Override
            public Object execute(Object[] params) {
                // Set initial allUsers in list
                allUsers = new ArrayList<>();
                allUsers.addAll(db.userDao().getAll());
                allUsers.remove(currentUser);
                for (User user : allUsers) {
                    UserUtil.loadUser(user.getEmail());
                }

                // Set up friends list
                friends = new ArrayList<>();
                friends.addAll(db.userDao().getAll());
                friends.remove(currentUser);
                // Filter out anyone who isn't friends with current user
                friends = friends.stream()
                        .filter(user -> user.isFriendsWith(currentUser.getId(), getApplicationContext()))
                        .collect(Collectors.toList());
                for (User user : friends) {
                    UserUtil.loadUser(user.getEmail());
                }

                // Set up not friends list
                notFriends = new ArrayList<>();
                notFriends.addAll(db.userDao().getAll());
                notFriends.remove(currentUser);
                // Filter out anyone who is friends with current user
                notFriends = notFriends.stream()
                        .filter(user -> !user.isFriendsWith(currentUser.getId(), getApplicationContext()))
                        .collect(Collectors.toList());
                for (User user : notFriends) {
                    UserUtil.loadUser(user.getEmail());
                }
                return null;
            }
        });

        task.execute();

    }

    //***** ActivityCommunication interface methods ******//

    @Override
    public void userClicked(String email) {
        Log.i(TAG, "Navigating to View Profile page for user: " + email);
        Intent intent = new Intent(this, ViewProfileScreen.class);
        intent.putExtra("currentUserEmail", currentUser.getEmail());
        intent.putExtra("selectedUserEmail", email);
        startActivity(intent);
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }
}
