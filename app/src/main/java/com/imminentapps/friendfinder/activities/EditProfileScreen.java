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
import com.imminentapps.friendfinder.domain.Hobby;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.utils.DBUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.util.List;

public class EditProfileScreen extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private AppDatabase db;

    private User currentUser;
    private ImageView profileImageView;
    private EditText hobbyList;
    private EditText aboutMeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);
        db = DBUtil.getDBInstance();

        Intent intent = getIntent();
        currentUser = db.userDao().findByEmail((intent.getCharSequenceExtra("email").toString()));


        // TODO: Handle this case better
        if (currentUser == null) {
            throw new IllegalStateException("Edit Profile Screen was not able to locate the logged in user.");
        }
        // TODO: Figure out how to get Room to pull the Profile info in with the previous Query
        Profile userProfile = db.profileDao().findById(currentUser.getId());
        currentUser.setProfile(userProfile);
        int profileId = currentUser.getProfile().getProfileId();
        List<Hobby> hobbyfromdb = db.hobbyDao().getHobbyByProfileId(profileId);
        currentUser.getProfile().setHobbies(hobbyfromdb);

        // Initialize vars/fields
        TextView usernameView = findViewById(R.id.editprofile_usernameTextView);
        aboutMeView = findViewById(R.id.editprofile_aboutMeEdit);
        profileImageView = findViewById(R.id.editprofile_profileImageView);
        hobbyList = findViewById(R.id.editprofile_hobbyList);
        Button saveButton = findViewById(R.id.editprofile_saveButton);

        // Set text
        usernameView.setText(currentUser.getProfile().getUsername());

        if (currentUser.getProfile().getAboutMeSection() != null) {
            aboutMeView.setText(currentUser.getProfile().getAboutMeSection());
        }

        List<Hobby> hobbies = currentUser.getProfile().getHobbies();
        String hobbieString = hobbyListToText(hobbies).toString();

        hobbyList.setText(hobbieString);

        // Add onClickListeners
        saveButton.setOnClickListener(view -> saveButtonClicked());

        // Get the profile image
        setupProfileImage();
    }

    private void saveButtonClicked() {
        // Get the new data to save
        int profileId = currentUser.getProfile().getProfileId();
        Hobby[] hobbies = textToHobbyArray(profileId, hobbyList.getText().toString());
        String aboutMe = aboutMeView.getText().toString();
        Profile profile = currentUser.getProfile();
        profile.setAboutMeSection(aboutMe);

        // Save button
        db.hobbyDao().insert(hobbies);
        db.profileDao().update(profile);
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

    private StringBuilder hobbyListToText(List<Hobby> hobbies) {
        StringBuilder hobbyString = new StringBuilder();

        for (int i = 0; i < hobbies.size(); i++) {
            if (i != 0) {
                hobbyString.append(", ");
            }
            hobbyString.append(hobbies.get(i).getHobby());
        }

        return hobbyString;
    }

    private Hobby[] textToHobbyArray(int profileId, String hobbies) {
        String[] hobbyStringArray = StringUtils.split(hobbies, ", ");
        Hobby[] hobbyArray = new Hobby[hobbyStringArray.length];

        for (int i = 0; i < hobbyArray.length; i++) {
            hobbyArray[i] = new Hobby(profileId, hobbyStringArray[i]);
        }

        return hobbyArray;
    }
}
