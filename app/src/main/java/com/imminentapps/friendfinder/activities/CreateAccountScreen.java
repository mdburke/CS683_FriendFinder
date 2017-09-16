package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.mocks.MockUserDatabase;

public class CreateAccountScreen extends AppCompatActivity {
    private static final MockUserDatabase userDatabase = MockUserDatabase.getDatabase();

    // Views
    private TextView usernameView;
    private TextView passwordView;
    private TextView emailView;
    private TextView firstNameView;
    private TextView lastNameView;
    private Button createAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Initialize subviews
        usernameView = (TextView) findViewById(R.id.enterUsernameField);
        passwordView = (TextView) findViewById(R.id.enterPasswordField);
        emailView = (TextView) findViewById(R.id.enterEmailField);
        firstNameView = (TextView) findViewById(R.id.enterFirstNameField);
        lastNameView = (TextView) findViewById(R.id.enterLastNameField);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);

        // Add onClickListener
        createAccountButton.setOnClickListener((view -> createAccountAndNavigateHome()));
    }

    private void createAccountAndNavigateHome() {
        // Guard Clause
        if (!validateEmail() || !validatePassword() || !validateUsername()) { return; }

        User newUser = new User(emailView.getText().toString(), passwordView.getText().toString(),
                new Profile(null, usernameView.getText().toString(), null));

        userDatabase.addUser(newUser);

        Intent intent = new Intent(this, HomeScreen.class);
        intent.putExtra("email", emailView.getText());
        startActivity(intent);
    }

    private boolean validateEmail() {
        String email = emailView.getText().toString();
        if (!email.contains("@") || userDatabase.containsEmail(email)) {
            emailView.setError("Invalid email address!");
            emailView.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        if (passwordView.getText().length() < 5) {
            passwordView.setError("Invalid password!");
            passwordView.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateUsername() {
        String username = usernameView.getText().toString();
        if (username.length() < 5 || userDatabase.containsUsername(username)) {
            usernameView.setError("Invalid username!");
            usernameView.requestFocus();
            return false;
        }
        return true;
    }
}
