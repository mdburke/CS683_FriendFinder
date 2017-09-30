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
import com.imminentapps.friendfinder.database.AppDatabase;
import com.imminentapps.friendfinder.domain.Hobby;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.utils.Constants;
import com.imminentapps.friendfinder.utils.DBUtil;
import com.imminentapps.friendfinder.utils.UserUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  Adapted from http://blog.inapptext.com/recyclerview-creating-dynamic-lists-and-grids-in-android-1/
 */
public class SearchForFriendsScreen extends AppCompatActivity implements  SearchAdapter.UpdateMainClass {
    private final String TAG = this.getClass().getSimpleName();
    private AppDatabase db;
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

        // Set views
        recyclerView = findViewById(R.id.recycler_view);
        filterButton = findViewById(R.id.search_filterbutton);
        filterButton.setOnClickListener(view -> filterButtonClicked());
        friendSpinner = findViewById(R.id.search_spinner);
        db = DBUtil.getDBInstance();

        // Setup user
        Intent intent = getIntent();
        currentUser = UserUtil.loadUser(intent.getCharSequenceExtra("currentUserEmail").toString());

        // TODO: Handle this case better
        if (currentUser == null) {
            throw new IllegalStateException("Edit Profile Screen was not able to locate the logged in user.");
        }

        // Setup spinner
        List<String> choiceList = new ArrayList<>();
        choiceList.add(Constants.SEARCH_FILTER_ALL_USERS);
        choiceList.add(Constants.SEARCH_FILTER_FRIENDS);
        choiceList.add(Constants.SEARCH_FILTER_NOT_FRIENDS);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choiceList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        friendSpinner.setAdapter(adapter);

        // Setup RecyclerView
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //**** TODO: Fix super inefficient algorithm *******//

        // Set initial allUsers in list
        allUsers = new ArrayList<>();
        allUsers.addAll(db.userDao().getAll());
        allUsers.remove(currentUser);
        for (User user : allUsers) {
            Profile profile = db.profileDao().findById(user.getId());
            List<Hobby> hobbies = db.hobbyDao().getHobbyByProfileId(profile.getProfileId());
            profile.setHobbies(hobbies);
            user.setProfile(profile);
        }

        // Set up friends list
        friends = new ArrayList<>();
        friends.addAll(db.userDao().getAll());
        friends.remove(currentUser);
        friends = friends.stream()
                .filter(user -> user.isFriendsWith(currentUser.getId(), this))
                .collect(Collectors.toList());
        for (User user : friends) {
            Profile profile = db.profileDao().findById(user.getId());
            List<Hobby> hobbies = db.hobbyDao().getHobbyByProfileId(profile.getProfileId());
            profile.setHobbies(hobbies);
            user.setProfile(profile);
        }

        // Set up not friends list
        notFriends = new ArrayList<>();
        notFriends.addAll(db.userDao().getAll());
        notFriends.remove(currentUser);
        notFriends = notFriends.stream()
                .filter(user -> !user.isFriendsWith(currentUser.getId(), this))
                .collect(Collectors.toList());
        for (User user : notFriends) {
            Profile profile = db.profileDao().findById(user.getId());
            List<Hobby> hobbies = db.hobbyDao().getHobbyByProfileId(profile.getProfileId());
            profile.setHobbies(hobbies);
            user.setProfile(profile);
        }

        searchAdapter = new SearchAdapter(this, allUsers);
        recyclerView.setAdapter(searchAdapter);
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
