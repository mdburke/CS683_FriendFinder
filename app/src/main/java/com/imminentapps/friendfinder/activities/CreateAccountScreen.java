package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.mocks.MockUserDatabase;
import com.imminentapps.friendfinder.utils.DBUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Activity for new user to create account
 */
public class CreateAccountScreen extends AppCompatActivity {
    private static final MockUserDatabase userDatabase = MockUserDatabase.getDatabase();
    private DBUtil dbUtil;

    // Views
    private TextView usernameView;
    private TextView passwordView;
    private TextView emailView;
    private TextView firstNameView;
    private TextView lastNameView;
    private Button createAccountButton;
    private Bitmap profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        dbUtil = new DBUtil(getApplicationContext());

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
        User newUser = new User(
                emailView.getText().toString(),
                passwordView.getText().toString(),
                new Profile(null,
                        usernameView.getText().toString(),
                        null,
                        firstNameView.getText().toString(),
                        lastNameView.getText().toString(),
                        compressImage(profileImage)));

        // Add the user to the MockDB
        try {
            dbUtil.addUser(newUser);
        } catch (SQLiteConstraintException e) {
            emailView.setError("Invalid email address!");
            emailView.requestFocus();
            return;
        }

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

    public void selectProfileImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    // https://stackoverflow.com/questions/2227209/how-to-get-the-images-from-device-in-android-java-application
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();

            try {
                profileImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // https://acadgild.com/blog/save-retrieve-image-sqlite-android/
    private byte[] compressImage(Bitmap image) {
        if (image != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
        }
        return null;
    }
}
