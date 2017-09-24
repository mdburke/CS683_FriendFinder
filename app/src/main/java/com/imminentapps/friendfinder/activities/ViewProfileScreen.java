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

import java.io.FileInputStream;
import java.util.ArrayList;

public class ViewProfileScreen extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private final String TAG = this.getClass().getSimpleName();
    // Distance used to test for a valid swipe
    private static final int SWIPE_MIN_DISTANCE = 120;

    // Instance vars
    private User viewedUser;
    private User loggedInUser;
    private Profile viewedProfile;
    private GestureDetectorCompat gestureDetectorCompat;
    private ImageView friendIcon;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_screen);

        Intent intent = getIntent();

        try {
            // Grab the logged in user info and the viewed user info
            viewedUser = (User) intent.getSerializableExtra("viewedUser");
            loggedInUser = (User) intent.getSerializableExtra("loggedInUser");
        } catch (ClassCastException e) {
            // TODO: Handle this state better
            throw new IllegalStateException("Activity was not passed a valid user object.");
        }

        // Initialize vars/fields
        viewedProfile = viewedUser.getProfile();
        TextView usernameView = findViewById(R.id.usernameTextView);
        ListView listView = findViewById(R.id.hobbyListView);
        TextView aboutMeView = findViewById(R.id.aboutMeTextView);
        profileImageView = findViewById(R.id.profileImageView);
        friendIcon = findViewById(R.id.friendIcon);
        gestureDetectorCompat = new GestureDetectorCompat(this, this);

        // Setup the view based on the data
        usernameView.setText(viewedProfile.getUsername());
        aboutMeView.setText(viewedProfile.getAboutMeSection());

        // TODO: Make a better default so this is no longer needed
        if (viewedProfile.getHobbies() == null) {
            viewedProfile.setHobbies(new ArrayList<>());
        }

        // Setup the list adapter
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, viewedProfile.getHobbies());
        listView.setAdapter(adapter);

        // Get the profile image
        setupProfileImage();

        // Set the "star" to show if the users are friends.
        if (!loggedInUser.isFriendsWith(viewedUser.getId(), getApplicationContext())) {
            friendIcon.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Grabs the profile image from the file system, transforms to Bitmap
     * and set the profileImageView to that bitmap.
     */
    private void setupProfileImage() {
        // TODO: Add a default image if bitmap is null or the uri is null
        if (viewedProfile.getProfileImageUri() != null) {
            Bitmap bitmap = null;
            String uri = viewedProfile.getProfileImageUri();
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
            loggedInUser.addFriend(viewedUser.getId(), getApplicationContext());
            friendIcon.setVisibility(View.VISIBLE);
            Log.i(TAG, "Left to right fling detected");
        } else if (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE) {
            // Detected right -> left swipe
            loggedInUser.removeFriend(viewedUser.getId(), getApplicationContext());
            friendIcon.setVisibility(View.INVISIBLE);
            Log.i(TAG, "Right to left fling detected");
        }

        return true;
    }
}
