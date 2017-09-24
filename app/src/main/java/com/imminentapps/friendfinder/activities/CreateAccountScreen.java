package com.imminentapps.friendfinder.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.database.AppDatabase;
import com.imminentapps.friendfinder.utils.DBUtil;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Activity for new user to create account
 */
public class CreateAccountScreen extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private static AppDatabase db;

    // Views
    private TextView usernameView;
    private TextView passwordView;
    private TextView emailView;
    private TextView firstNameView;
    private TextView lastNameView;
    private Button createAccountButton;
    private String profileImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        db = DBUtil.getDBInstance();

        // Initialize subviews
        usernameView = findViewById(R.id.enterUsernameField);
        passwordView = findViewById(R.id.enterPasswordField);
        emailView = findViewById(R.id.enterEmailField);
        firstNameView = findViewById(R.id.enterFirstNameField);
        lastNameView = findViewById(R.id.enterLastNameField);
        createAccountButton = findViewById(R.id.createAccountButton);

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

        // Create the new profile and user with the given information
        Profile profile = new Profile(null,
                usernameView.getText().toString(), null,
                firstNameView.getText().toString(),
                lastNameView.getText().toString(),
                profileImageUri);

        User newUser = new User(emailView.getText().toString(),
                passwordView.getText().toString(), profile);

        // Add the user to the Database
        // TODO: Figure out how to do this all in one transaction
        db.userDao().insertUsers(newUser);
        db.profileDao().insert(newUser.getProfile());

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
        if (!email.contains("@") || (db.userDao().findByEmail(email) != null)) {
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
        if (username.length() < 5 || (db.profileDao().findByUsername(username) != null)) {
            usernameView.setError("Invalid username!");
            usernameView.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Use the photo picker to grab an image.
     */
    public void selectProfileImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    /**
     * Callback for the profile image picker. This grabs the data from the photo picker
     * and stores it in the file system for easy loading in the future.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FileOutputStream outputStream = null;

        // Create a unique file name for this image.
        String filename = emailView.getText().toString().concat("_profileImage_").concat(UUID.randomUUID().toString());
        // Grab the data uri
        Uri filepath = data.getData();

        if (resultCode == RESULT_OK) {
            try {
                // Adapted from https://stackoverflow.com/questions/2227209/how-to-get-the-images-from-device-in-android-java-application
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                byte[] imageData = getBytes(inputStream);

                // Write the image to the file system.
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(imageData);

                // Save the uri to the profile
                profileImageUri = filename;
            } catch (Exception e) {
                Log.e(TAG, "Error saving profile image.");
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing output stream.");
                    }
                }
            }
            Log.i(TAG, "Profile image uri is " + profileImageUri);
        }
    }

    // Adapted from https://stackoverflow.com/questions/10296734/image-uri-to-bytesarray
    /**
     * Converts an input stream into a byte array
     */
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
