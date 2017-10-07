package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.database.AppDatabase;
import com.imminentapps.friendfinder.database.DatabaseTask;
import com.imminentapps.friendfinder.domain.Hobby;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.utils.DBUtil;
import com.imminentapps.friendfinder.utils.UserUtil;

import java.io.FileInputStream;

import static com.imminentapps.friendfinder.utils.UserUtil.getAboutMeText;
import static com.imminentapps.friendfinder.utils.UserUtil.getHobbyListText;

public class EditProfileScreen extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private AppDatabase db;

    private User currentUser;
    private ImageView profileImageView;
    private EditText hobbyList;
    private EditText aboutMeView;
    private TextView usernameView;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);
        db = DBUtil.getDBInstance();


        // Initialize vars/fields
        usernameView = findViewById(R.id.editprofile_usernameTextView);
        aboutMeView = findViewById(R.id.editprofile_aboutMeEdit);
        profileImageView = findViewById(R.id.editprofile_profileImageView);
        hobbyList = findViewById(R.id.editprofile_hobbyList);
        saveButton = findViewById(R.id.editprofile_saveButton);

        // Add onClickListeners
        saveButton.setOnClickListener(view -> saveButtonClicked());

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
                // Set the views with the user data
                usernameView.setText(currentUser.getProfile().getUsername());
                aboutMeView.setText(getAboutMeText(currentUser));
                hobbyList.setText(getHobbyListText(currentUser));
                setupProfileImage();
            }
        }, new DatabaseTask.DatabaseTaskQuery<String, User>() {
            @Override
            public User execute(String... emails) {
                return UserUtil.loadUser(emails[0]);
            }
        });

        task.execute(email);
    }

    private void saveButtonClicked() {
        // Get the new data to save
        int profileId = currentUser.getProfile().getProfileId();
        Hobby[] hobbies = UserUtil.textToHobbyArray(profileId, hobbyList.getText().toString());
        String aboutMe = aboutMeView.getText().toString();
        Profile profile = currentUser.getProfile();
        profile.setAboutMeSection(aboutMe);

        DatabaseTask<String, Void> task = new DatabaseTask<>(new DatabaseTask.DatabaseTaskListener<Void>() {
            @Override
            public void onFinished(Void result) {

            }
        }, new DatabaseTask.DatabaseTaskQuery<String, Void>() {
            @Override
            public Void execute(String... emails) {
                // Save button
                db.hobbyDao().insert(hobbies);
                db.profileDao().update(profile);
                return null;
            }
        });

        task.execute();
    }

    /**
     * Grabs the profile image from the file system, transforms to Bitmap
     * and set the profileImageView to that bitmap.
     */
    private void setupProfileImage() {
        // TODO: Add a default image if bitmap is null or the uri is null
        if (currentUser.getProfile().getProfileImageUri() != null) {
            Bitmap bitmap = null;
            String uri = currentUser.getProfile().getProfileImageUri();
            FileInputStream inputStream;

            try {
                inputStream = openFileInput(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                Log.e(this.getClass().toString(), "Error loading image");
                e.printStackTrace();
            }

            if (bitmap != null) {
                profileImageView.setImageBitmap(bitmap);
            }
        }
    }
}
