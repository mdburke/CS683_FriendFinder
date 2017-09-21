package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.imminentapps.friendfinder.utils.DBUtil;

import java.io.ByteArrayInputStream;

public class ViewProfileScreen extends AppCompatActivity implements GestureDetector.OnGestureListener {
    private DBUtil dbUtil;

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
        dbUtil = new DBUtil(getApplicationContext());

        Intent intent = getIntent();

        try {
            viewedUser = dbUtil.getUser(intent.getCharSequenceExtra("viewedUser").toString());
            loggedInUser = dbUtil.getUser(intent.getSerializableExtra("email").toString());
        } catch (ClassCastException e) {
            throw new IllegalStateException("Activity was not passed a valid user object.");
        }

        // Initialize vars/fields
        viewedProfile = viewedUser.getProfile();
        TextView usernameView = (TextView) findViewById(R.id.usernameTextView);
        ListView listView = (ListView) findViewById(R.id.hobbyListView);
        TextView aboutMeView = (TextView) findViewById(R.id.aboutMeTextView);
        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        friendIcon = (ImageView) findViewById(R.id.friendIcon);

        // Setup the view based on the data
        usernameView.setText(viewedProfile.getUsername());
        aboutMeView.setText(viewedProfile.getAboutMeSection());
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, viewedProfile.getHobbies());
        listView.setAdapter(adapter);
        setupProfileImage();

        if (!loggedInUser.isFriendsWith(viewedUser.getEmail())) {
            friendIcon.setVisibility(View.INVISIBLE);
        }

        // Initialize gesture detector
        this.gestureDetectorCompat = new GestureDetectorCompat(this, this);
    }

    private void setupProfileImage() {
        byte[] profileImage = viewedProfile.getProfileImage();
        if (profileImage != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(profileImage);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            profileImageView.setImageBitmap(bitmap);
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
            loggedInUser.addFriend(viewedUser.getEmail());
            friendIcon.setVisibility(View.VISIBLE);
        } else if (event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE) {
            // Detected right -> left swipe
            loggedInUser.removeFriend(viewedUser.getEmail());
            friendIcon.setVisibility(View.INVISIBLE);
        }

        return true;
    }
}
