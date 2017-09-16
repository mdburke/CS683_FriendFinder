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

/**
 * Activity for new user to create account
 */
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

    /**
     * Validates the email, password and username. If these are valid,
     * this creates the new user, adds it to the mock DB, and navigates to the home screen.
     */
    private void createAccountAndNavigateHome() {
        // Guard Clause
        if (!validateEmail() || !validatePassword() || !validateUsername()) { return; }

        // Create the new user with the given information
        User newUser = new User(emailView.getText().toString(), passwordView.getText().toString(),
                new Profile(null, usernameView.getText().toString(), null));

        // Add the user to the MockDB
        userDatabase.addUser(newUser);

        // Navigate to the HomeScreen as if the user has just logged in
        Intent intent = new Intent(this, HomeScreen.class);
        intent.putExtra("email", emailView.getText());
        startActivity(intent);
    }

    /**
     * Method checks if email has an '@' symbol and also checks if the email is already
     * in the database.
     * @return - true if email is valid
     * TODO: add a more advanced regex check for a valid email.
     */
    private boolean validateEmail() {
        String email = emailView.getText().toString();
        if (!email.contains("@") || userDatabase.containsEmail(email)) {
            emailView.setError("Invalid email address!");
            emailView.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Method ensures password is 5 chars long.
     * @return - true if password is valid
     * TODO: add a more advanced regex check for a valid password with more requirements.
     */
    private boolean validatePassword() {
        if (passwordView.getText().length() < 5) {
            passwordView.setError("Invalid password!");
            passwordView.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Method ensures username is at least 5 chars long and is not already in the database.
     * @return - true if username is valid
     */
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
