package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.domain.User;

public class ViewProfileScreen extends AppCompatActivity {
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        try {
            currentUser = (User) intent.getSerializableExtra("user");
        } catch (ClassCastException e) {
            throw new IllegalStateException("Activity was not passed a valid user object.");
        }

        setContentView(R.layout.activity_view_profile_screen);
    }
}
