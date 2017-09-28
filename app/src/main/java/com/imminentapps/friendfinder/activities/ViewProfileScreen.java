package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.utils.UserUtil;

import java.io.FileInputStream;

public class ViewProfileScreen extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private final String TAG = this.getClass().getSimpleName();
    // Distance used to test for a valid swipe
    private static final int SWIPE_MIN_DISTANCE = 120;

    // Instance vars
    private User selectedUser;
    private User currentUser;
    private Profile selectedProfile;
    private GestureDetectorCompat gestureDetectorCompat;
    private ImageView friendIcon;
    private ImageView profileImageView;
    private TextView usernameView;
    private ListView listView;
    private TextView aboutMeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_screen);

        Intent intent = getIntent();

        // Grab the logged in user info and the viewed user info
        selectedUser = UserUtil.loadUser(intent.getCharSequenceExtra("selectedUserEmail").toString());
        currentUser = UserUtil.loadUser(intent.getCharSequenceExtra("currentUserEmail").toString());

        // TODO: Handle this case better
        if (currentUser == null || selectedUser == null) {
            throw new IllegalStateException("View Profile Screen was not able to locate the users.");
        }

        // Initialize vars/fields
        selectedProfile = selectedUser.getProfile();
        usernameView = findViewById(R.id.editprofile_usernameTextView);
        listView = findViewById(R.id.hobbyListView);
        aboutMeView = findViewById(R.id.editprofile_aboutMeTextView);
        profileImageView = findViewById(R.id.editprofile_profileImageView);
        friendIcon = findViewById(R.id.friendIcon);
        gestureDetectorCompat = new GestureDetectorCompat(this, this);

        // Setup the view based on the data
        usernameView.setText(selectedProfile.getUsername());
        aboutMeView.setText(UserUtil.getAboutMeText(selectedUser));

        // Setup the list adapter
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedProfile.getHobbiesAsStrings());
        listView.setAdapter(adapter);

        // Get the profile image
        setupProfileImage();

        // Set the "star" to show if the users are friends.
        if (!currentUser.isFriendsWith(selectedUser.getId(), getApplicationContext())) {
            friendIcon.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Grabs the profile image from the file system, transforms to Bitmap
     * and set the profileImageView to that bitmap.
     */
    private void setupProfileImage() {
        // TODO: Add a default image if bitmap is null or the uri is null
        if (selectedProfile.getProfileImageUri() != null) {
            Bitmap bitmap = null;
            String uri = selectedProfile.getProfileImageUri();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetectorCompat.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //********** OnGestureListener methods ************//

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
        // Detects if we have a valid right -> left swipe or left -> right swipe
        // And updates the users friends based on that information.
        // Logic taken from: http://androidtuts4u.blogspot.com/2013/03/swipe-or-onfling-event-android.html
        if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE) {
            // Detected left -> right swipe
            currentUser.addFriend(selectedUser.getId(), getApplicationContext());
            friendIcon.setVisibility(View.VISIBLE);
            Log.i(TAG, "Left to right fling detected");
        } else if (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE) {
            // Detected right -> left swipe
            currentUser.removeFriend(selectedUser.getId(), getApplicationContext());
            friendIcon.setVisibility(View.INVISIBLE);
            Log.i(TAG, "Right to left fling detected");
        }

        return true;
    }
}
