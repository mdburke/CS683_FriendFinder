package com.imminentapps.friendfinder.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.imminentapps.friendfinder.R;
import com.imminentapps.friendfinder.database.DatabaseTask;
import com.imminentapps.friendfinder.domain.Profile;
import com.imminentapps.friendfinder.domain.User;
import com.imminentapps.friendfinder.utils.Constants;
import com.imminentapps.friendfinder.utils.PropertiesUtil;
import com.imminentapps.friendfinder.utils.UserUtil;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

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
    private TransferUtility transferUtility;
    private AmazonS3 s3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_screen);

        // Initialize vars/fields
        usernameView = findViewById(R.id.editprofile_usernameTextView);
        listView = findViewById(R.id.hobbyListView);
        aboutMeView = findViewById(R.id.editprofile_aboutMeTextView);
        profileImageView = findViewById(R.id.editprofile_profileImageView);
        friendIcon = findViewById(R.id.friendIcon);
        gestureDetectorCompat = new GestureDetectorCompat(this, this);

        BasicAWSCredentials credentials = null;

        try {
            credentials = new BasicAWSCredentials(
                    PropertiesUtil.getProperty("AccessKey", getApplicationContext()),
                    PropertiesUtil.getProperty("SecretKey", getApplicationContext()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        s3 = new AmazonS3Client(credentials);
        transferUtility = new TransferUtility(s3, getApplicationContext());

        initializeCurrentUserData();
        initializeSelectedUserData();
    }

    private void initializeSelectedUserData() {
        // Grab the user information from the database based on the email passed in
        Intent intent = getIntent();
        String email = intent.getCharSequenceExtra("selectedUserEmail").toString();

        DatabaseTask<String, User> task = new DatabaseTask<>(new DatabaseTask.DatabaseTaskListener<User>() {
            @Override
            public void onFinished(User user) {
                // TODO: Handle this case better
                if (user == null) {
                    throw new IllegalStateException("HomeScreen was not able to locate the logged in user.");
                }
                selectedUser = user;
                selectedProfile = selectedUser.getProfile();

                // Setup the view based on the data
                usernameView.setText(selectedProfile.getUsername());
                aboutMeView.setText(UserUtil.getAboutMeText(selectedUser));

                // Setup the list adapter
                ArrayAdapter adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, selectedProfile.getHobbiesAsStrings());
                listView.setAdapter(adapter);

                // Get the profile image
                setupProfileImage();

                // Set the "star" to show if the users are friends.
                isFriendsWith(selectedUser.getId());
            }

            private void isFriendsWith(int userId) {
                DatabaseTask<Integer, Boolean> task = new DatabaseTask<>(new DatabaseTask.DatabaseTaskListener<Boolean>() {
                    @Override
                    public void onFinished(Boolean result) {
                        if (!result) {
                            friendIcon.setVisibility(View.INVISIBLE);
                        }
                    }
                }, new DatabaseTask.DatabaseTaskQuery<Integer, Boolean>() {
                    @Override
                    public Boolean execute(Integer... ids) {
                        return currentUser.isFriendsWith(ids[0], getApplicationContext());
                    }
                });

                task.execute(userId);
            }
        }, new DatabaseTask.DatabaseTaskQuery<String, User>() {
            @Override
            public User execute(String... emails) {
                return UserUtil.loadUser(emails[0]);
            }
        });

        task.execute(email);
    }

    private void initializeCurrentUserData() {
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

            }
        }, new DatabaseTask.DatabaseTaskQuery<String, User>() {
            @Override
            public User execute(String... emails) {
                return UserUtil.loadUser(emails[0]);
            }
        });

        task.execute(email);
    }

    /**
     * Grabs the profile image from the file system, transforms to Bitmap
     * and set the profileImageView to that bitmap.
     */
    private void setupProfileImage() {
        // TODO: Add a default image if bitmap is null or the uri is null
        if (selectedProfile.getProfileImageUri() != null) {
            AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... objects) {
                    S3Object object;
                    InputStream objectData = null;
                    Bitmap bitmap = null;
                    try {
                        object = s3.getObject(Constants.AWS_PROFILE_IMAGE_BUCKET, selectedProfile.getProfileImageUri());
                        objectData = object.getObjectContent();
                        bitmap = BitmapFactory.decodeStream(objectData);
                    } catch (AmazonS3Exception e) {
                        e.printStackTrace();
                        Log.i("FILE", "AWS key did not exist for profile image.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("FILE", "Error getting profile image data.");
                    } finally {
                        IOUtils.closeQuietly(objectData);
                    }
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (bitmap != null) {
                        profileImageView.setImageBitmap(bitmap);
                    }
                }
            };

            task.execute();
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
