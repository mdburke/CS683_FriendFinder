package com.imminentapps.friendfinder.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;

public class CreateAccountScreen extends AppCompatActivity {
    // Instance Vars
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    // Views
    private TextView usernameView;
    private TextView passwordView;
    private TextView emailView;
    private TextView firstNameView;
    private TextView lastNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        usernameView = (TextView) findViewById(R.id.enterUsernameField);
        passwordView = (TextView) findViewById(R.id.enterPasswordField);
        emailView = (TextView) findViewById(R.id.enterEmailField);
        firstNameView = (TextView) findViewById(R.id.enterFirstNameField);
        lastNameView = (TextView) findViewById(R.id.enterLastNameField);


    }

    private boolean validateEmail(String email) {

    }

    private boolean validatePassword(String password) {

    }

    private boolean validateUsername(String username) {

    }
}
