package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;

public class ViewProfileScreen extends AppCompatActivity {
    private User currentUser;
    private Profile currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_screen);

        Intent intent = getIntent();

        try {
            currentUser = (User) intent.getSerializableExtra("user");
        } catch (ClassCastException e) {
            throw new IllegalStateException("Activity was not passed a valid user object.");
        }

        currentProfile = currentUser.getProfile();

        TextView usernameView = (TextView) findViewById(R.id.usernameTextView);
        ListView listView = (ListView) findViewById(R.id.hobbyListView);
        TextView aboutMeView = (TextView) findViewById(R.id.aboutMeTextView);

        usernameView.setText(currentProfile.getUsername());
        aboutMeView.setText(currentProfile.getAboutMeSection());

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, currentProfile.getHobbies());
        listView.setAdapter(adapter);
    }
}
